package com.cmap.controller;

import java.io.IOException;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openid4java.OpenIDException;
import org.openid4java.association.AssociationSessionType;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.Discovery;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.discovery.yadis.YadisResolver;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.openid4java.server.RealmVerifierFactory;
import org.openid4java.util.HttpFetcherFactory;
import org.openid4java.util.ProxyProperties;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletConfigAware;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.comm.oidc.User;
import com.cmap.comm.oidc.X509HostnameVerifier;
import com.cmap.comm.oidc.X509TrustManager;
import com.cmap.exception.ServiceLayerException;
import com.google.gson.Gson;

/**
 * OpenID 登入驗證流程
 * 目前 for 新北教網
 * @author 不滅神話
 *
 */
@Controller
@RequestMapping("/login/authByOIDC_NTPC")
public class ConsumerController extends BaseController implements ServletConfigAware {
    @Log
    private static Logger log;

    private ServletConfig servletConfig;
    private ConsumerManager manager;

    public void init() throws ServiceLayerException {
        try {
            // --- Forward proxy setup (only if needed) ---
            /*
            ProxyProperties proxyProps = getProxyProperties(servletConfig);
            if (proxyProps != null) {
                log.info("ProxyProperties: " + proxyProps);
                HttpClientFactory.setProxyProperties(proxyProps);
            }
            */

            javax.net.ssl.X509TrustManager x509TrustManager = new X509TrustManager();
            org.apache.http.conn.ssl.X509HostnameVerifier x509HostnameVerifier = new X509HostnameVerifier();

            SSLContext sslContext = null;

            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());

            Discovery discovery = new Discovery();
            discovery.setYadisResolver(new YadisResolver(new HttpFetcherFactory(sslContext, x509HostnameVerifier)));

            this.manager = new ConsumerManager(new RealmVerifierFactory(new YadisResolver(new HttpFetcherFactory(sslContext, x509HostnameVerifier))), discovery, new HttpFetcherFactory(sslContext, x509HostnameVerifier));
            manager.setAssociations(new InMemoryConsumerAssociationStore());
            manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
            manager.setMinAssocSessEnc(AssociationSessionType.DH_SHA256);

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("loginOIDC_NTPC -- init error ! (" + e.getMessage() + ")");
        }
    }

    @RequestMapping(value = {"", "/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String doOIDC(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            if ("true".equals(request.getParameter("is_return"))) {
                return processReturn(model, principal, request, response);

            } else {
                init();
                return this.authRequest(model, principal, request, response);
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
        }

        return null;
    }

    private String processReturn(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Identifier identifier = this.verifyResponse(request);
        log.debug("identifier: " + identifier);

        if (identifier == null) {
            return "redirect:/login";

        } else {
            String[] identifierArray = identifier.getIdentifier().split("/");
            Gson gson = new Gson();
            User[] users = gson.fromJson(request.getAttribute("timezone").toString(), User[].class);

            String account = identifierArray[identifierArray.length - 1];
            String schoolId = users[0].getId();
            String username = request.getAttribute("fullname").toString();
            String email = request.getAttribute("email").toString();

            Map<String, String> roleMap = new HashMap<>();
            for (int i=0; i<users.length; i++) {
                roleMap.put(users[i].getRole(), users[i].getRole());
            }
            String[] roles = new String[roleMap.size()];
            roles = roleMap.values().toArray(roles);

            session.setAttribute(Constants.OIDC_SUB, account);
            session.setAttribute(Constants.OIDC_SCHOOL_ID, schoolId);
            session.setAttribute(Constants.OIDC_USER_NAME, username);
            session.setAttribute(Constants.OIDC_EMAIL, email);

            boolean canAccess = checkUserCanOrNotAccess(request, schoolId, roles, account);

            if (canAccess) {
                return loginAuthByPRTG(model, principal, request, schoolId);

            } else {
                session.setAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, "無網路管理系統存取權限，請與系統管理員聯繫");
                return "redirect:/loginOIDC_NTPC";
            }
        }
    }

    // --- placing the authentication request ---
    public String authRequest(Model model, Principal principal, HttpServletRequest httpReq, HttpServletResponse httpResp)
            throws IOException, ServletException {
        try {
            // configure the return_to URL where your application will receive
            // the authentication responses from the OpenID provider
            // String returnToUrl = "http://example.com/openid";
            String returnToUrl = httpReq.getRequestURL().toString()
                    + "?is_return=true";

            // perform discovery on the user-supplied identifier
            final String userSuppliedString = Env.OIDC_URL_OF_NEW_TAIPEI_CITY;
            List discoveries = manager.discover(userSuppliedString);

            // attempt to associate with the OpenID provider
            // and retrieve one service endpoint for authentication
            DiscoveryInformation discovered = manager.associate(discoveries);

            // store the discovery information in the user's session
            httpReq.getSession().setAttribute("openid-disc", discovered);

            // obtain a AuthRequest message to be sent to the OpenID provider
            AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

            // Simple registration example
            addSimpleRegistrationToAuthRequest(httpReq, authReq);

            // Attribute exchange example
            addAttributeExchangeToAuthRequest(httpReq, authReq);

            if (!discovered.isVersion2()) {
                // Option 1: GET HTTP-redirect to the OpenID Provider endpoint
                // The only method supported in OpenID 1.x
                // redirect-URL usually limited ~2048 bytes
                httpResp.sendRedirect(authReq.getDestinationUrl(true));
                return null;

            } else {
                // Option 2: HTML FORM Redirection (Allows payloads >2048 bytes)

                model.addAttribute("prameterMap", httpReq.getParameterMap());
                model.addAttribute("message", authReq);
                // httpReq.setAttribute("destinationUrl", httpResp
                // .getDestinationUrl(false));
                return "formredirection";
            }

        } catch (OpenIDException e) {
            // present error to the user
            throw new ServletException(e);
        }
    }

    /**
     * Simple Registration Extension example.
     *
     * @param httpReq
     * @param authReq
     * @throws MessageException
     * @see <a href="http://code.google.com/p/openid4java/wiki/SRegHowTo">Simple Registration HowTo</a>
     * @see <a href="http://openid.net/specs/openid-simple-registration-extension-1_0.html">OpenID Simple Registration Extension 1.0</a>
     */
    private void addSimpleRegistrationToAuthRequest(HttpServletRequest httpReq,
            AuthRequest authReq) throws MessageException {
        // Attribute Exchange example: fetching the 'email' attribute
        // FetchRequest fetch = FetchRequest.createFetchRequest();
        SRegRequest sregReq = SRegRequest.createFetchRequest();
        sregReq.setTypeUri(SRegMessage.OPENID_NS_SREG11);

        String[] attributes = { "nickname", "email", "fullname", "dob",
                "gender", "postcode", "country", "language", "timezone" };
        for (int i = 0, l = attributes.length; i < l; i++) {
                sregReq.addAttribute(attributes[i], true);
        }

        // attach the extension to the authentication request
        if (!sregReq.getAttributes().isEmpty()) {
            authReq.addExtension(sregReq);
        }
    }

    /**
     * Attribute exchange example.
     *
     * @param httpReq
     * @param authReq
     * @throws MessageException
     * @see <a href="http://code.google.com/p/openid4java/wiki/AttributeExchangeHowTo">Attribute Exchange HowTo</a>
     * @see <a href="http://openid.net/specs/openid-attribute-exchange-1_0.html">OpenID Attribute Exchange 1.0 - Final</a>
     */
    private void addAttributeExchangeToAuthRequest(HttpServletRequest httpReq,
            AuthRequest authReq) throws MessageException {
        String[] aliases = httpReq.getParameterValues("alias");
        String[] typeUris = httpReq.getParameterValues("typeUri");
        String[] counts = httpReq.getParameterValues("count");
        FetchRequest fetch = FetchRequest.createFetchRequest();
        for (int i = 0, l = typeUris == null ? 0 : typeUris.length; i < l; i++) {
            String typeUri = typeUris[i];
            if (StringUtils.isNotBlank(typeUri)) {
                String alias = aliases[i];
                boolean required = httpReq.getParameter("required" + i) != null;
                int count = NumberUtils.toInt(counts[i], 1);
                fetch.addAttribute(alias, typeUri, required, count);
            }
        }
        authReq.addExtension(fetch);
    }

    // --- processing the authentication response ---
    public Identifier verifyResponse(HttpServletRequest httpReq)
            throws ServletException {
        try {
            // extract the parameters from the authentication response
            // (which comes in as a HTTP request from the OpenID provider)
            ParameterList response = new ParameterList(httpReq
                    .getParameterMap());

            // retrieve the previously stored discovery information
            DiscoveryInformation discovered = (DiscoveryInformation) httpReq
                    .getSession().getAttribute("openid-disc");

            // extract the receiving URL from the HTTP request
            StringBuffer receivingURL = httpReq.getRequestURL();
//          String queryString = httpReq.getQueryString();
//          if (queryString != null && queryString.length() > 0)
//              receivingURL.append("?").append(httpReq.getQueryString());

            for(String key : httpReq.getParameterMap().keySet()) {
                receivingURL.append("&" + key + "=" + response.getParameterValue(key));
            }

            receivingURL.setCharAt(receivingURL.indexOf("&"), '?');

            // verify the response; ConsumerManager needs to be the same
            // (static) instance used to place the authentication request
            VerificationResult verification = manager.verify(receivingURL
                    .toString(), response, discovered);

            // examine the verification result and extract the verified
            // identifier
            Identifier verified = verification.getVerifiedId();
            if (verified != null) {
                AuthSuccess authSuccess = (AuthSuccess) verification
                        .getAuthResponse();

                receiveSimpleRegistration(httpReq, authSuccess);

                receiveAttributeExchange(httpReq, authSuccess);

                return verified; // success
            }
        } catch (OpenIDException e) {
            // present error to the user
            throw new ServletException(e);
        }

        return null;
    }

    /**
     * @param httpReq
     * @param authSuccess
     * @throws MessageException
     */
    private void receiveSimpleRegistration(HttpServletRequest httpReq,
            AuthSuccess authSuccess) throws MessageException {
        if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG11)) {
            MessageExtension ext = authSuccess
                    .getExtension(SRegMessage.OPENID_NS_SREG11);
            if (ext instanceof SRegResponse) {
                SRegResponse sregResp = (SRegResponse) ext;
                for (Iterator iter = sregResp.getAttributeNames()
                        .iterator(); iter.hasNext();) {
                    String name = (String) iter.next();
                    String value = sregResp.getParameterValue(name);
                    httpReq.setAttribute(name, value);
                }
            }
        }
    }

    /**
     * @param httpReq
     * @param authSuccess
     * @throws MessageException
     */
    private void receiveAttributeExchange(HttpServletRequest httpReq,
            AuthSuccess authSuccess) throws MessageException {
        if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
            FetchResponse fetchResp = (FetchResponse) authSuccess
                    .getExtension(AxMessage.OPENID_NS_AX);

            // List emails = fetchResp.getAttributeValues("email");
            // String email = (String) emails.get(0);

            List aliases = fetchResp.getAttributeAliases();
            Map attributes = new LinkedHashMap();
            for (Iterator iter = aliases.iterator(); iter.hasNext();) {
                String alias = (String) iter.next();
                List values = fetchResp.getAttributeValues(alias);
                if (values.size() > 0) {
                    String[] arr = new String[values.size()];
                    values.toArray(arr);
                    attributes.put(alias, StringUtils.join(arr));
                }
            }
            httpReq.setAttribute("attributes", attributes);
        }
    }

    /**
     * Get proxy properties from the context init params.
     *
     * @return proxy properties
     */
    private static ProxyProperties getProxyProperties(ServletConfig config) {
        ProxyProperties proxyProps;
        String host = config.getInitParameter("proxy.host");
        log.debug("proxy.host: " + host);
        if (host == null) {
            proxyProps = null;
        } else {
            proxyProps = new ProxyProperties();
            String port = config.getInitParameter("proxy.port");
            String username = config.getInitParameter("proxy.username");
            String password = config.getInitParameter("proxy.password");
            String domain = config.getInitParameter("proxy.domain");
            proxyProps.setProxyHostName(host);
            proxyProps.setProxyPort(Integer.parseInt(port));
            proxyProps.setUserName(username);
            proxyProps.setPassword(password);
            proxyProps.setDomain(domain);
        }
        return proxyProps;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }
}
