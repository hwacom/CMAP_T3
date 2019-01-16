package com.cmap.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.comm.BaseAuthentication;
import com.cmap.configuration.security.CustomAuthenticationProvider;
import com.cmap.exception.AuthenticateException;
import com.cmap.extension.openid.connect.sdk.ConfigurationErrorResponse;
import com.cmap.extension.openid.connect.sdk.ConfigurationRequest;
import com.cmap.extension.openid.connect.sdk.ConfigurationResponse;
import com.cmap.extension.openid.connect.sdk.ConfigurationSuccessResponse;
import com.cmap.extension.openid.connect.sdk.EduInfoErrorResponse;
import com.cmap.extension.openid.connect.sdk.EduInfoRequest;
import com.cmap.extension.openid.connect.sdk.EduInfoResponse;
import com.cmap.extension.openid.connect.sdk.EduInfoSuccessResponse;
import com.cmap.security.SecurityUtil;
import com.cmap.service.UserService;
import com.cmap.service.vo.PrtgServiceVO;
import com.cmap.utils.ApiUtils;
import com.cmap.utils.impl.PrtgApiUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.UserInfoSuccessResponse;

import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/login/code")
public class OidcController extends BaseController {
	@Log
	private static Logger log;

	@Autowired
	private CustomAuthenticationProvider customerAuthProvider;

	@Autowired
	private UserService userService;

	private ClientID clientID = null;
	private Secret clientSecret = null;
	private URI configurationEndpointURL = null;
	private URI tokenEndpoint = null;
	private AuthorizationCode code = null;
	private URI callback = null;
	private URI userinfoEndpointURL = null;
	private URI eduinfoEndpointURL = null;

	@RequestMapping(value = "getConfig", method = RequestMethod.GET)
	public String getConfig(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
		HttpSession session = request.getSession();

		try {
			configurationEndpointURL = new URI(session.getAttribute(Constants.OIDC_CONFIGURATION_ENDPOINT).toString());

	        // Append the access token to form actual request
	        ConfigurationRequest configurationReq = new ConfigurationRequest(configurationEndpointURL, new BearerAccessToken());

	        HTTPResponse configurationHTTPResponse = null;
	        //觀察送出的header
	        log.info("configuration request header:" + configurationReq.toHTTPRequest().getHeaderMap().toString());
	        configurationHTTPResponse = configurationReq.toHTTPRequest().send();

	        ConfigurationResponse configurationResponse = ConfigurationResponse.parse(configurationHTTPResponse);

	        if (configurationResponse instanceof ConfigurationErrorResponse) {
	            ErrorObject error = ((ConfigurationErrorResponse) configurationResponse).getErrorObject();
	            // TODO error handling
	        }

	        ConfigurationSuccessResponse successConfigurationResponse = (ConfigurationSuccessResponse) configurationResponse;
	        JSONObject configurationJSON = successConfigurationResponse.getGeneralInfo().toJSONObject();
	        log.info("configurationJSON: " + configurationJSON);

	        final String AUTH_ENDPOINT = configurationJSON.getAsString("authorization_endpoint");
	        final String JWKS_URI = configurationJSON.getAsString("jwks_uri");
	        final String TOKEN_ENDPOINT = configurationJSON.getAsString("token_endpoint");
	        final String USER_INFO_ENDPOINT = configurationJSON.getAsString("userinfo_endpoint");

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return null;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getCode(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
	    HttpSession session = request.getSession();

	    final String ipAddr = SecurityUtil.getIpAddr(request);
	    session.setAttribute(Constants.IP_ADDR, ipAddr);

        callback = new URI(session.getAttribute(Constants.OIDC_REDIRECT_URI).toString());
        clientID = new ClientID(session.getAttribute(Constants.OIDC_CLIENT_ID).toString());
        clientSecret = new Secret(session.getAttribute(Constants.OIDC_CLIENT_SECRET).toString());
        tokenEndpoint = new URI(session.getAttribute(Constants.OIDC_TOKEN_ENDPOINT).toString());

		try {
			//authCode 如果還沒取得,表示要從Auth Server 發過來
            if (session.getAttribute("code") == null) {
                //log.info(request.getQueryString());
                String queryString = request.getQueryString();
                String responseURL = "https:///path/?" + queryString;
                log.info(responseURL);

                log.info("session state:" + session.getAttribute(Constants.OIDC_STATE));
                String state = session.getAttribute(Constants.OIDC_STATE).toString();

                AuthenticationResponse authResponse = AuthenticationResponseParser.parse(new URI(responseURL));
                AuthenticationSuccessResponse successResponse = (AuthenticationSuccessResponse) authResponse;
                // Retrieve the authorisation code
                code = successResponse.getAuthorizationCode();

                if (code == null || (code != null && StringUtils.isBlank(code.getValue()))) {
                	session.setAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, "取得教育雲授權失敗，請重新操作或聯絡系統管理員");
                    return "redirect:/login";
    			}

                log.info("3. auth code grant.");
                log.info("code:" + code.getValue());
                session.setAttribute(Constants.OIDC_CODE, code.getValue());

                log.info("return state:" + successResponse.getState().toString());

                // 驗證Session連續性
    			if (!successResponse.getState().toString().equals(state)) {
    				session.setAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, "取得教育雲授權失敗，請重新操作或聯絡系統管理員");
    	            return "redirect:/login";
    			}
                log.info("the same state");

            } else {
                log.info("auth code from session:" + session.getAttribute(Constants.OIDC_CODE).toString());
                code = new AuthorizationCode(session.getAttribute(Constants.OIDC_CODE).toString());
            }

            return getToken(model, principal, request, response);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
		}

		return "";
	}

