package com.cmap.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.cmap.exception.ServiceLayerException;
import com.cmap.plugin.module.ip.blocked.record.IpBlockedRecordService;
import com.cmap.plugin.module.ip.blocked.record.IpBlockedRecordVO;
import com.cmap.security.SecurityUtil;
import com.cmap.service.DeliveryService;
import com.cmap.service.vo.DeliveryParameterVO;
import com.cmap.service.vo.DeliveryServiceVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Controller
@RequestMapping("/delivery")
public class DeliveryController extends BaseController {
	@Log
	private static Logger log;

	private static final String[] UI_SEARCH_BY_SCRIPT_COLUMNS = new String[] {"","","scriptName","scriptType.scriptTypeName","systemVersion","","","",""};
	private static final String[] UI_RECORD_COLUMNS = new String[] {"","plm.begin_time","plm.create_by","dl.group_name","dl.device_name","dl.device_model","si.script_name","plm.remark","pls.result"};
	private static final String[] UI_BLOCKED_IP_RECORD_COLUMNS = new String[] {"","","ipAddress","blockTime","blockReason","blockBy"};

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private IpBlockedRecordService ipRecordService;

	private Map<String, String> groupListMap = null;
	private Map<String, String> deviceListMap = null;
	private Map<String, String> scriptTypeMap = null;

	private void initMenu(Model model, HttpServletRequest request) {
		try {
			groupListMap = getGroupList(request);
			scriptTypeMap = getScriptTypeList(Constants.DEFAULT_FLAG_N);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			model.addAttribute("group", "");
			model.addAttribute("groupList", groupListMap);

			model.addAttribute("device", "");
			model.addAttribute("deviceList", deviceListMap);

			model.addAttribute("scriptType", "");
			model.addAttribute("scriptTypeList", scriptTypeMap);

			model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String main(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}

		return "delivery/delivery_main";
	}

	@RequestMapping(value = "switchPort", method = RequestMethod.GET)
	public String switchPort(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}

		return "plugin/module_switch_port";
	}

	@RequestMapping(value = "ipOpenBlock", method = RequestMethod.GET)
	public String ipOpenBlock(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}

