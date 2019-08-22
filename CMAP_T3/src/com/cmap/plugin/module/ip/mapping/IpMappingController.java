package com.cmap.plugin.module.ip.mapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cmap.AppResponse;
import com.cmap.DatatableResponse;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;
import com.cmap.exception.ServiceLayerException;
import com.cmap.security.SecurityUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/plugin/module/ipMapping")
public class IpMappingController extends BaseController {
    @Log
    private static Logger log;

    private static final String[] UI_MAPPING_CHANGE_COLUMNS = new String[] {"","Create_Time","Group_Name","Device_Name","","Ip_Address","Mac_Address","Port"};

    @Autowired
    private IpMappingService ipMappingService;

    /**
     * 初始化選單
     * @param model
     * @param request
     */
    private void initMenu(Model model, HttpServletRequest request) {
        Map<String, String> groupListMap = null;
        Map<String, String> deviceListMap = null;
        try {
            groupListMap = getGroupList(request);

        } catch (Exception e) {
            log.error(e.toString(), e);

        } finally {
            model.addAttribute("queryGroup", "");
            model.addAttribute("groupList", groupListMap);
            model.addAttribute("queryDevice", "");
			model.addAttribute("deviceList", deviceListMap);

            model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
            model.addAttribute("timeout", Env.TIMEOUT_4_NET_FLOW_QUERY);
        }
    }

