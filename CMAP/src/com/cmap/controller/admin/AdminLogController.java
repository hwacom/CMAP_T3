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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmap.AppResponse;
import com.cmap.DatatableResponse;
import com.cmap.annotation.Log;
import com.cmap.security.SecurityUtil;
import com.cmap.service.LogService;
import com.cmap.service.LogService.LogType;
import com.cmap.service.vo.LogServiceVO;

@Controller
@RequestMapping("/admin/log")
public class AdminLogController {
	@Log
	private static Logger log;

	private static final String[] ERROR_LOG_COLUMNS =
			new String[] {"","entryDate","logger","logLevel","message","exception"};

	private static final String[] JOB_LOG_COLUMNS =
			new String[] {"","startTime","jobGroup","jobName","result","recordsNum","remark","endTime","spendTimeInSeconds","cronExpression","prevFireTime","nextFireTime",};

	@Autowired
	LogService logService;

	private void init(Model model, HttpServletRequest request) {
		model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
	}

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String adminJob(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			init(model, request);
		}

		return "admin/admin_log";
	}

	@RequestMapping(value = "getErrorLog.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse findrrorLogData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="100") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="") String orderDirection) {

		long total = 0;
		long filterdTotal = 0;
		List<LogServiceVO> dataList = new ArrayList<LogServiceVO>();
		LogServiceVO lsVO;
		try {
			lsVO = new LogServiceVO();
			lsVO.setStartNum(startNum);
			lsVO.setPageLength(pageLength);

			if (StringUtils.isNotBlank(searchValue)) {
				lsVO.setSearchValue(searchValue);
			}
			if (orderColIdx != null) {
				lsVO.setOrderColumn(ERROR_LOG_COLUMNS[orderColIdx]);
				lsVO.setOrderDirection(orderDirection);
			}

			filterdTotal = logService.countSysErrorLogByVO(lsVO);

			if (filterdTotal > 0) {
				dataList = logService.findSysErrorLogByVO(lsVO);
			}

			total = logService.countSysErrorLogByVO(null);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			init(model, request);
		}

		return new DatatableResponse(total, dataList, filterdTotal);
	}

	@RequestMapping(value = "getJobLog.json", method = RequestMethod.POST)
	public @ResponseBody DatatableResponse findJobLogData(
			Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="start", required=false, defaultValue="0") Integer startNum,
			@RequestParam(name="length", required=false, defaultValue="100") Integer pageLength,
			@RequestParam(name="search[value]", required=false, defaultValue="") String searchValue,
			@RequestParam(name="order[0][column]", required=false, defaultValue="") Integer orderColIdx,
			@RequestParam(name="order[0][dir]", required=false, defaultValue="") String orderDirection) {

		long total = 0;
		long filterdTotal = 0;
		List<LogServiceVO> dataList = new ArrayList<LogServiceVO>();
		LogServiceVO lsVO;
		try {
			lsVO = new LogServiceVO();
			lsVO.setStartNum(startNum);
			lsVO.setPageLength(pageLength);

			if (StringUtils.isNotBlank(searchValue)) {
				lsVO.setSearchValue(searchValue);
			}
			if (orderColIdx != null) {
				lsVO.setOrderColumn(JOB_LOG_COLUMNS[orderColIdx]);
				lsVO.setOrderDirection(orderDirection);
			}

			filterdTotal = logService.countSysJobLogByVO(lsVO);

			if (filterdTotal > 0) {
				dataList = logService.findSysJobLogByVO(lsVO);
			}

			total = logService.countSysJobLogByVO(null);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			init(model, request);
		}

		return new DatatableResponse(total, dataList, filterdTotal);
	}

	@RequestMapping(value = "getDetailInfo.json", method = RequestMethod.POST)
	public @ResponseBody AppResponse findJobDetails(
			Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="logType", required=false, defaultValue="") String logType,
			@RequestParam(name="logId", required=false, defaultValue="") String logId) {

		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			LogServiceVO lsVO = new LogServiceVO();
			lsVO.setQueryLogId(logId);

			LogType type = null;
			switch (logType) {
			case "ERROR":
				type = LogType.ERROR_LOG;
				break;
			case "JOB":
				type = LogType.JOB_LOG;
				break;
			}
			lsVO.setQueryLogType(type);

			lsVO = logService.findLogDetail(lsVO);

			if (lsVO != null) {
				retMap.put("details", lsVO.getDetails());
			}

			return new AppResponse(HttpServletResponse.SC_OK, "資料取得正常", retMap);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(HttpServletResponse.SC_NOT_FOUND, "資料取得異常", retMap);

		} finally {
			init(model, request);
		}
	}
}
