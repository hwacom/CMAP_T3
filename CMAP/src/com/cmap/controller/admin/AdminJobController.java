package com.cmap.controller.admin;

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
import com.cmap.service.JobService;
import com.cmap.service.vo.JobServiceVO;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/admin/job")
public class AdminJobController extends BaseController {
	@Log
	private static Logger log;

	private static final String[] UI_TABLE_COLUMNS =
			new String[] {"","","qt.jobGroup","qt.jobName","qt.priority","qt.triggerState","qt.prevFireTime","qt.nextFireTime","qt.misfireInstr","qct.cronExpression","qct.timeZoneId","qjd.jobClassName","qjd.description"};

	@Autowired
	JobService jobService;

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String adminJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			model.addAttribute("inputSchedType", getMenuItem(Env.MENU_CODE_OF_SCHED_TYPE, true));
			model.addAttribute("inputConfigType", getMenuItem(Env.MENU_CODE_OF_CONFIG_TYPE, true));
			model.addAttribute("inputMisFirePolicy", getMenuItem(Env.MENU_CODE_OF_MIS_FIRE_POLICY, true));
		}

		return "admin/admin_job";
	}

	@RequestMapping(value="save", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public AppResponse saveJob(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

		JobServiceVO jsVO = new JobServiceVO();
		try {
			convertJson2POJO(jsVO, jsonData);

			if (StringUtils.isNotBlank(jsVO.getJobKeyName()) && StringUtils.isNotBlank(jsVO.getJobKeyGroup())) {
				jobService.modifyJob(jsVO);
				return new AppResponse(HttpServletResponse.SC_OK, "修改成功");

			} else {
				jobService.addJob(jsVO);
				return new AppResponse(HttpServletResponse.SC_OK, "新增成功");
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
	}
	/*
	@RequestMapping(value="save", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public AppResponse saveJob(
			@RequestParam(name="inputSchedType", required=true) String inputSchedType,
			@RequestParam(name="inputSchedName", required=false, defaultValue="") String inputSchedName,
			@RequestParam(name="inputJobName", required=true) String inputJobName,
			@RequestParam(name="inputJobGroup", required=true) String inputJobGroup,
			@RequestParam(name="inputDescription", required=false, defaultValue="") String inputDescription,
			@RequestParam(name="inputCronExpression", required=true) String inputCronExpression,
			@RequestParam(name="inputPriority", required=false, defaultValue="") Integer inputPriority,
			@RequestParam(name="inputDeviceListIds[]", required=false) List<String> inputDeviceListIds,
			@RequestParam(name="inputGroupIds[]", required=false) List<String> inputGroupIds,
			@RequestParam(name="inputDeviceIds[]", required=false) List<String> inputDeviceIds,
			@RequestParam(name="inputConfigType", required=false) String inputConfigType,
			@RequestParam(name="inputDataKeepDays", required=false) String inputDataKeepDays,
			@RequestParam(name="inputMisFirePolicy", required=false) Integer inputMisFirePolicy,
			@RequestParam(name="jobKeyName", required=false, defaultValue="") String jobKeyName,
			@RequestParam(name="jobKeyGroup", required=false, defaultValue="") String jobKeyGroup) {

		JobServiceVO jsVO = null;
		try {
			jsVO = new JobServiceVO();
			jsVO.setInputSchedType(inputSchedType);
			jsVO.setInputSchedName(inputSchedName);
			jsVO.setInputJobName(inputJobName);
			jsVO.setInputJobGroup(inputJobGroup);
			jsVO.setInputDescription(inputDescription);
			jsVO.setInputCronExpression(inputCronExpression);
			jsVO.setInputPriority(inputPriority);
			jsVO.setInputDeviceListIds(inputDeviceListIds);
			jsVO.setInputGroupIds(inputGroupIds);
			jsVO.setInputDeviceIds(inputDeviceIds);
			jsVO.setInputConfigType(inputConfigType);
			jsVO.setInputDataKeepDays(inputDataKeepDays);
			jsVO.setInputMisFirePolicy(inputMisFirePolicy);
			jsVO.setJobKeyName(jobKeyName);
			jsVO.setJobKeyGroup(jobKeyGroup);

			if (StringUtils.isNotBlank(jobKeyName) && StringUtils.isNotBlank(jobKeyGroup)) {
				jobService.modifyJob(jsVO);
				return new AppResponse(HttpServletResponse.SC_OK, "修改成功");

			} else {
				jobService.addJob(jsVO);
				return new AppResponse(HttpServletResponse.SC_OK, "新增成功");
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
    }
	 */

	/**
	 * 解析JSON data取得使用者選擇的JOB項目的Name & Group，for後續操作使用
	 * @param jsonData
	 * @return
	 */
	private List<JobServiceVO> retrieveKeyVal(JsonNode jsonData) {
		List<JobServiceVO> jsVOList = new ArrayList<JobServiceVO>();
		JobServiceVO jsVO;
		for (JsonNode jn : jsonData.findValues("keyVal")) {
			jsVO = new JobServiceVO();
			jsVO.setJobKeyName(jn.asText().split(Env.COMM_SEPARATE_SYMBOL)[0]);
			jsVO.setJobKeyGroup(jn.asText().split(Env.COMM_SEPARATE_SYMBOL)[1]);
			jsVOList.add(jsVO);
		}

		return jsVOList;
	}

	@RequestMapping(value="modify", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody AppResponse modifyJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

		try {
			Map<String, Object> retMap = new HashMap<String, Object>();
			JobServiceVO retVO;

			List<JobServiceVO> inputVOs = retrieveKeyVal(jsonData);
			if (inputVOs.size() != 1) {
				return new AppResponse(HttpServletResponse.SC_FORBIDDEN, "修改僅允許勾選一項，請重新選擇");
			}

			JobServiceVO jsVO = inputVOs.get(0);
			retVO = jobService.findJobInfoByVO(jsVO).get(0);

			retMap.put("inputSchedType", retVO.getSchedType());
			retMap.put("inputJobName", retVO.getJobName());
			retMap.put("inputJobGroup", retVO.getJobGroup());
			retMap.put("inputDescription", retVO.getDescription());
			retMap.put("inputCronExpression", retVO.getCronExpression());
			retMap.put("inputPriority", retVO.getPriority());
			retMap.put("inputConfigType", retVO.getConfigType());
			retMap.put("inputMisFirePolicy", retVO.getMisfireInstr());
			retMap.put("inputGroupIds", retVO.getGroupIdsStr());
			retMap.put("inputDeviceIds", retVO.getDeviceIdsStr());
			retMap.put("inputFtpName", retVO.getFtpName());
			retMap.put("inputFtpHost", retVO.getFtpHost());
			retMap.put("inputFtpPort", retVO.getFtpPort());
			retMap.put("inputFtpAccount", retVO.getFtpAccount());
			retMap.put("inputFtpPassword", retVO.getFtpPassword());

			return new AppResponse(HttpServletResponse.SC_OK, "OK", retMap);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
	}

	@RequestMapping(value="excute", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody AppResponse excuteJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

		try {
			String msg = jobService.fireJobImmediately(retrieveKeyVal(jsonData));

			return new AppResponse(HttpServletResponse.SC_OK, msg);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
	}

	@RequestMapping(value="pause", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody AppResponse pauseJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

		try {
			String msg = jobService.pauseJob(retrieveKeyVal(jsonData));

			return new AppResponse(HttpServletResponse.SC_OK, msg);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
	}

	@RequestMapping(value="resume", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse resumeJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

		try {
			String msg = jobService.resumeJob(retrieveKeyVal(jsonData));

			return new AppResponse(HttpServletResponse.SC_OK, msg);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
	}

	@RequestMapping(value="delete", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse deleteJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {

		try {
			String msg = jobService.deleteJob(retrieveKeyVal(jsonData));

			return new AppResponse(HttpServletResponse.SC_OK, msg);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
	}

	@RequestMapping(value = "getJobInfo.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse findDeviceListData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="10") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="") String orderDirection) {

		long total = 0;
		long filterdTotal = 0;
		List<JobServiceVO> dataList = new ArrayList<JobServiceVO>();
		JobServiceVO jsVO;
		try {
			jsVO = new JobServiceVO();
			jsVO.setStartNum(startNum);
			jsVO.setPageLength(pageLength);

			if (StringUtils.isNotBlank(searchValue)) {
				jsVO.setSearchValue(searchValue);
			}
			if (orderColIdx != null) {
				jsVO.setOrderColumn(UI_TABLE_COLUMNS[orderColIdx]);
				jsVO.setOrderDirection(orderDirection);
			}

			filterdTotal = jobService.countJobInfoByVO(jsVO);

			if (filterdTotal > 0) {
				dataList = jobService.findJobInfoByVO(jsVO);
			}

			total = jobService.countJobInfoByVO(null);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return new DatatableResponse(total, dataList, filterdTotal);
	}

	@RequestMapping(value = "getJobDetails.json", method = RequestMethod.POST)
	public @ResponseBody AppResponse findJobDetails(
			Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="jobKeyName", required=false, defaultValue="") String jobKeyName,
			@RequestParam(name="jobKeyGroup", required=false, defaultValue="") String jobKeyGroup) {

		try {
			JobServiceVO jsVO = new JobServiceVO();
			jsVO.setJobKeyName(jobKeyName);
			jsVO.setJobKeyGroup(jobKeyGroup);

			jsVO = jobService.findJobDetails(jsVO);

			Map<String, Object> retMap = new HashMap<String, Object>();
			if (jsVO != null) {
				retMap.put("schedType", jsVO.getSchedType());
				retMap.put("schedTypeName", jsVO.getSchedTypeName());
				retMap.put("configType", jsVO.getConfigType());
				retMap.put("groupId", jsVO.getGroupIdsStr());
				retMap.put("deviceId", jsVO.getDeviceIdsStr());

				retMap.put("ftpName", jsVO.getFtpName());
				retMap.put("ftpHost", jsVO.getFtpHost());
				retMap.put("ftpPort", jsVO.getFtpPort());
				retMap.put("ftpAccount", jsVO.getFtpAccount());
				retMap.put("ftpPassword", jsVO.getFtpPassword());
			}

			return new AppResponse(HttpServletResponse.SC_OK, "資料取得正常", retMap);

		} catch (Exception e) {

		}

		return null;
	}
}