	public String getToken(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HttpSession session = request.getSession();

	    /*
         * Step 2. 取得 Access Token
         */
        AuthorizationGrant codeGrant = new AuthorizationCodeGrant(code, callback);
        ClientAuthentication clientAuth = new ClientSecretPost(clientID, clientSecret);

        // Make the token request
        TokenRequest tokenRequest
                = new TokenRequest(tokenEndpoint, clientAuth, codeGrant);

//      log.info("Token Authorization Header : " + httpRequest.getAuthorization());
        log.info("4. Access Token Request");
        TokenResponse tokenResponse = OIDCTokenResponseParser.parse(tokenRequest.toHTTPRequest().send());
        if (tokenResponse instanceof TokenErrorResponse) {
            TokenErrorResponse errorResponse = (TokenErrorResponse) tokenResponse;
            log.info("error happened!!");
            log.info(errorResponse.getErrorObject().getCode());
            log.error(String.format("%d", errorResponse.getErrorObject().getHTTPStatusCode()));

            session.setAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, "取得教育雲授權失敗，請重新操作或聯絡系統管理員");
            return "redirect:/login";

        } else {
            OIDCTokenResponse accessTokenResponse = (OIDCTokenResponse) tokenResponse;
            BearerAccessToken accessToken
                    = accessTokenResponse.getOIDCTokens().getBearerAccessToken();

            SignedJWT idToken = (SignedJWT) accessTokenResponse.getOIDCTokens().getIDToken();
            RefreshToken refreshToken = accessTokenResponse.getOIDCTokens().getRefreshToken();

            log.info("5 Access Token Grant.");
            log.info("access token value:" + accessToken.getValue());
            log.info("idToken value:" + idToken.getParsedString());
            session.setAttribute(Constants.OIDC_ACCESS_TOKEN, accessToken.getValue());
            session.setAttribute(Constants.OIDC_ID_TOKEN, idToken.getParsedString());
            session.setAttribute(Constants.OIDC_REFRESH_TOKEN, refreshToken.getValue());

            return getUserInfo(model, principal, request, response);
        }
	}

	private String getUserInfo(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		BearerAccessToken accessToken = new BearerAccessToken((session.getAttribute(Constants.OIDC_ACCESS_TOKEN).toString()));
        String idToken = session.getAttribute(Constants.OIDC_ID_TOKEN).toString();
        log.info("session idToken:" + idToken);

        userinfoEndpointURL = new URI(session.getAttribute(Constants.OIDC_USER_INFO_ENDPOINT).toString());

        // Append the access token to form actual request
        UserInfoRequest userInfoReq = new UserInfoRequest(userinfoEndpointURL, accessToken);

        HTTPResponse userInfoHTTPResponse = null;
        //觀察送出的header
        log.info("userinfo request header:" + userInfoReq.toHTTPRequest().getHeaderMap().toString());
        userInfoHTTPResponse = userInfoReq.toHTTPRequest().send();

        UserInfoResponse userInfoResponse = UserInfoResponse.parse(userInfoHTTPResponse);

        if (userInfoResponse instanceof UserInfoErrorResponse) {
            ErrorObject error = ((UserInfoErrorResponse) userInfoResponse).getErrorObject();
            // TODO error handling
        }

        UserInfoSuccessResponse successUserInfoResponse = (UserInfoSuccessResponse) userInfoResponse;
        JSONObject userInfoJSON = successUserInfoResponse.getUserInfo().toJSONObject();
        String userName = userInfoJSON.getAsString(Env.OIDC_USERINFO_ENDPOINT_JSON_NAME_NODE);
        String email = userInfoJSON.getAsString(Env.OIDC_USERINFO_ENDPOINT_JSON_EMAIL_NODE);
        String jsonStr = userInfoJSON.toJSONString();
        log.info("UserInfoSuccessResponse: " + jsonStr);

        session.setAttribute(Constants.OIDC_USER_INFO_JSON, jsonStr);
        session.setAttribute(Constants.OIDC_USER_NAME, userName);
        session.setAttribute(Constants.OIDC_EMAIL, email);

        // Set up a JWT processor to parse the tokens and then check their signature
        // and validity time window (bounded by the "iat", "nbf" and "exp" claims)
        ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();

        // The public RSA keys to validate the signatures will be sourced from the
        //OAuth 2.0 server's JWK set, published at a well-known URL. The RemoteJWKSet
        //object caches the retrieved keys to speed up subsequent look-ups and can
        //also gracefully handle key-rollover

        String jwksURI = session.getAttribute(Constants.OIDC_JWKS_URI).toString();
        //        JWKSource keySource = new RemoteJWKSet(new URL("https://oidc.tanet.edu.tw/oidc/v1/jwksets"));
        JWKSource keySource = new RemoteJWKSet(new URL(jwksURI));

        // The expected JWS algorithm of the access tokens (agreed out-of-band)
        JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;

        // Configure the JWT processor with a key selector to feed matching public
        //RSA keys sourced from the JWK set URL
        JWSKeySelector keySelector = new JWSVerificationKeySelector(expectedJWSAlg, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);

        //Process the token
        SecurityContext ctx = null; // optional context parameter, not required here
        JWTClaimsSet claimsSet = jwtProcessor.process(idToken, ctx);

        log.info(claimsSet.toString());
        String idTokenParsed = claimsSet.toString();

        //migration from openid2.0
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(idTokenParsed);

        log.info("jsonnode:" + root.toString());
        log.info("open2_id: " + root.get(Env.OIDC_USERINFO_ENDPOINT_JSON_OPEN2ID_NODE).toString().replace("\"", ""));

        String oidcOpen2Id = root.get(Env.OIDC_USERINFO_ENDPOINT_JSON_OPEN2ID_NODE).toString();

        session.setAttribute(Constants.OIDC_OPEN_2_ID, oidcOpen2Id);
        session.setAttribute(Constants.OIDC_SUB, claimsSet.getSubject());	// 取得Subject值做為識別, 進行帳號識別

        return getEduInfo(model, principal, request, response);
	}

	private String getEduInfo(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		BearerAccessToken accessToken = new BearerAccessToken((session.getAttribute(Constants.OIDC_ACCESS_TOKEN).toString()));
        String idToken = session.getAttribute(Constants.OIDC_ID_TOKEN).toString();
        log.info("session idToken:" + idToken);

        eduinfoEndpointURL = new URI(session.getAttribute(Constants.OIDC_EDU_INFO_ENDPOINT).toString());

        // Append the access token to form actual request
        EduInfoRequest eduInfoReq = new EduInfoRequest(eduinfoEndpointURL, accessToken);

        HTTPResponse eduInfoHTTPResponse = null;
        //觀察送出的header
        log.info("userinfo request header:" + eduInfoReq.toHTTPRequest().getHeaderMap().toString());
        eduInfoHTTPResponse = eduInfoReq.toHTTPRequest().send();

        EduInfoResponse eduInfoResponse = EduInfoResponse.parse(eduInfoHTTPResponse);

        if (eduInfoResponse instanceof EduInfoErrorResponse) {
            ErrorObject error = ((EduInfoErrorResponse) eduInfoResponse).getErrorObject();
            // TODO error handling
        }

        EduInfoSuccessResponse successEduInfoResponse = (EduInfoSuccessResponse) eduInfoResponse;
        String eduInfo = successEduInfoResponse.getGeneralInfo().toJSONObject().toString();
        log.info("EduInfoSuccessResponse: " + eduInfo);

        //migration from openid2.0
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(eduInfo);

        /*
        JsonNode titles = root.get("titles");
        List<JsonNode> schoolIDs = titles.findValues("schoolid");
        */

        String schoolId = root.get(Env.OIDC_EDUINFO_ENDPOINT_JSON_SCHOOLID_NODE).toString().replace("\"", "");
        log.info("jsonnode:" + root.toString());
        log.info("schoolid: " + schoolId);

        session.setAttribute(Constants.OIDC_EDU_INFO_JSON, root.toString());
        session.setAttribute(Constants.OIDC_SCHOOL_ID, schoolId);

        final String account = Objects.toString(request.getSession().getAttribute(Constants.OIDC_SUB));
        boolean canAccess = checkUserCanOrNotAccess(request, account);

        if (canAccess) {
        	return loginAuthByPRTG(model, principal, request, schoolId);

        } else {
        	session.setAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, "無網路管理系統存取權限，請與系統管理員聯繫");
            return "redirect:/login";
        }
	}

	private String loginAuthByPRTG(Model model, Principal principal, HttpServletRequest request, String sourceId) {
		HttpSession session = request.getSession();
		PrtgServiceVO prtgVO = null;

		try {
			prtgVO = commonService.findPrtgLoginInfo(sourceId);

			if (prtgVO == null ||
					(prtgVO != null && StringUtils.isBlank(prtgVO.getAccount()) && StringUtils.isBlank(prtgVO.getPassword()))) {
				throw new AuthenticateException("PRTG登入失敗 >> 取不到 Prtg_Account_Mapping 資料 (sourceId: " + sourceId + " )");
			}

			ApiUtils prtgApiUtils = new PrtgApiUtils();
			boolean loginSuccess = prtgApiUtils.login(request, prtgVO.getAccount(), prtgVO.getPassword());

			if (!loginSuccess) {
				throw new AuthenticateException("PRTG登入失敗 >> prtgApiUtils.login return false");
			}

			String role = Objects.toString(session.getAttribute(Constants.USERROLE), null);

			if (StringUtils.isBlank(role)) {
				request.getSession().setAttribute(Constants.USERROLE, Constants.USERROLE_USER);

			} else {
				if (role.indexOf(Constants.USERROLE_USER) == -1) {
					role = role.concat(Env.COMM_SEPARATE_SYMBOL).concat(Constants.USERROLE_USER);
					request.getSession().setAttribute(Constants.USERROLE, role);
				}
			}

			String userOIDCSub = Objects.toString(request.getSession().getAttribute(Constants.OIDC_SUB), null);

			if (StringUtils.isNotBlank(userOIDCSub)) {
				BaseAuthentication.authAdminRole(request, userOIDCSub);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);

			session.setAttribute(Constants.MODEL_ATTR_LOGIN_ERROR, "PRTG登入失敗，請重新操作或聯絡系統管理員");
            return "redirect:/login";
		}

		return super.manualAuthenticatd4EduOIDC(model, principal, request);
	}
}
