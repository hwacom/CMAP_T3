package com.cmap.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (null == principal) {
				return "redirect:/login";
			}

			RequestDispatcher rd = request.getRequestDispatcher("/prtg/index");
			rd.forward(request,response);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return null;
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String indexPage(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (null == principal) {
				return "redirect:/login";
			}

			RequestDispatcher rd = request.getRequestDispatcher("/prtg/index");
			rd.forward(request,response);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return null;
	}

	@RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
	public String loginPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "langType", defaultValue = "en_US") String langType,
			Locale locale,
			Principal principal,
			Model model) {
		LocaleContextHolder.getLocale();
		
		if (Env.LOGIN_AUTH_MODE.equals(Constants.LOGIN_AUTH_MODE_OIDC)) {
			return "login_openid";
			
		} else {
			return "login_openid";
		}
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
				
				model.addAttribute("LOGIN_EXCEPTION", "連接苗栗縣教育雲端帳號認證服務失敗，請重新操作或聯絡系統管理員");
				return "login_openid";
			}

        } catch (URISyntaxException ex) {
        	log.error(ex.toString(), ex);
        	
        	model.addAttribute("LOGIN_EXCEPTION", "OIDC授權驗證流程發生問題，請重新操作");
			return "login_openid";
        }
		
		return null;
	}
}
