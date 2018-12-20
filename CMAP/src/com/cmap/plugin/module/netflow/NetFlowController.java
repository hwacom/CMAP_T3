package com.cmap.plugin.module.netflow;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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

import com.cmap.DatatableResponse;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;
import com.cmap.exception.ServiceLayerException;
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
			tableTitleField.add("Group_Id");
			tableTitleField.addAll(dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_TARGET));

			if (tableTitleField != null && !tableTitleField.isEmpty()) {
				retVal = (orderColIdx < tableTitleField.size()) ? tableTitleField.get(orderColIdx) : retVal;
			}

		} catch (ServiceLayerException e) {
			log.error(e.toString(), e);
		}

		return retVal;
	}

	@RequestMapping(value = "getNetFlowData.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse getNetFlowData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="queryGroup", required=false, defaultValue="") String queryGroup,
			@RequestParam(name="queryIp", required=false, defaultValue="") String queryIp,
			@RequestParam(name="queryPort", required=false, defaultValue="") String queryPort,
			@RequestParam(name="queryMac", required=false, defaultValue="") String queryMac,
			@RequestParam(name="queryDateBegin", required=false, defaultValue="") String queryDateBegin,
			@RequestParam(name="queryDateEnd", required=false, defaultValue="") String queryDateEnd,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="10") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="2") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {

		long total = 0;
		long filterdTotal = 0;
		List<NetFlowVO> dataList = new ArrayList<>();
		NetFlowVO nfVO;
		try {
			if (StringUtils.isBlank(queryDateBegin)) {
				return new DatatableResponse(new Long(0), new ArrayList<NetFlowVO>(), new Long(0), "請選擇日期");
			}

			List<String> targetFieldList = new ArrayList<>();
			targetFieldList.add("Group_Id");
			targetFieldList.addAll(dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_TARGET));

			nfVO = new NetFlowVO();
			nfVO.setQueryGroupId(queryGroup);
			nfVO.setQueryIp(queryIp);
			nfVO.setQueryPort(queryPort);
			nfVO.setQueryMac(queryMac);
			nfVO.setQueryDateBegin(queryDateBegin);
			nfVO.setQueryDateEnd(queryDateEnd);
			nfVO.setStartNum(startNum);
			nfVO.setPageLength(pageLength);
			nfVO.setSearchValue(searchValue);
			nfVO.setOrderColumn(getOrderColumnName(orderColIdx));
			nfVO.setOrderDirection(orderDirection);

			filterdTotal = netFlowService.countNetFlowRecord(nfVO, targetFieldList);

			if (filterdTotal != 0) {
				dataList = netFlowService.findNetFlowRecord(nfVO, startNum, pageLength, targetFieldList);
			}

			NetFlowVO newVO = new NetFlowVO();
			newVO.setQueryDateBegin(queryDateBegin);
			total = netFlowService.countNetFlowRecord(newVO, targetFieldList);

		} catch (ServiceLayerException sle) {
		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return new DatatableResponse(total, dataList, filterdTotal);
	}
}
