package com.cmap.configuration.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.comm.BaseAuthentication;
import com.cmap.exception.AuthenticateException;
import com.cmap.security.SecurityUtil;
import com.cmap.utils.ApiUtils;
import com.cmap.utils.impl.PrtgApiUtils;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.Nonce;

public class RequestBodyReaderAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Log
	private static Logger log;

	public RequestBodyReaderAuthenticationFilter() {
	}

	/**
	 * 解析request form資訊取得使用者輸入的帳號&密碼
	 * @param requestBody
	 * @return
	 */
	/*
    private Map<String, String> composeUserNamePasswordMap(String requestBody) {
    	Map<String, String> retMap = new HashMap<String, String>();

    	//username=prtgadmin&password=prtgadmin
    	if (requestBody != null && requestBody.indexOf("&") != -1) {
    		String[] temp = requestBody.split("&");
        	for (String t : temp) {
        		String key = t.split("=")[0];
        		String value = t.split("=")[1];
        		retMap.put(key, value);
        	}
    	}

    	return retMap;
    }
	 */

	private void loginAuthByPRTG(HttpServletRequest request, String username, String password) {
		try {
			final String ipAddr = SecurityUtil.getIpAddr(request);
			request.getSession().setAttribute(Constants.IP_ADDR, ipAddr);

			ApiUtils prtgApiUtils = new PrtgApiUtils();
			boolean loginSuccess = prtgApiUtils.login(request, username, password);

			if (loginSuccess) {
				request.getSession().setAttribute(Constants.USERROLE, Constants.USERROLE_USER);
			}

		} catch (AuthenticateException ae) {
			log.error(ae.toString());

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		BaseAuthentication.authAdminUser(request, username, password);
		BaseAuthentication.authAdminRole(request, username);
	}

	private void loginAuthByOIDC(HttpServletRequest request, HttpServletResponse response) {
		ClientID clientID;
	    Secret clientSecret;
	    URI tokenEndpoint;
	    URI authEndpoint;
	    String jwksURI;

		HttpSession session = request.getSession();
        String login = request.getParameter("login");
        if (login == null) {
            login = "mlc";  //預設教育部帳號服務登入
        }
        URI userinfoEndpointURL;
        String redirectURI;
//        logger.info(login);
        try {

            if (login.equals("google")) {
//                google clientid
                clientID = new ClientID("432402061677-ivsu1a14dtah90f4on0p5tsirfktfj8j.apps.googleusercontent.com");
                clientSecret = new Secret("I9Jgk7y9RdxdfptniK51mQxg");
                authEndpoint = new URI("https://accounts.google.com/o/oauth2/auth");
                tokenEndpoint = new URI("https://www.googleapis.com/oauth2/v4/token");
                userinfoEndpointURL = new URI("https://www.googleapis.com/oauth2/v3/userinfo");
                jwksURI = "https://www.googleapis.com/oauth2/v3/certs";
//                redirectURI = "http://localhost:8080/demoApp/callback";
                redirectURI = "https://coding.teliclab.info/demoApp/callback";

            } else {
                //moe clientid
                clientID = new ClientID("e1dea85308ad402e988d73c9dd7d27b7");
                clientSecret = new Secret("52ba124a2aeb74927646a28e6e9869d5e79d225cd1b1db2318e4582d07c72254");
                authEndpoint = new URI("https://mlc.sso.edu.tw/oidc/v1/azp");
                tokenEndpoint = new URI("https://mlc.sso.edu.tw/oidc/v1/token");
                userinfoEndpointURL = new URI("https://mlc.sso.edu.tw/oidc/v1/userinfo");
                jwksURI = "https://mlc.sso.edu.tw/oidc/v1/jwksets";
                redirectURI = "https://163.19.163.170/login/code";

            }

            session.setAttribute("clientID", clientID.getValue());
//            logger.info("clientID:" + clientID.getValue());
            session.setAttribute("clientSecret", clientSecret.getValue());
//            logger.info("client Secret:" + clientSecret.getValue());
            session.setAttribute("tokenEndpoint", tokenEndpoint.toString());
//            logger.info("token endpoint"+ tokenEndpoint.toString());
            session.setAttribute("userinfoEndpointURL", userinfoEndpointURL.toString());
            session.setAttribute("jwksURI", jwksURI);
            // The client callback URI, typically pre-registered with the server
//            URI callback = new URI("https://coding.teliclab.info/demoApp/callback");

            URI callback = new URI(redirectURI);
            session.setAttribute("redirectURI", redirectURI);

            // Generate random state string for pairing the response to the request
            State state = new State();
            session.setAttribute("state", state.toString());

            // Generate nonce
            Nonce nonce = new Nonce();

            // Compose the request (in code flow)
            AuthenticationRequest authzReq = new AuthenticationRequest(
                    authEndpoint,
                    new ResponseType("code"),
                    Scope.parse("openid profile email openid2"),
                    clientID,
                    callback,
                    state,
                    nonce);

            log.info("1.User authorization request");

            log.info(authzReq.getEndpointURI().toString() + "?" + authzReq.toQueryString());
            try {
				response.sendRedirect(authzReq.getEndpointURI().toString() + "?" + authzReq.toQueryString());

			} catch (IOException ioe) {
				log.error(ioe.toString(), ioe);
			}

        } catch (URISyntaxException ex) {
        	log.error(ex.toString(), ex);
        }
	}

	/**
	 * 攔截登入表單，進行PRTG驗證
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		//        String requestBody;
		//            requestBody = IOUtils.toString(request.getReader());

		//每次登入動作首先清空Session所有值
		request.getSession().invalidate();

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		request.getSession().setAttribute(Constants.USERNAME, username);
		request.getSession().setAttribute(Constants.PASSWORD, password);

		//            Map<String, String> authMap = composeUserNamePasswordMap(requestBody);

		switch (Env.LOGIN_AUTH_MODE) {
			case Constants.LOGIN_AUTH_MODE_PRTG:
				loginAuthByPRTG(request, username, password);
				break;

			case Constants.LOGIN_AUTH_MODE_OIDC:
				loginAuthByOIDC(request, response);
				break;
		}

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

		// Allow subclasses to set the "details" property
		setDetails(request, token);

		return this.getAuthenticationManager().authenticate(token);
	}
}
