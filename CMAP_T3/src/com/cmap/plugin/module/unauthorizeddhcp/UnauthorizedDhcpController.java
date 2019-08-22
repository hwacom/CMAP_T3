package com.cmap.plugin.module.unauthorizeddhcp;

import java.security.Principal;
import java.util.ArrayList;
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

import com.cmap.DatatableResponse;
import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;
import com.cmap.exception.ServiceLayerException;
import com.cmap.security.SecurityUtil;

@Controller
@RequestMapping("/plugin/module/unauthorizedDHCP")
public class UnauthorizedDhcpController extends BaseController {
	@Log
    private static Logger log;

    private static final String[] UI_COLUMNS = new String[] {"","Create_Time","Group_Name","Device_Name","Port"};

    @Autowired
    private UnauthorizedDhcpService unauthorizedDhcpService;
    
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
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String ipRecord(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}
		return "plugin/module_unauthorized_dhcp";
	}
	
	/**
	 * 查找非授權DHCP紀錄
	 * @param model
	 * @param request
	 * @param response
	 * @param queryGroup
	 * @param queryDevice
	 * @param queryIp
	 * @param queryMac
	 * @param queryPort
	 * @param startNum
	 * @param pageLength
	 * @param searchValue
	 * @param orderColIdx
	 * @param orderDirection
	 * @return
	 */
	@RequestMapping(value = "getUnauthorizedDhcpRecord.json", method = RequestMethod.POST)
    public @ResponseBody DatatableResponse getUnauthorizedDhcpRecord(
            Model model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(name="queryGroup", required=true, defaultValue="") String queryGroup,
            @RequestParam(name="queryDevice", required=false, defaultValue="") String queryDevice,
            @RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
            @RequestParam(name="length", required=false, defaultValue="100") Integer pageLength,
            @RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
            @RequestParam(name="order[0][column]", required=false, defaultValue="2") Integer orderColIdx,
            @RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {

    	long total = 0;
		long filterdTotal = 0;
		List<UnauthorizedDhcpServiceVO> dataList = new ArrayList<>();
		UnauthorizedDhcpServiceVO udsVO = null;
        try {
        	udsVO = new UnauthorizedDhcpServiceVO();
        	udsVO.setQueryGroup(queryGroup);
        	udsVO.setQueryDevice(queryDevice);
        	udsVO.setStartNum(startNum);
        	udsVO.setPageLength(pageLength);
        	udsVO.setSearchValue(searchValue);
        	udsVO.setOrderColumn(UI_COLUMNS[orderColIdx]);
        	udsVO.setOrderDirection(orderDirection);

        	filterdTotal = unauthorizedDhcpService.countUnauthorizedDhcpRecord(udsVO);

			if (filterdTotal != 0) {
				dataList = unauthorizedDhcpService.findUnauthorizedDhcpRecord(udsVO);
			}

			udsVO = new UnauthorizedDhcpServiceVO();
			udsVO.setQueryGroup(queryGroup);
        	
			total = unauthorizedDhcpService.countUnauthorizedDhcpRecord(udsVO);
			
        } catch (ServiceLayerException sle) {
		} catch (Exception e) {
            log.error(e.toString(), e);
        }

        return new DatatableResponse(total, dataList, filterdTotal);
    }
}
