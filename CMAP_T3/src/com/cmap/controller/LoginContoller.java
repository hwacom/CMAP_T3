package com.cmap.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Locale;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cmap.AppResponse;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.Nonce;

@Controller
@RequestMapping("/")
public class LoginContoller extends BaseController {
	@Log
	private static Logger log;

	private String chkLoginPage(HttpServletRequest request) {
	    HttpSession session = request.getSession();

	    if (Env.LOGIN_AUTH_MODE.equals(Constants.LOGIN_AUTH_MODE_OIDC_MIAOLI)) {
            return "redirect:/loginOIDC";

        } else if (Env.LOGIN_AUTH_MODE.equals(Constants.LOGIN_AUTH_MODE_OIDC_NEW_TAIPEI)) {
            String preUrl = ObjectUtils.toString(session.getAttribute(Constants.PREVIOUS_URL), null);

            if (StringUtils.isBlank(preUrl) || StringUtils.equals(preUrl, "/") || StringUtils.equals(preUrl, "/login")) {
                return "redirect:/loginOIDC_NTPC";

            } else {
                return "redirect:" + preUrl;
            }

        } else {
            return "redirect:/login";
        }
	}

	/**
	 ** 判斷要導到哪種登入頁面
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public String check(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
	        HttpSession session = request.getSession();
            if (session != null) {
                DefaultSavedRequest dsr = (DefaultSavedRequest)session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                if (dsr != null) {
                    String servletPath = dsr.getServletPath();
                    session.setAttribute(Constants.PREVIOUS_URL, servletPath);
                }
            }

			if (null == principal) {
				return chkLoginPage(request);
			}

			return "redirect:" + Env.HOME_PAGE;

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return null;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (null == principal) {
			    return chkLoginPage(request);
			}

			return "redirect:" + Env.HOME_PAGE;

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return null;
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String indexPage(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (null == principal) {
			    return chkLoginPage(request);
			}

			String previousPage = Objects.toString(request.getSession().getAttribute(Constants.PREVIOUS_URL));

			String redirectUrl = StringUtils.isNotBlank(previousPage) && StringUtils.contains(previousPage, "/plugin/module/vmswitch/power/off")
			                        ? previousPage : Env.HOME_PAGE;

			return "redirect:" + redirectUrl;

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return null;
	}

	@RequestMapping(value = "login/app", method = RequestMethod.GET)
    public String loginForApp(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            session.setAttribute(Constants.LOGIN_FROM_APP, Constants.DATA_Y);
            session.removeAttribute(Constants.PREVIOUS_URL);
            return "redirect:/loginOIDC_NTPC";  //TODO:先寫死 for APP測試用

        } catch (Exception e) {
            log.error(e.toString(), e);
        }

        return null;
    }

	@RequestMapping(value = "login/returnApp", method = RequestMethod.GET)
    public @ResponseBody AppResponse loginReturnApp(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

	    HttpSession session = request.getSession();
	    String username = Objects.toString(session.getAttribute(Constants.PRTG_LOGIN_ACCOUNT));
	    String passhash = Objects.toString(session.getAttribute(Constants.PASSHASH));

        AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "Success");
        app.putData(Constants.USERNAME, username);
        app.putData(Constants.PASSHASH, passhash);
        return app;
    }

	@RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
	public String loginPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "langType", defaultValue = "en_US") String langType,
			Locale locale,
			Principal principal,
			Model model) {

		HttpSession session = request.getSession();
		LocaleContextHolder.getLocale();

		final String loginError = Objects.toString(session.getAttribute(Constants.MODEL_ATTR_LOGIN_ERROR), null);
		if (StringUtils.isNotBlank(loginError)) {
			model.addAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, loginError);
			session.removeAttribute(Constants.MODEL_ATTR_LOGIN_ERROR);
			return chkLoginPage(request);

		} else {
			if (Env.LOGIN_AUTH_MODE.equals(Constants.LOGIN_AUTH_MODE_OIDC_MIAOLI)) {
				URI configurationEndpoint = null;
				try {
					configurationEndpoint = new URI(Env.OIDC_CONFIGURATION_ENDPOINT);

				} catch (URISyntaxException e) {
					log.error(e.toString(), e);

					try {
						configurationEndpoint = new URI(Constants.OIDC_MLC_CONFIGURATION_ENDPOINT);

					} catch (URISyntaxException e1) {
						log.error(e1.toString(), e1);
					}
				}
				request.getSession().setAttribute(Constants.OIDC_CONFIGURATION_ENDPOINT, configurationEndpoint.toString());

				return "redirect:/loginOIDC";

			} else {
		        return "login";
			}
		}
	}

	@RequestMapping(value = "loginOIDC", method = {RequestMethod.GET, RequestMethod.POST})
	public String loginOIDCPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "langType", defaultValue = "en_US") String langType,
			Locale locale,
			Principal principal,
			Model model) {

		HttpSession session = request.getSession();
		LocaleContextHolder.getLocale();

		final String loginError = Objects.toString(session.getAttribute(Constants.MODEL_ATTR_LOGIN_ERROR), null);
		if (StringUtils.isNotBlank(loginError)) {
			model.addAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, loginError);
			session.removeAttribute(Constants.MODEL_ATTR_LOGIN_ERROR);

			if (Env.LOGIN_AUTH_MODE.equals(Constants.LOGIN_AUTH_MODE_OIDC_MIAOLI)) {
				return "login_openid_mlc";
			} else {
				return "redirect:/login";
			}

		} else {
			if (Env.LOGIN_AUTH_MODE.equals(Constants.LOGIN_AUTH_MODE_OIDC_MIAOLI)) {
				URI configurationEndpoint = null;
				try {
					configurationEndpoint = new URI(Env.OIDC_CONFIGURATION_ENDPOINT);

				} catch (URISyntaxException e) {
					log.error(e.toString(), e);

					try {
						configurationEndpoint = new URI(Constants.OIDC_MLC_CONFIGURATION_ENDPOINT);

					} catch (URISyntaxException e1) {
						log.error(e1.toString(), e1);
					}
				}
				request.getSession().setAttribute(Constants.OIDC_CONFIGURATION_ENDPOINT, configurationEndpoint.toString());

				return "login_openid_mlc";

			} else {
				return "redirect:/login";
			}
		}
	}

	@RequestMapping(value = "loginOIDC_NTPC", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginOIDC_NTPC_Page(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "langType", defaultValue = "en_US") String langType,
            Locale locale,
            Principal principal,
            Model model) {

        HttpSession session = request.getSession();
        LocaleContextHolder.getLocale();

        final String loginError = Objects.toString(session.getAttribute(Constants.MODEL_ATTR_LOGIN_ERROR), null);
        if (StringUtils.isNotBlank(loginError)) {
            model.addAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, loginError);
            session.removeAttribute(Constants.MODEL_ATTR_LOGIN_ERROR);
        }

        /*
        if (Env.LOGIN_AUTH_MODE.equals(Constants.LOGIN_AUTH_MODE_OIDC_NEW_TAIPEI)) {
            return "login_openid_ntpc";
        } else {
            return "redirect:/login";
        }
        */
        //TODO:先寫死for新北教網
        return "login_openid_ntpc";
    }

	@RequestMapping(value = "login/authByOIDC", method = {RequestMethod.GET, RequestMethod.POST})
	public String authByOIDC(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		ClientID clientID = null;
	    Secret clientSecret = null;
	    URI tokenEndpoint = null;
	    URI authEndpoint = null;
	    String jwksURI = null;
	    URI userinfoEndpointURL = null;
	    URI eduinfoEndpointURL = null;
        String redirectURI = null;

		HttpSession session = request.getSession();
        String login = request.getParameter("login");
        if (login == null) {
            login = "mlc";  //預設苗栗縣教育雲帳號服務登入
        }
//        logger.info(login);
        try {

            if (login.equals("google")) {
            	/*
//                google clientid
                clientID = new ClientID("432402061677-ivsu1a14dtah90f4on0p5tsirfktfj8j.apps.googleusercontent.com");
                clientSecret = new Secret("I9Jgk7y9RdxdfptniK51mQxg");
                authEndpoint = new URI("https://accounts.google.com/o/oauth2/auth");
                tokenEndpoint = new URI("https://www.googleapis.com/oauth2/v4/token");
                userinfoEndpointURL = new URI("https://www.googleapis.com/oauth2/v3/userinfo");
                jwksURI = "https://www.googleapis.com/oauth2/v3/certs";
//                redirectURI = "http://localhost:8080/demoApp/callback";
                redirectURI = "https://coding.teliclab.info/demoApp/callback";
                */

            } else {
                //mlc clientid
                clientID = new ClientID(Env.OIDC_CLIENT_ID);
                clientSecret = new Secret(Env.OIDC_CIENT_SECRET);
                authEndpoint = new URI(Env.OIDC_AUTH_ENDPOINT);
                tokenEndpoint = new URI(Env.OIDC_TOKEN_ENDPOINT);
                userinfoEndpointURL = new URI(Env.OIDC_USER_INFO_ENDPOINT);
                eduinfoEndpointURL = new URI(Env.OIDC_EDU_INFO_ENDPOINT);
                jwksURI = Env.OIDC_JWKS_URI;
                redirectURI = Env.OIDC_REDIRECT_URI;
            }

            session.setAttribute(Constants.OIDC_CLIENT_ID, clientID.getValue());
            session.setAttribute(Constants.OIDC_CLIENT_SECRET, clientSecret.getValue());
            session.setAttribute(Constants.OIDC_TOKEN_ENDPOINT, tokenEndpoint.toString());
            session.setAttribute(Constants.OIDC_USER_INFO_ENDPOINT, userinfoEndpointURL.toString());
            session.setAttribute(Constants.OIDC_EDU_INFO_ENDPOINT, eduinfoEndpointURL.toString());
            session.setAttribute(Constants.OIDC_JWKS_URI, jwksURI);

            URI callback = new URI(redirectURI);
            session.setAttribute(Constants.OIDC_REDIRECT_URI, redirectURI);

            // Generate random state string for pairing the response to the request
            State state = new State();
            session.setAttribute(Constants.OIDC_STATE, state.toString());

            // Generate nonce
            Nonce nonce = new Nonce();

            // Compose the request (in code flow)
            AuthenticationRequest authzReq = new AuthenticationRequest(
                    authEndpoint,
                    new ResponseType(Env.OIDC_RESPONSE_TYPE),
                    Scope.parse(Env.OIDC_SCOPE),
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

				model.addAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, "連接苗栗縣教育雲端帳號認證服務失敗，請重新操作或聯絡系統管理員");
				return "login_openid_mlc";
			}

        } catch (URISyntaxException ex) {
        	log.error(ex.toString(), ex);

        	model.addAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, "OIDC授權驗證流程發生問題，請重新操作");
			return "login_openid_mlc";
        }

		return null;
	}
}
