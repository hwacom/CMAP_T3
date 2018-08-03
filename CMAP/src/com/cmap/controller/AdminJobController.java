package com.cmap.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
import com.cmap.service.JobService;
import com.cmap.service.vo.JobServiceVO;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/admin/job")
public class AdminJobController extends BaseController {
	private static Log log = LogFactory.getLog(AdminJobController.class);
	
	private static final String[] UI_TABLE_COLUMNS = new String[] {"","","group_Name","device_Name","system_Version","config_Version","create_Time"};
	
	@Autowired
	JobService jobService;

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String adminJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
		}
		
		return "admin/admin_job";
	}
	
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
			@RequestParam(name="inputClassName", required=true) String inputClassName,
			@RequestParam(name="inputDeviceListIds", required=false) List<String> inputDeviceListIds,
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
			jsVO.setInputClassName(inputClassName);
			jsVO.setInputDeviceListIds(inputDeviceListIds);
			jsVO.setInputConfigType(inputConfigType);
			jsVO.setInputDataKeepDays(inputDataKeepDays);
			jsVO.setInputMisFirePolicy(inputMisFirePolicy);
			
			if (StringUtils.isNotBlank(jobKeyName) && StringUtils.isNotBlank(jobKeyGroup)) {
				jobService.modifyJob(jsVO);
				return new AppResponse(HttpServletResponse.SC_OK, "修改成功");
				
			} else {
				jobService.addJob(jsVO);
				return new AppResponse(HttpServletResponse.SC_OK, "新增成功");
			}
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
    }
	
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
	
	@RequestMapping(value="pause", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse pauseJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		try {
			jobService.pauseJob(retrieveKeyVal(jsonData));
			
			return new AppResponse(HttpServletResponse.SC_OK, "選定的排程已暫停");
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
    }
	
	@RequestMapping(value="resume", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse resumeJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		try {
			jobService.resumeJob(retrieveKeyVal(jsonData));
			
			return new AppResponse(HttpServletResponse.SC_OK, "選定的排程已恢復運作");
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
    }
	
	@RequestMapping(value="delete", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse deleteJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestBody JsonNode jsonData) {
		
		try {
			jobService.deleteJob(retrieveKeyVal(jsonData));
			
			return new AppResponse(HttpServletResponse.SC_OK, "選定的排程已暫停");
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			return new AppResponse(super.getLineNumber(), e.getMessage());
		}
    }
	
	@RequestMapping(value = "getJobInfo.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse findDeviceListData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="10") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="6") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="desc") String orderDirection) {
		
		long total = 0;
		long filterdTotal = 0;
		List<JobServiceVO> dataList = new ArrayList<JobServiceVO>();
		JobServiceVO jsVO;
		try {
			jsVO = new JobServiceVO();
			jsVO.setStartNum(startNum);
			jsVO.setPageLength(pageLength);
			jsVO.setSearchValue(searchValue);
			jsVO.setOrderColumn(UI_TABLE_COLUMNS[orderColIdx]);
			jsVO.setOrderDirection(orderDirection);
			
			filterdTotal = jobService.countJobInfoByVO(jsVO);
			
			if (filterdTotal > 0) {
				dataList = jobService.findJobInfoByVO(jsVO);
			}
			
			total = jobService.countJobInfoByVO(null);
			
		} catch (Exception e) {
			
		}
		
		return new DatatableResponse(total, dataList, filterdTotal);
	}
}
