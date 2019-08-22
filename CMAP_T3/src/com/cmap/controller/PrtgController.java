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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cmap.AppResponse;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.PrtgAccountMapping;
import com.cmap.security.SecurityUtil;
import com.cmap.service.CommonService;
import com.cmap.service.PrtgService;
import com.cmap.service.UserService;
import com.cmap.service.vo.PrtgServiceVO;
import com.cmap.utils.impl.CloseableHttpClientUtils;
import com.cmap.utils.impl.PrtgApiUtils;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/prtg")
public class PrtgController extends BaseController {
	@Log
	private static Logger log;

	@Autowired
	private CommonService commonService;

	@Autowired
	private PrtgService prtgService;

	@Autowired
	private UserService userService;

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

	/**
	 * 取得 PRTG passhash
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @param jsonData
	 * [範例]:
     *     {
              "groupId" : "019998",
              "role": [
                "教師",
                "資訊教師"
              ],
              "account" : "test1234"
            }
	 * @return
	 */
    @RequestMapping(value = "/getPasshash", method = RequestMethod.POST, produces="application/json")
    public @ResponseBody AppResponse getPasshash(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
            @RequestBody JsonNode jsonData) {

        try {
            String groupId = jsonData.findValue("groupId") != null ? jsonData.findValue("groupId").asText() : "";

            String[] roles;
            JsonNode roleArr = jsonData.get("role");
            if (roleArr.isArray()) {
                roles = new String[roleArr.size()];

                for (int i=0; i<roleArr.size(); i++) {
                    roles[i] = roleArr.get(i).asText();
                }
            } else {
                roles = new String[1];
                roles[0] = roleArr.asText();
            }

            String account = jsonData.findValue("account") != null ? jsonData.findValue("account").asText() : "";

            // Step 1. 判斷此 groupId + role + account 有無權限登入使用
            boolean canLogin = userService.checkUserCanAccess(request, true, groupId, roles, account);

            if (!canLogin) {
                throw new ServiceLayerException("使用者無登入權限");
            }

            // Step 2. 取得 groupId 對應的 PRTG 登入帳密
            PrtgAccountMapping mapping = prtgService.getMappingBySourceId(groupId);

            String username = mapping.getPrtgAccount();
            String password = mapping.getPrtgPassword();

            // Step 3. 呼叫 PRTG API 取得 passhash
            PrtgApiUtils prtgApiUtils = new PrtgApiUtils();
            String passhash = prtgApiUtils.getPasshash(username, password);

            AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "Success");
            app.putData(Constants.USERNAME, username);
            app.putData(Constants.PASSHASH, passhash);
            return app;

        } catch (ServiceLayerException sle) {
            log.error(sle.getMessage());

            AppResponse app = new AppResponse(HttpServletResponse.SC_EXPECTATION_FAILED, sle.getMessage());
            app.putData(Constants.USERNAME, null);
            app.putData(Constants.PASSHASH, null);
            return app;

        } catch (Exception e) {
            log.error(e.toString(), e);

            AppResponse app = new AppResponse(HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            app.putData(Constants.USERNAME, null);
            app.putData(Constants.PASSHASH, null);
            return app;
        }
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
			    indexUrl = Env.PRTG_DEFAULT_INDEX_URI;   //如果沒設定則取得預設MAP
            }

