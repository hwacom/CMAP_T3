package com.cmap.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.cmap.Env;
import com.cmap.service.VersionService;
import com.cmap.service.vo.VersionServiceVO;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/version")
public class VersionController extends BaseController {
	private static Log log = LogFactory.getLog(VersionController.class);
	
	private static final String[] UI_TABLE_COLUMNS = new String[] {"","","group_Name","device_Name","system_Version","config_Version","create_Time"};

	@Autowired
	private VersionService versionService;
	
	/**
	 * 初始化選單
	 * @param model
	 * @param request
	 */
	private void initMenu(Model model, HttpServletRequest request) {
		Map<String, String> groupListMap = null;
		Map<String, String> deviceListMap = null;
		Map<String, String> configTypeMap = null;
		try {
			groupListMap = getGroupList(request);
			configTypeMap = getMenuItem(Env.MENU_CODE_OF_CONFIG_TYPE, true);
			
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
			
			model.addAttribute("queryConfigType", "");
			model.addAttribute("configTypeList", configTypeMap);
		}
	}
	
	/**
	 * [版本管理] >> 主頁面
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @return
	 */
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
	
	/**
	 * [版本管理] >> 查看組態檔內容
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @param jsonData
	 * @return
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse viewConfig(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		try {
			List<JsonNode> vIdList = (List<JsonNode>)jsonData.findValues("value");
			
			if (vIdList == null || (vIdList != null && vIdList.isEmpty())) {
				return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");
			}
			
			String versionId = vIdList.get(0).asText();
			System.out.println("view >> versionId: " + versionId);
			
			List<String> versionIDs = new ArrayList<String>();
			versionIDs.add(versionId);
			
			List<VersionServiceVO> vsVOList = versionService.findConfigFilesInfo(versionIDs);
			
			if (vsVOList != null && !vsVOList.isEmpty()) {
				VersionServiceVO retVO = versionService.getConfigFileContent(vsVOList.get(0));
				
				if (StringUtils.isNotBlank(retVO.getConfigFileContent())) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("group", retVO.getGroupName());
					retMap.put("device", retVO.getDeviceName());
					retMap.put("version", retVO.getConfigVersion());
					retMap.put("content", retVO.getConfigFileContent());
					
					return new AppResponse(HttpServletResponse.SC_OK, "資料取得正常", retMap);
					
				} else {
					return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");
				}
			} else {
				return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");
			}
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
			initMenu(model, request);
		}
		
		return null;
	}
	
	/**
	 * [版本管理] >> 版本比對
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @param jsonData
	 * @return
	 */
	@RequestMapping(value = "/compare", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse compareFiles(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		try {
			List<JsonNode> vIdList = (List<JsonNode>)jsonData.findValues("value");
			
			if (vIdList == null || (vIdList != null && vIdList.isEmpty())) {
				return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "版本比對只允許兩份檔案，請重新選擇");
			} else {
				if (vIdList.size() != 2) {
					return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "版本比對只允許兩份檔案，請重新選擇");
				}
			}
			
			List<String> versionIDs = new ArrayList<String>();
			for (JsonNode node : vIdList) {
				System.out.println("view >> versionId: " + node.asText());
				versionIDs.add(node.asText());
			}
			
			List<VersionServiceVO> vsVOList = versionService.findConfigFilesInfo(versionIDs);
			
			if (vsVOList != null && !vsVOList.isEmpty() && vsVOList.size() == 2) {
				VersionServiceVO retVO = versionService.compareConfigFiles(vsVOList);
				
				Map<String, Object> retMap = new HashMap<String, Object>();
				retMap.put("versionLeft", retVO.getVersionOri());
				retMap.put("versionRight", retVO.getVersionRev());
				retMap.put("versionLineNum", retVO.getVersionLineNum());
				retMap.put("contentLeft", retVO.getConfigDiffOriContent());
				retMap.put("contentRight", retVO.getConfigDiffRevContent());
				retMap.put("diffPos", retVO.getDiffPos());
				
				return new AppResponse(HttpServletResponse.SC_OK, "資料取得正常", retMap);
				
			} else {
				return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");
			}
			
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			
		} finally {
			initMenu(model, request);
		}
	}
	
	/**
	 * [版本管理] >> 刪除組態檔
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @param jsonData
	 * @return
	 */
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
	
	/**
	 * [版本管理、備份、還原] >> 查詢按鈕共用入口方法
	 * @param model
	 * @param request
	 * @param response
	 * @param startNum
	 * @param pageLength
	 * @param queryGroup1
	 * @param queryGroup2
	 * @param queryDevice1
	 * @param queryDevice2
	 * @param queryDateBegin1
	 * @param queryDateEnd1
	 * @param queryDateBegin2
	 * @param queryDateEnd2
	 * @param queryNewChkbox
	 * @param searchValue
	 * @param orderColIdx
	 * @param orderDirection
	 * @return
	 */
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
			@RequestParam(name="queryConfigType", required=false, defaultValue="") String queryConfigType,
			@RequestParam(name="queryNewChkbox", required=false, defaultValue="true") boolean queryNewChkbox,
			@RequestParam(name="maxCountByDevice", required=false, defaultValue="false") boolean maxCountByDevice,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="6") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {
		
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
			vsVO.setQueryConfigType(queryConfigType);
			vsVO.setQueryNewChkbox(queryNewChkbox);
			vsVO.setStartNum(startNum);
			vsVO.setPageLength(pageLength);
			vsVO.setSearchValue(searchValue);
			vsVO.setOrderColumn(UI_TABLE_COLUMNS[orderColIdx]);
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
			
			if (!maxCountByDevice) {
				total = versionService.countUserPermissionAllVersionInfo(
											vsVO.getAllGroupList(), vsVO.getAllDeviceList(), vsVO.getQueryConfigType());
			} else {
				total = vsVO.getAllDeviceList().size();
			}
			
		} catch (Exception e) {
			
		}
		
		return new DatatableResponse(total, dataList, filterdTotal);
	}
	
	@RequestMapping(value = "getDeviceListData.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse findDeviceListData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="10") Integer pageLength,
			@RequestParam(name="queryGroup", required=false, defaultValue="") String queryGroup,
			@RequestParam(name="queryDevice", required=false, defaultValue="") String queryDevice,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="6") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {
		
		long total = 0;
		long filterdTotal = 0;
		List<VersionServiceVO> dataList = new ArrayList<VersionServiceVO>();
		VersionServiceVO vsVO;
		try {
			vsVO = new VersionServiceVO();
		    setQueryGroupList(request, vsVO, StringUtils.isNotBlank(queryGroup) ? "queryGroup" : "queryGroupList", queryGroup);
		    setQueryDeviceList(request, vsVO, StringUtils.isNotBlank(queryDevice) ? "queryDevice" : "queryDeviceList", queryGroup, queryDevice);
		    
			vsVO.setStartNum(startNum);
			vsVO.setPageLength(pageLength);
			vsVO.setSearchValue(searchValue);
			vsVO.setOrderColumn(UI_TABLE_COLUMNS[orderColIdx]);
			vsVO.setOrderDirection(orderDirection);
			
			/*
			 * 底下兩個參數用來後續查找使用者所有有權限的群組和設備筆數
			 */
			setQueryGroupList(request, vsVO, "allGroupList", null);
			setQueryDeviceList(request, vsVO, "allDeviceList", null, null);
			
			
			filterdTotal = versionService.countDeviceList(vsVO);
			
			if (filterdTotal != 0) {
				dataList = versionService.findDeviceList(vsVO, startNum, pageLength);
			}
			
			total = vsVO.getAllDeviceList().size();
			
		} catch (Exception e) {
			
		}
		
		return new DatatableResponse(total, dataList, filterdTotal);
	}
	
	/**
	 * [版本備份] >> 主頁面
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @return
	 */
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
	
	@RequestMapping(value = "/backup/execute", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse doBackup(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		VersionServiceVO retVO;
		try {
			List<String> deviceListIDs = new ArrayList<String>();
			String configType = jsonData.findValue("configType").asText();
			for (JsonNode jn : jsonData.findValues("deviceListId")) {
				deviceListIDs.add(jn.asText());
			}
			
			retVO = versionService.backupConfig(configType, deviceListIDs, false);
			
			return new AppResponse(HttpServletResponse.SC_OK, retVO.getRetMsg());
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
			return new AppResponse(HttpServletResponse.SC_NOT_ACCEPTABLE, "備份失敗");
			
		} finally {
			initMenu(model, request);
		}
	}
	
	/**
	 * [版本還原] >> 執行還原動作
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/restore", method = RequestMethod.GET)
	public String recoverMain(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
			initMenu(model, request);
		}
		
		return "version/version_restore";
	}
}