		return "plugin/module_ip_open_block";
	}

	/**
	 * 查找被封鎖過的IP紀錄
	 * @param model
	 * @param request
	 * @param response
	 * @param groupId
	 * @param ipAddress
	 * @param startNum
	 * @param pageLength
	 * @param searchValue
	 * @param orderColIdx
	 * @param orderDirection
	 * @return
	 */
	@RequestMapping(value = "getBlockedIpData.json", method = RequestMethod.POST)
    public @ResponseBody DatatableResponse getBlockedIpData(
            Model model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(name="queryGroupId", required=false, defaultValue="") String queryGroupId,
            @RequestParam(name="queryDeviceId", required=false, defaultValue="") String queryDeviceId,
            @RequestParam(name="queryIpAddress", required=false, defaultValue="") String queryIpAddress,
            @RequestParam(name="queryStatusFlag", required=false, defaultValue="") String queryStatusFlag,
            @RequestParam(name="queryBeginDate", required=false, defaultValue="") String queryBeginDate,
            @RequestParam(name="queryEndDate", required=false, defaultValue="") String queryEndDate,
            @RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
            @RequestParam(name="length", required=false, defaultValue="10") Integer pageLength,
            @RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
            @RequestParam(name="order[0][column]", required=false, defaultValue="6") Integer orderColIdx,
            @RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {

        long total = 0;
        long filterdTotal = 0;
        List<IpBlockedRecordVO> dataList = new ArrayList<>();
        IpBlockedRecordVO irVO;
        try {
            irVO = new IpBlockedRecordVO();
            irVO.setQueryGroupId(queryGroupId);
            irVO.setQueryDeviceId(queryDeviceId);
            irVO.setQueryIpAddress(queryIpAddress);
            irVO.setQueryStatusFlag(queryStatusFlag);
            irVO.setQueryBeginDate(queryBeginDate);
            irVO.setQueryEndDate(queryEndDate);
            irVO.setPageLength(pageLength);
            irVO.setSearchValue(searchValue);
            irVO.setOrderColumn(UI_BLOCKED_IP_RECORD_COLUMNS[orderColIdx]);  //TODO
            irVO.setOrderDirection(orderDirection);

            filterdTotal = ipRecordService.countModuleBlockedIpList(irVO);

            if (filterdTotal != 0) {
                dataList = ipRecordService.findModuleBlockedIpList(irVO, startNum, pageLength);
            }

            if (StringUtils.isBlank(queryDeviceId) && StringUtils.isBlank(queryIpAddress)
                    && StringUtils.isBlank(queryStatusFlag) && StringUtils.isBlank(queryBeginDate)
                    && StringUtils.isBlank(queryEndDate)) {
                //如果只有傳入GroupId條件，不需再另外查詢只有GroupId下的筆數 (即等於前面篩選的筆數)
                total = filterdTotal;

            } else {
                irVO = new IpBlockedRecordVO();
                irVO.setQueryGroupId(queryGroupId);
                total = ipRecordService.countModuleBlockedIpList(irVO);
            }

        } catch (ServiceLayerException sle) {
        } catch (Exception e) {

        } finally {
            //initMenu(model, request);
        }

        return new DatatableResponse(total, dataList, filterdTotal);
    }

	@RequestMapping(value = "macOpenBlock", method = RequestMethod.GET)
	public String macOpenBlock(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}

		return "plugin/module_mac_open_block";
	}

	@RequestMapping(value = "record", method = RequestMethod.GET)
	public String record(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}

		return "delivery/delivery_record";
	}

	@RequestMapping(value = "getDeviceListData.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse queryByDevice(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="10") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="6") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {

		long total = 0;
		long filterdTotal = 0;
		List<DeliveryServiceVO> dataList = new ArrayList<>();
		DeliveryServiceVO dsVO;
		try {
			dsVO = new DeliveryServiceVO();

		} catch (Exception e) {

		} finally {
			//initMenu(model, request);
		}

		return new DatatableResponse(total, dataList, filterdTotal);
	}

	@RequestMapping(value = "getScriptListData.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse queryByScript(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="queryScriptTypeCode", required=false, defaultValue="") String queryScriptTypeCode,
			@RequestParam(name="onlyOneScript", required=false, defaultValue="") String onlyOneScript,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="25") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="2") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="asc") String orderDirection) {

		long total = 0;
		long filterdTotal = 0;
		List<DeliveryServiceVO> dataList = new ArrayList<>();
		DeliveryServiceVO dsVO;
		try {
			dsVO = new DeliveryServiceVO();
			dsVO.setQueryScriptTypeCode(queryScriptTypeCode);
			dsVO.setStartNum(startNum);
			dsVO.setPageLength(pageLength);
			dsVO.setSearchValue(searchValue);
			dsVO.setOrderColumn(UI_SEARCH_BY_SCRIPT_COLUMNS[orderColIdx]);
			dsVO.setOrderDirection(orderDirection);

			switch (onlyOneScript) {
				case Constants.DELIVERY_ONLY_SCRIPT_OF_SWITCH_PORT:
					dsVO.setOnlySwitchPort(true);
					break;

				case Constants.DELIVERY_ONLY_SCRIPT_OF_IP_OPEN_BLOCK:
					dsVO.setOnlyIpOpenBlock(true);
					break;

				case Constants.DELIVERY_ONLY_SCRIPT_OF_MAC_OPEN_BLOCK:
					dsVO.setOnlyMacOpenBlock(true);
					break;
			}

			filterdTotal = deliveryService.countScriptList(dsVO);

			if (filterdTotal != 0) {
				dataList = deliveryService.findScriptList(dsVO, startNum, pageLength);
			}

			total = deliveryService.countScriptList(new DeliveryServiceVO());

		} catch (ServiceLayerException sle) {
		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return new DatatableResponse(total, dataList, filterdTotal);
	}

	private boolean logAccessRecord(HttpServletRequest request, DeliveryServiceVO logVO) {
		try {
			String ipAddr = getIp(request);
			String macAddr = getMac(ipAddr);
			logVO.setIpAddr(ipAddr);
			logVO.setMacAddr(macAddr);
			logVO.setActionBy(SecurityUtil.getSecurityUser().getUsername());
			logVO.setActionTime(new Date());
			deliveryService.logAccessRecord(logVO);
			return true;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return false;
		}
	}

	@RequestMapping(value = "getScriptInfo.json", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse getScriptInfo(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="scriptInfoId", required=true) String scriptInfoId) {

		DeliveryServiceVO dsVO;
		try {
			dsVO = deliveryService.getScriptInfoById(scriptInfoId);
			final String systemVersion = dsVO.getSystemVersion();

			//取得Group & Device選單內容
			Map<String, String> menuMap = getGroupDeviceMenu(request, null, systemVersion);
			dsVO.setGroupDeviceMenuJsonStr(new Gson().toJson(menuMap));

			ObjectMapper oMapper = new ObjectMapper();
			Map<String, Object> retMap = oMapper.convertValue(dsVO, Map.class);
			retMap.put("systemVersion", systemVersion);

			return new AppResponse(HttpServletResponse.SC_OK, "資料取得正常", retMap);

		} catch (ServiceLayerException sle) {
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");
		}
	}

	@RequestMapping(value = "getVariableSetting.json", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse getVariableSetting(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="groupArray", required=true) String groupArray,
			@RequestParam(name="deviceArray", required=true) String deviceArray,
			@RequestParam(name="varKeyArray", required=true) String varKeyArray) {

		DeliveryServiceVO dsVO;
		try {
			ObjectMapper oMapper = new ObjectMapper();
			List<String> groups = oMapper.readValue(groupArray, List.class);
			List<String> devices = oMapper.readValue(deviceArray, List.class);
			List<String> variables = oMapper.readValue(varKeyArray, List.class);

			dsVO = deliveryService.getVariableSetting(groups, devices, variables);

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "資料取得正常");
			app.putData("info", dsVO.getDeviceVarMap());
			app.putData("symbol", Env.COMM_SEPARATE_SYMBOL);

			return app;

		} catch (ServiceLayerException sle) {
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");
		}
	}

	@RequestMapping(value = "doDelivery.json", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse doDelivery(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="ps", required=true) String ps) {

		DeliveryServiceVO retVO;
		try {
			DeliveryParameterVO pVO = (DeliveryParameterVO)transJSON2Object(ps, DeliveryParameterVO.class);

			retVO = deliveryService.doDelivery(Env.CONNECTION_MODE_OF_DELIVERY, pVO, false, null, null, true);
			String retVal = retVO.getRetMsg();

			return new AppResponse(HttpServletResponse.SC_OK, retVal);

		} catch (ServiceLayerException sle) {
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, sle.getMessage());

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * 執行IP開通 by 「IP開通/封鎖」功能中的「解鎖」按鈕
	 * @param model
	 * @param request
	 * @param response
	 * @param listId
	 * @return
	 */
	@RequestMapping(value = "doIpOpenByBtn.json", method = RequestMethod.POST)
    public @ResponseBody AppResponse doIpOpenByBtn(Model model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(name="listId", required=true) String[] listId) {

        try {
            //TODO


            return new AppResponse(HttpServletResponse.SC_OK, null);

        /*
        } catch (ServiceLayerException sle) {
            return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, sle.getMessage());
        */
        } catch (Exception e) {
            log.error(e.toString(), e);
            return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

	@RequestMapping(value = "getDeliveryRecordData.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse getDeliveryRecordData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="queryGroup", required=false, defaultValue="") String queryGroup,
			@RequestParam(name="queryDevice", required=false, defaultValue="") String queryDevice,
			@RequestParam(name="queryDateBegin", required=false, defaultValue="") String queryDateBegin,
			@RequestParam(name="queryDateEnd", required=false, defaultValue="") String queryDateEnd,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="10") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="6") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {

		long total = 0;
		long filterdTotal = 0;
		List<DeliveryServiceVO> dataList = new ArrayList<>();
		DeliveryServiceVO dsVO;
		try {
			dsVO = new DeliveryServiceVO();
			dsVO.setQueryGroup(queryGroup);
			dsVO.setQueryDevice(queryDevice);
			dsVO.setQueryTimeBegin(queryDateBegin);
			dsVO.setQueryTimeEnd(queryDateEnd);
			dsVO.setStartNum(startNum);
			dsVO.setPageLength(pageLength);
			dsVO.setSearchValue(searchValue);
			dsVO.setOrderColumn(UI_RECORD_COLUMNS[orderColIdx]);
			dsVO.setOrderDirection(orderDirection);

			filterdTotal = deliveryService.countProvisionLog(dsVO);

			if (filterdTotal != 0) {
				dataList = deliveryService.findProvisionLog(dsVO, startNum, pageLength);
			}

			total = deliveryService.countProvisionLog(new DeliveryServiceVO());

		} catch (ServiceLayerException sle) {
		} catch (Exception e) {

		} finally {
			//initMenu(model, request);
		}

		return new DatatableResponse(total, dataList, filterdTotal);
	}

	@RequestMapping(value = "viewProvisionLog.json", method = RequestMethod.POST)
	public @ResponseBody AppResponse viewProvisionLog(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="logStepId", required=true) String logStepId) {
		try {
			DeliveryServiceVO dsVO = new DeliveryServiceVO();
			dsVO.setQueryLogStepId(logStepId);

			dsVO = deliveryService.getProvisionLogById(logStepId);

			AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "資料取得正常");
			app.putData("log", dsVO.getProvisionLog());

			return app;

		} catch (Exception e) {
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "查找供裝紀錄發生錯誤，請重新操作");

		} finally {
			//initMenu(model, request);
		}
	}
}