			indexUrl = composePrtgUrl(request, indexUrl);

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", indexUrl);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	/**
	 * 組合最終 API URL for 設定的URL內是否有加上參數
	 * @param request
	 * @param oriUrl
	 * @return
	 */
	private String composePrtgUrl(HttpServletRequest request, String oriUrl) {
	    String username = Objects.toString(request.getSession().getAttribute(Constants.PRTG_LOGIN_ACCOUNT), null);
	    String password = Objects.toString(request.getSession().getAttribute(Constants.PRTG_LOGIN_PASSWORD), null);
	    String passhash = Objects.toString(request.getSession().getAttribute(Constants.PASSHASH), null);
	    oriUrl = StringUtils.replace(oriUrl, "{username}", username);
	    oriUrl = StringUtils.replace(oriUrl, "{password}", password);
	    oriUrl = StringUtils.replace(oriUrl, "{passhash}", passhash);
	    return oriUrl;
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

			dashboardMapUrl = composePrtgUrl(request, dashboardMapUrl);

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", dashboardMapUrl);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	@RequestMapping(value = "getPrtgTopographyUri", method = RequestMethod.POST)
    public @ResponseBody AppResponse getPrtgTopographyUri(
            Model model, HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        try {
            final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);

            String topographyMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_TOPOGRAPHY);

            if (StringUtils.isBlank(topographyMapUrl)) {
                topographyMapUrl = Env.PRTG_DEFAULT_TOPOGRAPHY_URI;   //如果沒設定則取得預設MAP
            }

            topographyMapUrl = composePrtgUrl(request, topographyMapUrl);

            AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
            app.putData("uri", topographyMapUrl);
            return app;

        } catch (Exception e) {
            log.error(e.toString(), e);
            return new AppResponse(super.getLineNumber(), e.getMessage());

        } finally {
        }
    }

	@RequestMapping(value = "getPrtgAlarmSummaryUri", method = RequestMethod.POST)
    public @ResponseBody AppResponse getPrtgAlarmSummaryUri(
            Model model, HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        try {
            final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);

            String alarmSummaryMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_ALARM_SUMMARY);

            if (StringUtils.isBlank(alarmSummaryMapUrl)) {
                alarmSummaryMapUrl = Env.PRTG_DEFAULT_ALARM_SUMMARY_URI;   //如果沒設定則取得預設MAP
            }

            alarmSummaryMapUrl = composePrtgUrl(request, alarmSummaryMapUrl);

            AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
            app.putData("uri", alarmSummaryMapUrl);
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

			netFlowSummaryMapUrl = composePrtgUrl(request, netFlowSummaryMapUrl);

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", netFlowSummaryMapUrl);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	@RequestMapping(value = "getPrtgNetFlowOutputUri", method = RequestMethod.POST)
    public @ResponseBody AppResponse getPrtgNetFlowOutputUri(
            Model model, HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        try {
            final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);

            String netFlowOutputMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_NET_FLOW_OUTPUT);

            if (StringUtils.isBlank(netFlowOutputMapUrl)) {
                netFlowOutputMapUrl = Env.PRTG_DEFAULT_NET_FLOW_OUTPUT_URI;   //如果沒設定則取得預設MAP
            }

            netFlowOutputMapUrl = composePrtgUrl(request, netFlowOutputMapUrl);

            AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
            app.putData("uri", netFlowOutputMapUrl);
            return app;

        } catch (Exception e) {
            log.error(e.toString(), e);
            return new AppResponse(super.getLineNumber(), e.getMessage());

        } finally {
        }
    }

	@RequestMapping(value = "getPrtgNetFlowOutputCoreUri", method = RequestMethod.POST)
    public @ResponseBody AppResponse getPrtgNetFlowOutputCoreUri(
            Model model, HttpServletRequest request, HttpServletResponse response) {

        try {
            String netFlowOutputMapCoreUrl = Env.PRTG_DEFAULT_NET_FLOW_OUTPUT_URI;   //如果沒設定則取得預設MAP
            netFlowOutputMapCoreUrl = composePrtgUrl(request, netFlowOutputMapCoreUrl);

            AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
            app.putData("uri", netFlowOutputMapCoreUrl);
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
			final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);
			//final String schoolId = "054649";

			String deviceFailureMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_DEVICE_FAILURE);

			if (StringUtils.isBlank(deviceFailureMapUrl)) {
				deviceFailureMapUrl = Env.PRTG_DEFAULT_DEVICE_FAILURE_URI;	//如果沒設定則取得預設MAP
			}

			deviceFailureMapUrl = composePrtgUrl(request, deviceFailureMapUrl);

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
			final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);
			//final String schoolId = "054649";

			String abnormalTrafficMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_ABNORMAL_TRAFFIC);

			if (StringUtils.isBlank(abnormalTrafficMapUrl)) {
				abnormalTrafficMapUrl = Env.PRTG_DEFAULT_ABNORMAL_TRAFFIC_URI;	//如果沒設定則取得預設MAP
			}

			abnormalTrafficMapUrl = composePrtgUrl(request, abnormalTrafficMapUrl);

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
			app.putData("uri", abnormalTrafficMapUrl);
			return app;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
		}
	}

	@RequestMapping(value = "getEmailUpdateUri", method = RequestMethod.POST)
    public @ResponseBody AppResponse getEmailUpdateUri(
            Model model, HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        try {
            //final String schoolId = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), null);
            final String schoolId = "054649";

            String emailUpdateMapUrl = prtgService.getMapUrlBySourceIdAndType(schoolId, Constants.MAP_URL_OF_EMAIL_UPDATE);

            if (StringUtils.isBlank(emailUpdateMapUrl)) {
                emailUpdateMapUrl = Env.PRTG_DEFAULT_EMAIL_UPDATE_URI;  //如果沒設定則取得預設MAP
            }

            emailUpdateMapUrl = composePrtgUrl(request, emailUpdateMapUrl);

            AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "success");
            app.putData("uri", emailUpdateMapUrl);
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

	@RequestMapping(value = "/topography", method = RequestMethod.GET)
    public String prtgTopography(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            init(model);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return "prtg/topography";
    }

	@RequestMapping(value = "/alarmSummary", method = RequestMethod.GET)
    public String prtgAlarmSummary(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            init(model);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return "prtg/alarm_summary";
    }

	@RequestMapping(value = "/netFlowSummary", method = RequestMethod.GET)
	public String prtgNetFlowSummary(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/net_flow_summary";
	}

	@RequestMapping(value = "/netFlowOutput", method = RequestMethod.GET)
    public String prtgNetFlowOutput(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            init(model);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return "prtg/net_flow_output";
    }

	@RequestMapping(value = "/netFlowOutput/core", method = RequestMethod.GET)
    public String prtgNetFlowOutputCore(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            init(model);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return "prtg/net_flow_output_core";
    }

	@RequestMapping(value = "/deviceFailure", method = RequestMethod.GET)
	public String prtgDeviceFailure(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/device_failure";
	}

	@RequestMapping(value = "/abnormalTraffic", method = RequestMethod.GET)
	public String prtgAbnormalTraffic(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			init(model);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return "prtg/abnormal_traffic";
	}

	@RequestMapping(value = "/email/update", method = RequestMethod.GET)
    public String prtgEmailUpdate(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            init(model);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return "prtg/email_update";
    }
}
