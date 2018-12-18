package com.cmap.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.security.SecurityUtil;
import com.cmap.service.CommonService;
import com.cmap.service.vo.PrtgServiceVO;
import com.cmap.utils.impl.CloseableHttpClientUtils;

@Controller
@RequestMapping("/prtg")
public class PrtgController extends BaseController {
	@Log
	private static Logger log;

	@Autowired
	private CommonService commonService;

	private void init(Model model) {
		model.addAttribute("PRTG_IP_ADDR", Env.PRTG_SERVER_IP);
		model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
	}

	private void sendLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();

		try {
			final String sourceId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);
			PrtgServiceVO psVO = commonService.findPrtgLoginInfo(sourceId);

			if (psVO != null) {
				final String account = psVO.getAccount();
				final String password = psVO.getPassword();

				CloseableHttpClient httpclient = CloseableHttpClientUtils.prepare();

				/*
				 * Step 1. Login
				 */
				HttpPost httpPost = new HttpPost(Env.PRTG_LOGIN_URI);

				RequestConfig requestConfig = RequestConfig.custom()
						.setConnectTimeout(Env.HTTP_CONNECTION_TIME_OUT)				//設置連接逾時時間，單位毫秒。
						.setConnectionRequestTimeout(Env.HTTP_CONNECTION_TIME_OUT)	//設置從connect Manager獲取Connection 超時時間，單位毫秒。這個屬性是新加的屬性，因為目前版本是可以共用連接池的。
						.setSocketTimeout(Env.HTTP_SOCKET_TIME_OUT)					//請求獲取資料的超時時間，單位毫秒。 如果訪問一個介面，多少時間內無法返回資料，就直接放棄此次調用。
						.build();
				httpPost.setConfig(requestConfig);

				List<NameValuePair> params = new ArrayList<>();
				params.add(new BasicNameValuePair("loginurl", "/welcome.htm"));
	            params.add(new BasicNameValuePair("username", account));
	            params.add(new BasicNameValuePair("password", password));

	            HttpEntity httpEntity = new UrlEncodedFormEntity(params, "UTF-8");
	            httpPost.setEntity(httpEntity);

				log.info("Executing request " + httpPost.getRequestLine());

				HttpClientContext context = HttpClientContext.create();
				CloseableHttpResponse response = httpclient.execute(httpPost, context);

				CookieStore cookieStore = context.getCookieStore();
				try {
					int statusCode = response.getStatusLine().getStatusCode();
					log.info(">>>>>>>>>>>>>>>>>> statusCode: " + statusCode);

				    List<Cookie> cookies = cookieStore.getCookies();
				    for (Cookie c : cookies) {
				    	log.info(c.toString());
				    }

				} finally {
				    response.close();
				}

				/*
			     * Step 2. Load welcome.htm
			     */
				/*
				HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
			    HttpGet httpGet = new HttpGet("https://163.19.163.170:1443/welcome.htm");
			    httpGet.setConfig(requestConfig);

			    HttpResponse resp = client.execute(httpGet);

			    ResponseHandler<String> handler = new BasicResponseHandler();
			    String body = handler.handleResponse(resp);
			    System.out.println("******** body: " + body);
//			    return body;
 * */
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
	}

	@RequestMapping(value = "/welcomePage", method = RequestMethod.GET)
	public String welcomePage(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			/*
			String body = sendLogin(request);
			model.addAttribute("IFRAME_HTML", body);
			*/
			sendLogin(request);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/welcome";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String prtgIndex(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);

			sendLogin(request);
			model.addAttribute("IFRAME_URI", Env.PRTG_INDEX_URI);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/index";
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String prtgDashboard(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);
			model.addAttribute("IFRAME_URI", Env.PRTG_DASHBOARD_URI);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/dashboard";
	}
}
