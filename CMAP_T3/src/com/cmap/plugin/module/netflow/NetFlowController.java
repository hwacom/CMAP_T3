package com.cmap.plugin.module.netflow;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cmap.AppResponse;
import com.cmap.Constants;
import com.cmap.DatatableResponse;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;
import com.cmap.exception.ServiceLayerException;
import com.cmap.i18n.DatabaseMessageSourceBase;
import com.cmap.security.SecurityUtil;
import com.cmap.service.DataPollerService;

@Controller
@RequestMapping("/plugin/module/netFlow")
public class NetFlowController extends BaseController {
	@Log
	private static Logger log;

	@Autowired
	private DataPollerService dataPollerService;

	@Autowired
	private NetFlowService netFlowService;

	@Autowired
	private DatabaseMessageSourceBase messageSource;

	/**
	 * 初始化選單
	 * @param model
	 * @param request
	 */
	private void initMenu(Model model, HttpServletRequest request) {
		Map<String, String> groupListMap = null;
		try {
			groupListMap = getGroupList(request);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			model.addAttribute("queryGroup", "");
			model.addAttribute("groupList", groupListMap);

			model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
			model.addAttribute("timeout", Env.TIMEOUT_4_NET_FLOW_QUERY);

			model.addAttribute("pageLength", Env.NET_FLOW_PAGE_LENGTH);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String netFlow(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<String> tableTitleField = dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_TARGET);
			model.addAttribute("TABLE_FIELD", tableTitleField);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}
		return "plugin/module_net_flow";
	}

	@RequestMapping(value = "run", method = RequestMethod.GET)
	public String netFlowRun(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

		try {
			dataPollerService.executePolling(Env.SETTING_ID_OF_NET_FLOW);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}

		return "plugin/module_net_flow";
	}

	private String getOrderColumnName(Integer orderColIdx) {
		String retVal = "Group_Id";
		try {
			List<String> tableTitleField = new ArrayList<>();
			tableTitleField.add("");
//			tableTitleField.add("Group_Id");
			tableTitleField.addAll(dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_TARGET));

			List<String> newTableTitleField = new ArrayList<>();
			for (String columnName : tableTitleField) {
			    String newName = null;
			    switch (columnName) {
			        case "Now":
			            newName = "Now_Time";
			            break;
			        case "From_Date_Time":
			            newName = "From_Time";
                        break;
			        case "To_Date_Time":
			            newName = "To_Time";
                        break;
                    default:
                        newName = columnName;
                        break;
			    }
			    newTableTitleField.add(newName);
			}
			if (newTableTitleField != null && !newTableTitleField.isEmpty()) {
				retVal = (orderColIdx < newTableTitleField.size()) ? newTableTitleField.get(orderColIdx) : retVal;
			}

		} catch (ServiceLayerException e) {
			log.error(e.toString(), e);
		}