    /**
     * IP/MAC/Port mapping紀錄頁面
     * @param model
     * @param principal
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "record", method = RequestMethod.GET)
    public String record(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {


        } catch (Exception e) {
            log.error(e.toString(), e);

        } finally {
        }
        return "plugin/module_ip_mapping_record";
    }

    /**
     * IP/MAC/Port mapping異動紀錄頁面
     * @param model
     * @param principal
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "change", method = RequestMethod.GET)
    public String change(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {


        } catch (Exception e) {
            log.error(e.toString(), e);

        } finally {
            initMenu(model, request);
        }
        return "plugin/module_ip_mapping_change";
    }

    /**
     * 查找 IP/MAC/Port mapping異動紀錄
     * @param model
     * @param request
     * @param response
     * @param queryGroup
     * @param queryIp
     * @param queryMac
     * @param queryPort
     * @param queryDateBegin
     * @param queryDateEnd
     * @param startNum
     * @param pageLength
     * @param searchValue
     * @param orderColIdx
     * @param orderDirection
     * @return
     */
    @RequestMapping(value = "getChangeRecord.json", method = RequestMethod.POST)
    public @ResponseBody DatatableResponse getChangeRecord(
            Model model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(name="queryGroup", required=true, defaultValue="") String queryGroup,
            @RequestParam(name="queryDevice", required=false, defaultValue="") String queryDevice,
            @RequestParam(name="queryIp", required=false, defaultValue="") String queryIp,
            @RequestParam(name="queryMac", required=false, defaultValue="") String queryMac,
            @RequestParam(name="queryPort", required=false, defaultValue="") String queryPort,
            @RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
            @RequestParam(name="length", required=false, defaultValue="100") Integer pageLength,
            @RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
            @RequestParam(name="order[0][column]", required=false, defaultValue="2") Integer orderColIdx,
            @RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {

    	long total = 0;
		long filterdTotal = 0;
		List<IpMappingServiceVO> dataList = new ArrayList<>();
    	IpMappingServiceVO imsVO = null;
        try {
        	imsVO = new IpMappingServiceVO();
        	imsVO.setQueryGroup(queryGroup);
        	imsVO.setQueryDevice(queryDevice);
        	imsVO.setQueryIp(queryIp);
        	imsVO.setQueryMac(queryMac);
        	imsVO.setQueryPort(queryPort);
        	imsVO.setStartNum(startNum);
        	imsVO.setPageLength(pageLength);
        	imsVO.setSearchValue(searchValue);
        	imsVO.setOrderColumn(UI_MAPPING_CHANGE_COLUMNS[orderColIdx]);
        	imsVO.setOrderDirection(orderDirection);

        	filterdTotal = ipMappingService.countModuleIpMacPortMappingChange(imsVO);

			if (filterdTotal != 0) {
				dataList = ipMappingService.findModuleIpMacPortMappingChange(imsVO);
			}

			imsVO = new IpMappingServiceVO();
        	imsVO.setQueryGroup(queryGroup);

			total = ipMappingService.countModuleIpMacPortMappingChange(imsVO);

        } catch (ServiceLayerException sle) {
		} catch (Exception e) {
            log.error(e.toString(), e);
        }

        return new DatatableResponse(total, dataList, filterdTotal);
    }

    /**
     * 從 NET_FLOW 查詢功能點擊 SOURCE_IP or DESTINATION_IP 連結時，查找該筆 NET_FLOW 當下 IP 對應的 PORT 資料
     * @param model
     * @param request
     * @param response
     * @param groupId
     * @param dataId
     * @return
     */
    @RequestMapping(value = "getMappingRecordFromNetFlow.json", method = RequestMethod.POST)
    public @ResponseBody AppResponse getMappingRecordFromNetFlow(
            Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

        try {
        	String groupId = jsonData.findValues("groupId").get(0).asText();
        	String dataId = jsonData.findValues("dataId").get(0).asText();
        	String fromDateTime = jsonData.findValues("fromDateTime").get(0).asText();
        	String type = jsonData.findValues("type").get(0).asText();

        	IpMappingServiceVO imsVO = ipMappingService.findMappingDataFromNetFlow(groupId, dataId, fromDateTime, type);

        	Map<String, Object> retMap = new HashMap<>();
			retMap.put("groupName", imsVO.getGroupName());
			retMap.put("deviceName", imsVO.getDeviceName());
			retMap.put("deviceModel", imsVO.getDeviceModel());
			retMap.put("ipAddress", imsVO.getIpAddress());
			retMap.put("portName", imsVO.getPortName());
			retMap.put("showMsg", imsVO.getShowMsg());

			String ipAddress = imsVO.getIpAddress();
			String ipFromInfo = getIpFromInfo(ipAddress);

			if (StringUtils.isNotBlank(ipFromInfo)) {
			    ObjectMapper mapper = new ObjectMapper();
	            JsonNode ipJsonObj = mapper.readTree(ipFromInfo);

	            String country = ipJsonObj.findValue("country").asText();
	            String countryCode = ipJsonObj.findValue("countryCode").asText();
	            String city = ipJsonObj.findValue("city").asText();
	            String region = ipJsonObj.findValue("regionName").asText();

	            retMap.put("location", city + ", " + region + ", " + country + " (" + countryCode + ")");
	            retMap.put("countryCode", StringUtils.lowerCase(countryCode));

			} else {
			    // 當API網站發生問題時，改提供網站連結
			    retMap.put("countryQueryURL", Env.GET_IP_FROM_INFO_WEB_SITE_URL + ipAddress);
			}

        	return new AppResponse(HttpServletResponse.SC_OK, "資料取得正常", retMap);

        } catch (ServiceLayerException sle) {
        	return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");
        } catch (Exception e) {
            log.error(e.toString(), e);
            return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");
        }
    }

    /**
     * 查找某一時段內 IP/MAC/Port mapping資料
     * @param model
     * @param request
     * @param response
     * @param queryGroup
     * @param queryIp
     * @param queryDateBegin
     * @param queryDateEnd
     * @param queryTimeBegin
     * @param queryTimeEnd
     * @return
     */
    @RequestMapping(value = "getMappingRecord.json", method = RequestMethod.POST)
    public @ResponseBody AppResponse getMappingRecord(
            Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

        try {



        } catch (Exception e) {
            log.error(e.toString(), e);
        }

        return null;
    }
}
