package com.cmap.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.cmap.service.VersionService;
import com.cmap.service.vo.VersionServiceVO;
import com.cmap.utils.FileUtils;
import com.cmap.utils.impl.FtpFileUtils;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/version")
public class VersionController extends BaseController {
	private static Log log = LogFactory.getLog(VersionController.class);
	
	private static final String[] MANAGE_TAB_HEADERS = new String[] {"","","group_Name","device_Name","system_Version","config_Version","create_Time"};

	@Autowired
	private VersionService versionService;
	
	private void initMenu(Model model, HttpServletRequest request) {
		Map<String, String> groupListMap = null;
		Map<String, String> deviceListMap = null;
		try {
			groupListMap = getGroupList(request);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
		} finally {
			model.addAttribute("queryGroup1", "");
			model.addAttribute("group1List", groupListMap);
			model.addAttribute("queryDevice1", "");
			model.addAttribute("device1List", deviceListMap);
			
			model.addAttribute("queryGroup2", "");
			model.addAttribute("group2List", groupListMap);
			model.addAttribute("queryDevice2", "");
			model.addAttribute("device2List", deviceListMap);
		}
	}
	
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String manageMain(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
			initMenu(model, request);
		}
		
		return "version/version_main";
	}
	
	@RequestMapping(value = "/viewConfig", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse viewConfig(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		try {
			List<String> versionIDs = new ArrayList<String>();
			for (JsonNode jn : jsonData.findValues("value")) {
				versionIDs.add(jn.asText());
			}
			
			FileUtils ftpUtils = new FtpFileUtils();
			ftpUtils.connect("Administrator", "!QAZ2wsx");
			ftpUtils.listFiles();
			ftpUtils.disconnect();
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
			initMenu(model, request);
		}
		
		return new AppResponse(HttpServletResponse.SC_OK, "比對成功");
	}
	
	@RequestMapping(value = "/compare", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse compareFiles(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		try {
			List<String> versionIDs = new ArrayList<String>();
			for (JsonNode jn : jsonData.findValues("value")) {
				versionIDs.add(jn.asText());
			}
			
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
			initMenu(model, request);
		}
		
		return new AppResponse(HttpServletResponse.SC_OK, "比對成功");
	}
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse deleteFiles(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		try {
			List<String> versionIDs = new ArrayList<String>();
			for (JsonNode jn : jsonData.findValues("value")) {
				versionIDs.add(jn.asText());
			}
			
			boolean delResult = versionService.deleteVersionInfo(versionIDs);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
			initMenu(model, request);
		}
		
		return new AppResponse(HttpServletResponse.SC_OK, "刪除成功");
	}
	
	//@RequestMapping(value = "getVersionInfoData.json", method = RequestMethod.POST, produces="application/json;odata=verbose")
	@RequestMapping(value = "getVersionInfoData.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse findVersionInfoData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="10") Integer pageLength,
			@RequestParam(name="queryGroup1", required=false, defaultValue="") String queryGroup1,
			@RequestParam(name="queryGroup2", required=false, defaultValue="") String queryGroup2,
			@RequestParam(name="queryDevice1", required=false, defaultValue="") String queryDevice1,
			@RequestParam(name="queryDevice2", required=false, defaultValue="") String queryDevice2,
			@RequestParam(name="queryDateBegin1", required=false, defaultValue="") String queryDateBegin1,
			@RequestParam(name="queryDateEnd1", required=false, defaultValue="") String queryDateEnd1,
			@RequestParam(name="queryDateBegin2", required=false, defaultValue="") String queryDateBegin2,
			@RequestParam(name="queryDateEnd2", required=false, defaultValue="") String queryDateEnd2,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="2") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="asc") String orderDirection) {
		
		long total = 0;
		long filterdTotal = 0;
		List<VersionServiceVO> dataList = new ArrayList<VersionServiceVO>();
		VersionServiceVO vsVO;
		try {
			vsVO = new VersionServiceVO();
		    setQueryGroupList(request, vsVO, StringUtils.isNotBlank(queryGroup1) ? "queryGroup1" : "queryGroup1List", queryGroup1);
		    setQueryDeviceList(request, vsVO, StringUtils.isNotBlank(queryDevice1) ? "queryDevice1" : "queryDevice1List", queryGroup1, queryDevice1);
			vsVO.setQueryDateBegin1(queryDateBegin1);
			vsVO.setQueryDateEnd1(queryDateEnd1);
			setQueryGroupList(request, vsVO, StringUtils.isNotBlank(queryGroup2) ? "queryGroup2" : "queryGroup2List", queryGroup2);
			setQueryDeviceList(request, vsVO, StringUtils.isNotBlank(queryDevice2) ? "queryDevice2" : "queryDevice2List", queryGroup2, queryDevice2);
			vsVO.setQueryDateBegin2(queryDateBegin2);
			vsVO.setQueryDateEnd2(queryDateEnd2);
			vsVO.setStartNum(startNum);
			vsVO.setPageLength(pageLength);
			vsVO.setSearchValue(searchValue);
			vsVO.setOrderColumn(MANAGE_TAB_HEADERS[orderColIdx]);
			vsVO.setOrderDirection(orderDirection);
			
			/*
			 * 底下兩個參數用來後續查找使用者所有有權限的群組和設備筆數
			 */
			setQueryGroupList(request, vsVO, "allGroupList", null);
			setQueryDeviceList(request, vsVO, "allDeviceList", null, null);
			
			filterdTotal = versionService.countVersionInfo(vsVO);
			
			if (filterdTotal != 0) {
				dataList = versionService.findVersionInfo(vsVO, startNum, pageLength);
			}
			
			total = versionService.countUserPermissionAllVersionInfo(vsVO.getAllGroupList(), vsVO.getAllDeviceList());
			
		} catch (Exception e) {
			
		}
		
		return new DatatableResponse(total, dataList, filterdTotal);
	}
	
	@RequestMapping(value = "/backup", method = RequestMethod.GET)
	public String backupMain(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
			initMenu(model, request);
		}
		
		return "version/version_backup";
	}
	
	@RequestMapping(value = "/recover", method = RequestMethod.GET)
	public String recoverMain(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
			initMenu(model, request);
		}
		
		return "version/version_recover";
	}
}