		return retVal;
	}

	/**
	 * 取得查詢條件下總流量
	 * @param model
	 * @param request
	 * @param response
	 * @param queryGroup
	 * @param querySourceIp
	 * @param queryDestinationIp
	 * @param querySenderIp
	 * @param querySourcePort
	 * @param queryDestinationPort
	 * @param queryMac
	 * @param queryDateBegin
	 * @param queryDateEnd
	 * @param queryTimeBegin
	 * @param queryTimeEnd
	 * @param searchValue
	 * @return
	 */
	@RequestMapping(value = "getTotalTraffic.json", method = RequestMethod.POST)
    public @ResponseBody AppResponse getTotalTraffic(
            Model model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(name="queryGroup", required=true, defaultValue="") String queryGroup,
            @RequestParam(name="querySourceIp", required=false, defaultValue="") String querySourceIp,
            @RequestParam(name="queryDestinationIp", required=false, defaultValue="") String queryDestinationIp,
            @RequestParam(name="querySenderIp", required=false, defaultValue="") String querySenderIp,
            @RequestParam(name="querySourcePort", required=false, defaultValue="") String querySourcePort,
            @RequestParam(name="queryDestinationPort", required=false, defaultValue="") String queryDestinationPort,
            @RequestParam(name="queryMac", required=false, defaultValue="") String queryMac,
            @RequestParam(name="queryDateBegin", required=true, defaultValue="") String queryDateBegin,
            @RequestParam(name="queryDateEnd", required=false, defaultValue="") String queryDateEnd,
            @RequestParam(name="queryTimeBegin", required=true, defaultValue="") String queryTimeBegin,
            @RequestParam(name="queryTimeEnd", required=false, defaultValue="") String queryTimeEnd,
            @RequestParam(name="searchValue", required=false, defaultValue="") String searchValue) {

	    String retVal = "N/A";
	    NetFlowVO nfVO;
	    try {
	        List<String> targetFieldList = new ArrayList<>();
            targetFieldList.add("Group_Id");
            targetFieldList.addAll(dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_TARGET));

            nfVO = new NetFlowVO();
            nfVO.setQueryGroupId(queryGroup);
            nfVO.setQuerySourceIp(querySourceIp);
            nfVO.setQuerySourcePort(querySourcePort);
            nfVO.setQueryDestinationIp(queryDestinationIp);
            nfVO.setQueryDestinationPort(queryDestinationPort);
            nfVO.setQuerySenderIp(querySenderIp);
            nfVO.setQueryDateBegin(queryDateBegin);
            nfVO.setQueryTimeBegin(queryTimeBegin);
            nfVO.setQueryTimeEnd(queryTimeEnd);
            nfVO.setQueryDateStr(queryDateBegin.replace("-", ""));
            nfVO.setQueryTimeBeginStr(queryTimeBegin.replace(":", ""));
            nfVO.setQueryTimeEndStr(queryTimeEnd.replace(":", ""));
            nfVO.setSearchValue(searchValue);

            String storeMethod = dataPollerService.getStoreMethodByDataType(Constants.DATA_TYPE_OF_NET_FLOW);

            if (StringUtils.equals(storeMethod, Constants.STORE_METHOD_OF_FILE)) {
                /*
                 * Option 1. 走 FILE 模式查詢
                 */

            } else if (StringUtils.equals(storeMethod, Constants.STORE_METHOD_OF_DB)) {
                /*
                 * Option 2. 走 DB 模式查詢
                 */
                retVal = netFlowService.getTotalTraffic(nfVO, targetFieldList);
            }

            AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "SUCCESS");
            app.putData(Constants.APP_DATA_KEY_TTL_TRAFFIC, retVal);
            return app;

	    } catch (Exception e) {
	        log.error(e.toString(), e);

	        AppResponse app = new AppResponse(HttpServletResponse.SC_NOT_ACCEPTABLE, "ERROR");
            app.putData(Constants.APP_DATA_KEY_TTL_TRAFFIC, retVal);
            return app;
	    }
	}

	/**
	 * 取得查詢條件下總筆數
	 * @param model
	 * @param request
	 * @param response
	 * @param queryGroup
	 * @param querySourceIp
	 * @param queryDestinationIp
	 * @param querySenderIp
	 * @param querySourcePort
	 * @param queryDestinationPort
	 * @param queryMac
	 * @param queryDateBegin
	 * @param queryDateEnd
	 * @param queryTimeBegin
	 * @param queryTimeEnd
	 * @param searchValue
	 * @return
	 */
	@RequestMapping(value = "getTotalFilteredCount.json", method = RequestMethod.POST)
    public @ResponseBody AppResponse getTotalFilteredCount(
            Model model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(name="queryGroup", required=true, defaultValue="") String queryGroup,
            @RequestParam(name="querySourceIp", required=false, defaultValue="") String querySourceIp,
            @RequestParam(name="queryDestinationIp", required=false, defaultValue="") String queryDestinationIp,
            @RequestParam(name="querySenderIp", required=false, defaultValue="") String querySenderIp,
            @RequestParam(name="querySourcePort", required=false, defaultValue="") String querySourcePort,
            @RequestParam(name="queryDestinationPort", required=false, defaultValue="") String queryDestinationPort,
            @RequestParam(name="queryMac", required=false, defaultValue="") String queryMac,
            @RequestParam(name="queryDateBegin", required=true, defaultValue="") String queryDateBegin,
            @RequestParam(name="queryDateEnd", required=false, defaultValue="") String queryDateEnd,
            @RequestParam(name="queryTimeBegin", required=true, defaultValue="") String queryTimeBegin,
            @RequestParam(name="queryTimeEnd", required=false, defaultValue="") String queryTimeEnd,
            @RequestParam(name="searchValue", required=false, defaultValue="") String searchValue) {

	    String retVal = "N/A";
	    long filteredTotal = 0;
        NetFlowVO nfVO;
        try {
            List<String> targetFieldList = new ArrayList<>();
            targetFieldList.add("Group_Id");
            targetFieldList.addAll(dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_TARGET));

            nfVO = new NetFlowVO();
            nfVO.setQueryGroupId(queryGroup);
            nfVO.setQuerySourceIp(querySourceIp);
            nfVO.setQuerySourcePort(querySourcePort);
            nfVO.setQueryDestinationIp(queryDestinationIp);
            nfVO.setQueryDestinationPort(queryDestinationPort);
            nfVO.setQuerySenderIp(querySenderIp);
            nfVO.setQueryDateBegin(queryDateBegin);
            nfVO.setQueryTimeBegin(queryTimeBegin);
            nfVO.setQueryTimeEnd(queryTimeEnd);
            nfVO.setQueryDateStr(queryDateBegin.replace("-", ""));
            nfVO.setQueryTimeBeginStr(queryTimeBegin.replace(":", ""));
            nfVO.setQueryTimeEndStr(queryTimeEnd.replace(":", ""));
            nfVO.setSearchValue(searchValue);

            String storeMethod = dataPollerService.getStoreMethodByDataType(Constants.DATA_TYPE_OF_NET_FLOW);

            if (StringUtils.equals(storeMethod, Constants.STORE_METHOD_OF_FILE)) {
                /*
                 * Option 1. 走 FILE 模式查詢
                 */

            } else if (StringUtils.equals(storeMethod, Constants.STORE_METHOD_OF_DB)) {
                /*
                 * Option 2. 走 DB 模式查詢
                 */
                filteredTotal = netFlowService.countNetFlowRecordFromDB(nfVO, targetFieldList);
                retVal = Constants.NUMBER_FORMAT_THOUSAND_SIGN.format(filteredTotal);
            }

            AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "SUCCESS");
            app.putData(Constants.APP_DATA_KEY_FILTERED_COUNT, retVal);
            return app;

        } catch (Exception e) {
            log.error(e.toString(), e);

            AppResponse app = new AppResponse(HttpServletResponse.SC_NOT_ACCEPTABLE, "ERROR");
            app.putData(Constants.APP_DATA_KEY_FILTERED_COUNT, retVal);
            return app;
        }
    }

	@RequestMapping(value = "getNetFlowData.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse getNetFlowData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="queryGroup", required=true, defaultValue="") String queryGroup,
			@RequestParam(name="querySourceIp", required=false, defaultValue="") String querySourceIp,
			@RequestParam(name="queryDestinationIp", required=false, defaultValue="") String queryDestinationIp,
			@RequestParam(name="querySenderIp", required=false, defaultValue="") String querySenderIp,
			@RequestParam(name="querySourcePort", required=false, defaultValue="") String querySourcePort,
			@RequestParam(name="queryDestinationPort", required=false, defaultValue="") String queryDestinationPort,
			@RequestParam(name="queryMac", required=false, defaultValue="") String queryMac,
			@RequestParam(name="queryDateBegin", required=true, defaultValue="") String queryDateBegin,
			@RequestParam(name="queryDateEnd", required=false, defaultValue="") String queryDateEnd,
			@RequestParam(name="queryTimeBegin", required=true, defaultValue="") String queryTimeBegin,
			@RequestParam(name="queryTimeEnd", required=false, defaultValue="") String queryTimeEnd,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="100") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="2") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {

		long total = 0;
		long filteredTotal = 0;
		String totalFlow = "";
		List<NetFlowVO> dataList = new ArrayList<>();
		NetFlowVO nfVO;
		try {
			if (StringUtils.isBlank(queryGroup)) {
				String msg = messageSource.getMessage("please.choose", Locale.TAIWAN, null) + messageSource.getMessage("group.name", Locale.TAIWAN, null);
				return new DatatableResponse(new Long(0), new ArrayList<NetFlowVO>(), new Long(0), msg);
			}
			if (StringUtils.isBlank(queryDateBegin)) {
				String msg = messageSource.getMessage("please.choose", Locale.TAIWAN, null) + messageSource.getMessage("date", Locale.TAIWAN, null);
				return new DatatableResponse(new Long(0), new ArrayList<NetFlowVO>(), new Long(0), msg);
			}
			if (StringUtils.isBlank(queryTimeBegin) || StringUtils.isBlank(queryTimeEnd)) {
				String msg = messageSource.getMessage("please.choose", Locale.TAIWAN, null) + messageSource.getMessage("time", Locale.TAIWAN, null);
				return new DatatableResponse(new Long(0), new ArrayList<NetFlowVO>(), new Long(0), msg);
			}

			List<String> targetFieldList = new ArrayList<>();
			targetFieldList.add("Group_Id");
			targetFieldList.addAll(dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_TARGET));

			nfVO = new NetFlowVO();
			nfVO.setQueryGroupId(queryGroup);
			nfVO.setQuerySourceIp(querySourceIp);
			nfVO.setQuerySourcePort(querySourcePort);
			nfVO.setQueryDestinationIp(queryDestinationIp);
			nfVO.setQueryDestinationPort(queryDestinationPort);
			nfVO.setQuerySenderIp(querySenderIp);
			nfVO.setQueryDateBegin(queryDateBegin);
			nfVO.setQueryTimeBegin(queryTimeBegin);
			nfVO.setQueryTimeEnd(queryTimeEnd);
			nfVO.setQueryDateStr(queryDateBegin.replace("-", ""));
			nfVO.setQueryTimeBeginStr(queryTimeBegin.replace(":", ""));
			nfVO.setQueryTimeEndStr(queryTimeEnd.replace(":", ""));
			nfVO.setStartNum(startNum);
			nfVO.setPageLength(pageLength);
			nfVO.setSearchValue(searchValue);
			//nfVO.setOrderColumn(getOrderColumnName(orderColIdx)); //效能issue，暫定限制僅能用From_Date_Time排序
			nfVO.setOrderColumn("From_Time");
			nfVO.setOrderDirection(orderDirection);

			String storeMethod = dataPollerService.getStoreMethodByDataType(Constants.DATA_TYPE_OF_NET_FLOW);

			if (StringUtils.equals(storeMethod, Constants.STORE_METHOD_OF_FILE)) {
				/*
				 * Option 1. 走 FILE 模式查詢
				 */
				NetFlowVO retVO = netFlowService.findNetFlowRecordFromFile(nfVO, startNum, pageLength);

				filteredTotal = retVO.getMatchedList().size();
				dataList = retVO.getMatchedList();
				total = retVO.getTotalCount();

			} else if (StringUtils.equals(storeMethod, Constants.STORE_METHOD_OF_DB)) {
				/*
				 * Option 2. 走 DB 模式查詢
				 */
			    dataList = netFlowService.findNetFlowRecordFromDB(nfVO, startNum, pageLength, targetFieldList);

			    /*
			     * Y190729, 總筆數、總流量透過另外兩個AJAX分別查詢，提升查詢效率
				filteredTotal = netFlowService.countNetFlowRecordFromDB(nfVO, targetFieldList);

				if (filteredTotal != 0) {
					totalFlow = dataList.get(0).getTotalFlow();	// 總流量記在第一筆資料VO內
				}

				NetFlowVO newVO = new NetFlowVO();
				newVO.setQueryGroupId(queryGroup);
				total = netFlowService.countNetFlowRecordFromDB(newVO, targetFieldList);
				*/
			}

		} catch (ServiceLayerException sle) {
		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return new DatatableResponse(total, dataList, filteredTotal, null, totalFlow);
	}
}
