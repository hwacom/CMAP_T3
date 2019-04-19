package com.cmap.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import com.cmap.AppResponse;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.security.SecurityUtil;
import com.cmap.service.CommonService;
import com.cmap.service.PrtgService;
import com.cmap.service.vo.PrtgServiceVO;
import com.cmap.utils.impl.CloseableHttpClientUtils;

@Controller
@RequestMapping("/prtg")
public class PrtgController extends BaseController {
	@Log
	private static Logger log;

	@Autowired
	private CommonService commonService;

	@Autowired
	private PrtgService prtgService;

	private void init(Model model) {
		model.addAttribute("PRTG_IP_ADDR", Env.PRTG_SERVER_IP);
		model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
	}

	private String sendLogin(HttpServletRequest request, HttpServletResponse response) {
		String retVal = "";
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
				CloseableHttpResponse closeableResponse = httpclient.execute(httpPost, context);

				CookieStore cookieStore = context.getCookieStore();
				try {
					int statusCode = closeableResponse.getStatusLine().getStatusCode();
					log.info(">>>>>>>>>>>>>>>>>> statusCode: " + statusCode);

					javax.servlet.http.Cookie httpCookie;
				    List<Cookie> cookies = cookieStore.getCookies();
				    for (Cookie c : cookies) {
				    	log.info(c.toString());

				    	System.out.println("Name: " + c.getName() + ", Value: " + c.getValue());
				    	httpCookie = new javax.servlet.http.Cookie(c.getName(), c.getValue());
				    	response.addCookie(httpCookie);
				    }

				} finally {
					closeableResponse.close();
				}

				/*
			     * Step 2. Load welcome.htm
			     */
				/*
				HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
			    HttpGet httpGet = new HttpGet("https://www.google.com/");
			    httpGet.setConfig(requestConfig);

			    HttpResponse resp = client.execute(httpGet);

			    ResponseHandler<String> handler = new BasicResponseHandler();
			    String body = handler.handleResponse(resp);
			    System.out.println("******** body: " + body);
			    return body;
			    */
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return retVal;
	}

	@RequestMapping(value = "/welcomePage", method = RequestMethod.GET)
	public String welcomePage(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			/*
			String body = sendLogin(request);
			model.addAttribute("IFRAME_HTML", body);
			*/
			/*
			String cookie = sendLogin(request, response);
			cookie = cookie.replace("[", "").replace(":", "=").replace("]", ";").replace(" ", "");
			model.addAttribute("IFRAME_COOKIE", cookie);

			String new_url = "https://163.19.163.170:1443/welcome.htm";
			String html = "<script type='text/javascript'>location.href='"+new_url+"';</script>";
			response.getWriter().print(html);
			*/
			String html = sendLogin(request, response);
//			cookie = cookie.replace("[", "").replace(":", "=").replace("]", ";").replace(" ", "");
//			model.addAttribute("IFRAME_COOKIE", cookie);
//			model.addAttribute("IFRAME_URI", Env.PRTG_INDEX_URI);
			model.addAttribute("IFRAME_HTML", html);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/welcome";
	}

	@RequestMapping(value = "getLoginUri", method = RequestMethod.POST)
	public @ResponseBody AppResponse getLoginUri(
			Model model, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		try {
			final String BASE_URI = Env.PRTG_LOGIN_URI;
			final String PRTG_ACCOUNT = Objects.toString(session.getAttribute(Constants.PRTG_LOGIN_ACCOUNT), "");
			final String PRTG_PASSWORD = Objects.toString(session.getAttribute(Constants.PRTG_LOGIN_PASSWORD), "");
			final String prtgIndexUri = BASE_URI + "?a=" + PRTG_ACCOUNT + "&p=" + PRTG_PASSWORD;

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", prtgIndexUri);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	@RequestMapping(value = "getPrtgIndexUri", method = RequestMethod.POST)
	public @ResponseBody AppResponse getPrtgIndexUri(
			Model model, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		try {
			final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);

			String indexUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_INDEX);

			if (StringUtils.isBlank(indexUrl)) {
				indexUrl = Env.PRTG_DEFAULT_INDEX_URI;	//如果沒設定則取得預設MAP
			}

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", indexUrl);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	@RequestMapping(value = "getPrtgDashboardUri", method = RequestMethod.POST)
	public @ResponseBody AppResponse getPrtgDashboardUri(
			Model model, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		try {
			final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);

			String dashboardMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_DASHBOARD);

			if (StringUtils.isBlank(dashboardMapUrl)) {
				dashboardMapUrl = Env.PRTG_DEFAULT_DASHBOARD_URI;	//如果沒設定則取得預設MAP
			}

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", dashboardMapUrl);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	@RequestMapping(value = "getPrtgNetFlowSummaryUri", method = RequestMethod.POST)
	public @ResponseBody AppResponse getPrtgNetFlowSummaryUri(
			Model model, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		try {
			final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);

			String netFlowSummaryMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_NET_FLOW_SUMMARY);

			if (StringUtils.isBlank(netFlowSummaryMapUrl)) {
				netFlowSummaryMapUrl = Env.PRTG_DEFAULT_NET_FLOW_SUMMARY_URI;	//如果沒設定則取得預設MAP
			}

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", netFlowSummaryMapUrl);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	@RequestMapping(value = "getPrtgDeviceFailureUri", method = RequestMethod.POST)
	public @ResponseBody AppResponse getPrtgDeviceFailureUri(
			Model model, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		try {
			//final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);
			final String schoolId = "054649";

			String deviceFailureMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_DEVICE_FAILURE);

			if (StringUtils.isBlank(deviceFailureMapUrl)) {
				deviceFailureMapUrl = Env.PRTG_DEFAULT_DEVICE_FAILURE_URI;	//如果沒設定則取得預設MAP
			}

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", deviceFailureMapUrl);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	@RequestMapping(value = "getPrtgAbnormalTrafficUri", method = RequestMethod.POST)
	public @ResponseBody AppResponse getPrtgAbnormalTrafficUri(
			Model model, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		try {
			//final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);
			final String schoolId = "054649";

			String abnormalTrafficMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_ABNORMAL_TRAFFIC);

			if (StringUtils.isBlank(abnormalTrafficMapUrl)) {
				abnormalTrafficMapUrl = Env.PRTG_DEFAULT_ABNORMAL_TRAFFIC_URI;	//如果沒設定則取得預設MAP
			}

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", abnormalTrafficMapUrl);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	@RequestMapping(value = "/index/login", method = RequestMethod.GET)
	public String prtgIndexAndLogin(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);
			model.addAttribute("DO_LOGIN", "Y");

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String prtgIndex(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/index";
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String prtgDashboard(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/dashboard";
	}

	@RequestMapping(value = "/netFlowSummary", method = RequestMethod.GET)
	public String netFlowSummary(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/net_flow_summary";
	}

	@RequestMapping(value = "/deviceFailure", method = RequestMethod.GET)
	public String deviceFailure(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/device_failure";
	}

	@RequestMapping(value = "/performance", method = RequestMethod.GET)
    public String performance(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            init(model);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return "prtg/performance";
    }

	@RequestMapping(value = "/report", method = RequestMethod.GET)
    public String report(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            init(model);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return "prtg/report";
    }

	@RequestMapping(value = "/abnormalTraffic", method = RequestMethod.GET)
	public String abnormalTraffic(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/abnormal_traffic";
	}
}
