package com.cmap.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.cmap.service.EnvService;
import com.cmap.service.vo.EnvServiceVO;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/admin/env")
public class AdminEnvController extends BaseController {
	private static Log log = LogFactory.getLog(AdminEnvController.class);

	private static final String[] UI_TABLE_COLUMNS = new String[] {"","","settingName","settingValue","settingRemark","createTime","createBy","updateTime","updateBy"};
	
	@Autowired
	EnvService envService;
	
	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String adminEnv(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
		}
		
		return "admin/admin_env";
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody AppResponse deleteEnv(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="settingIds[]", required=true) List<String> settingIds) {
		
		try {
			String retMsg = envService.deleteEnvSettings(settingIds);
			
			return new AppResponse(HttpServletResponse.SC_OK, retMsg);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
    }
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody AppResponse modifyEnv(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		try {
			List<EnvServiceVO> esVOs = new ArrayList<EnvServiceVO>();
			
			Iterator<JsonNode> idIt = jsonData.findValues("settingIds").get(0).iterator();
			Iterator<JsonNode> nameIt = jsonData.findValues("modifySettingName").get(0).iterator();
			Iterator<JsonNode> valueIt = jsonData.findValues("modifySettingValue").get(0).iterator();
			Iterator<JsonNode> remarkIt = jsonData.findValues("modifySettingRemark").get(0).iterator();
			
			EnvServiceVO esVO;
			while (nameIt.hasNext()) {
				esVO = new EnvServiceVO();
				esVO.setSettingId(idIt.hasNext() ? idIt.next().asText() : null);
				esVO.setModifySettingName(nameIt.hasNext() ? nameIt.next().asText() : null);
				esVO.setModifySettingValue(valueIt.hasNext() ? valueIt.next().asText() : null);
				esVO.setModifySettingRemark(remarkIt.hasNext() ? remarkIt.next().asText() : null);
			
				esVOs.add(esVO);
			}
			
			String retMsg = envService.addOrModifyEnvSettings(esVOs);
			return new AppResponse(HttpServletResponse.SC_OK, retMsg);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
    }
	
	@RequestMapping(value = "getEnvConfig.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse findDeviceListData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="settingName", required=false, defaultValue="") String settingName,
			@RequestParam(name="settingValue", required=false, defaultValue="") String settingValue,
			@RequestParam(name="settingRemark", required=false, defaultValue="") String settingRemark) {
		
		long total = 0;
		long filterdTotal = 0;
		String msg = null;
		List<EnvServiceVO> dataList = new ArrayList<EnvServiceVO>();
		EnvServiceVO esVO;
		try {
			esVO = new EnvServiceVO();
			esVO.setSettingName(settingName);
			esVO.setSettingValue(settingValue);
			esVO.setSettingRemark(settingRemark);
			
			filterdTotal = envService.countEnvSettingsByVO(esVO);
			
			if (filterdTotal > 0) {
				dataList = envService.findEnvSettingsByVO(esVO, null, null);
			}
			
			total = envService.countEnvSettingsByVO(null);
			
			if (!dataList.isEmpty() && dataList.get(0).getDifferCount() != 0) {
				msg =  "Notice: 有 "+dataList.get(0).getDifferCount()+" 筆設定未同步!!";
			}
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
		
		return new DatatableResponse(total, dataList, filterdTotal, msg);
	}
	
	@RequestMapping(value="refreshAll", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody AppResponse refreshAllEnv(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		try {
			String retMsg = envService.refreshAllEnv();
			
			return new AppResponse(HttpServletResponse.SC_OK, retMsg);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
    }
}
