package com.cmap.controller.admin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.cmap.Constants;
import com.cmap.DatatableResponse;
import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;
import com.cmap.security.SecurityUtil;
import com.cmap.service.EnvService;
import com.cmap.service.vo.EnvServiceVO;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/admin/env")
public class AdminEnvController extends BaseController {
	@Log
	private static Logger log;

	private static final String[] UI_TABLE_COLUMNS = new String[] {"","","settingName","settingValue","settingRemark","createTime","createBy","updateTime","updateBy"};

	@Autowired
	EnvService envService;

	private void init(Model model, HttpServletRequest request) {
		model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
	}

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String adminEnv(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			init(model, request);
		}

		return "admin/admin_env";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody AppResponse deleteEnv(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

		try {
			Iterator<JsonNode> idIt = jsonData.findValues(Constants.JSON_FIELD_SETTING_IDS).get(0).iterator();

			List<String> settingIds = new ArrayList<>();
			while (idIt.hasNext()) {
				settingIds.add(idIt.next().asText());
			}

			String retMsg = envService.deleteEnvSettings(settingIds);

			return new AppResponse(HttpServletResponse.SC_OK, retMsg);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
			init(model, request);
		}
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody AppResponse modifyEnv(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

		try {
			List<EnvServiceVO> esVOs = new ArrayList<>();

			Iterator<JsonNode> idIt = jsonData.findValues(Constants.JSON_FIELD_SETTING_IDS).get(0).iterator();
			Iterator<JsonNode> nameIt = jsonData.findValues(Constants.JSON_FIELD_MODIFY_SETTING_NAME).get(0).iterator();
			Iterator<JsonNode> valueIt = jsonData.findValues(Constants.JSON_FIELD_MODIFY_SETTING_VALUE).get(0).iterator();
			Iterator<JsonNode> remarkIt = jsonData.findValues(Constants.JSON_FIELD_MODIFY_SETTING_REMARK).get(0).iterator();

			EnvServiceVO esVO;
			while (nameIt.hasNext()) {
				esVO = new EnvServiceVO();
				esVO.setSettingId(idIt.hasNext() ? idIt.next().asText() : null);
				esVO.setModifySettingName(nameIt.hasNext() ? nameIt.next().asText() : null);
				esVO.setModifySettingValue(valueIt.hasNext() ? valueIt.next().asText() : null);
				esVO.setModifySettingRemark(remarkIt.hasNext() ? remarkIt.next().asText() : null);

				esVOs.add(esVO);
			}

			String retMsg = envService.addOrModifyEnvSettings(esVOs, request);
			return new AppResponse(HttpServletResponse.SC_OK, retMsg);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
			init(model, request);
		}
	}

	@RequestMapping(value = "getEnvConfig.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse findDeviceListData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="10") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="") String orderDirection,
			@RequestParam(name="settingName", required=false, defaultValue="") String settingName,
			@RequestParam(name="settingValue", required=false, defaultValue="") String settingValue,
			@RequestParam(name="settingRemark", required=false, defaultValue="") String settingRemark) {

		long total = 0;
		long filterdTotal = 0;
		String msg = null;
		List<EnvServiceVO> dataList = new ArrayList<>();
		EnvServiceVO esVO;
		try {
			esVO = new EnvServiceVO();
			esVO.setSettingName(settingName);
			esVO.setSettingValue(settingValue);
			esVO.setSettingRemark(settingRemark);

			if (StringUtils.isNotBlank(searchValue)) {
				esVO.setSearchValue(searchValue);
			}
			if (orderColIdx != null) {
				esVO.setOrderColumn(UI_TABLE_COLUMNS[orderColIdx]);
				esVO.setOrderDirection(orderDirection);
			}

			filterdTotal = envService.countEnvSettingsByVO(esVO);

			if (filterdTotal > 0) {
				dataList = envService.findEnvSettingsByVO(esVO, null, null);
			}

			total = envService.countEnvSettingsByVO(null);

			if (!dataList.isEmpty() && dataList.get(0).getDifferCount() != 0) {
				msg =  "Notice: 有 "+dataList.get(0).getDifferCount()+" 筆設定未同步!!";
			}

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			init(model, request);
		}

		return new DatatableResponse(total, dataList, filterdTotal, msg);
	}

	@RequestMapping(value="refreshAll", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/json")
	public @ResponseBody AppResponse refreshAllEnv(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

		try {
			String retMsg = envService.refreshAllEnv();

			return new AppResponse(HttpServletResponse.SC_OK, retMsg);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());

		} finally {
			init(model, request);
		}
	}
}
