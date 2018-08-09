package com.cmap.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.comm.BaseAuthentication;
import com.cmap.exception.AuthenticateException;
import com.cmap.utils.impl.PrtgApiUtils;

public class RequestBodyReaderAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static Log log = LogFactory.getLog(RequestBodyReaderAuthenticationFilter.class);
    private static final String ERROR_MESSAGE = "Something went wrong while parsing /login request body";
 
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
    		PrtgApiUtils prtgApiUtils = new PrtgApiUtils();
        	boolean loginSuccess = prtgApiUtils.login(request, username, password);
        	
        	if (loginSuccess) {
        		request.getSession().setAttribute(Constants.USERROLE, Constants.USERROLE_USER);
        	}
        	
    	} catch (AuthenticateException ae) {
    		System.out.println(ae.toString());
    		
    	} catch (Exception e) {
    		if (log.isErrorEnabled()) {
    			log.error(e.toString(), e);
    		}
    		e.printStackTrace();
    	}
    	
    	BaseAuthentication.authAdminUser(request, username, password);
    	BaseAuthentication.authAdminRole(request, username);
    }
    
    /**
     * 攔截登入表單，進行PRTG驗證
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String requestBody;
//            requestBody = IOUtils.toString(request.getReader());
            
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        
        request.getSession().setAttribute(Constants.USERNAME, username);
		request.getSession().setAttribute(Constants.PASSWORD, password);
        
//            Map<String, String> authMap = composeUserNamePasswordMap(requestBody);
        	
    	if (Env.LOGIN_AUTH_MODE.equals(Constants.LOGIN_AUTH_MODE_PRTG)) {
    		loginAuthByPRTG(request, username, password);
    	}
    	
    	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, token);

        return this.getAuthenticationManager().authenticate(token);
    }
}
