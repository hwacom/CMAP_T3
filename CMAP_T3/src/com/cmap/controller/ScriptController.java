package com.cmap.controller;

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
import com.cmap.exception.ServiceLayerException;
import com.cmap.security.SecurityUtil;
import com.cmap.service.ScriptService;
import com.cmap.service.vo.ScriptServiceVO;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/script")
public class ScriptController extends BaseController {
  @Log
  private static Logger log;

  private static final String[] UI_SCRIPT_TABLE_COLUMNS =
      new String[] {"", "", "si.scriptName", "si.scriptType.scriptTypeCode", "si.systemVersion",
          "si.actionScript", "si.actionScriptRemark", "si.checkScript", "si.checkScriptRemark",
          "si.createTime", "si.updateTime"};

  @Autowired
  private ScriptService scriptService;

  @RequestMapping(value = "", method = RequestMethod.GET)
  public String main(Model model, Principal principal, HttpServletRequest request,
      HttpServletResponse response) {
    Map<String, String> scriptTypeMap = null;
    try {
      scriptTypeMap = getScriptTypeList(null);

    } catch (Exception e) {
      log.error(e.toString(), e);

    } finally {
      model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
      model.addAttribute("enableModify", Env.ENABLE_CM_SCRIPT_MODIFY);

      model.addAttribute("scriptType", "");
      model.addAttribute("scriptTypeList", scriptTypeMap);
    }

    return "script/script_main";
  }

  /**
   * 查找腳本資料
   *
   * @param model
   * @param request
   * @param response
   * @param startNum
   * @param pageLength
   * @param queryScriptTypeCode
   * @param searchValue
   * @param orderColIdx
   * @param orderDirection
   * @return
   */
  @RequestMapping(value = "getScriptData.json", method = RequestMethod.POST)
  public @ResponseBody DatatableResponse findScriptData(Model model, HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(name = "start", required = false, defaultValue = "0") Integer startNum,
      @RequestParam(name = "length", required = false, defaultValue = "10") Integer pageLength,
      @RequestParam(name = "queryScriptTypeCode", required = false,
          defaultValue = "") String queryScriptTypeCode,
      @RequestParam(name = "search[value]", required = false, defaultValue = "") String searchValue,
      @RequestParam(name = "order[0][column]", required = false,
          defaultValue = "6") Integer orderColIdx,
      @RequestParam(name = "order[0][dir]", required = false,
          defaultValue = "desc") String orderDirection) {

    long total = 0;
    long filterdTotal = 0;
    List<ScriptServiceVO> dataList = new ArrayList<>();
    ScriptServiceVO ssVO;
    try {
      ssVO = new ScriptServiceVO();
      ssVO.setStartNum(startNum);
      ssVO.setPageLength(pageLength);
      ssVO.setSearchValue(searchValue);
      ssVO.setOrderColumn(UI_SCRIPT_TABLE_COLUMNS[orderColIdx]);
      ssVO.setOrderDirection(orderDirection);
      ssVO.setQueryScriptTypeCode(queryScriptTypeCode);

      filterdTotal = scriptService.countScriptInfo(ssVO);

      if (filterdTotal != 0) {
        dataList = scriptService.findScriptInfo(ssVO, startNum, pageLength);
      }

      filterdTotal = scriptService.countScriptInfo(new ScriptServiceVO());

    } catch (ServiceLayerException sle) {
    } catch (Exception e) {
      log.error(e.toString(), e);
    }

    return new DatatableResponse(total, dataList, filterdTotal);
  }

  @RequestMapping(value = "/view", method = RequestMethod.POST,
      produces = "application/json;odata=verbose")
  public @ResponseBody AppResponse viewConfig(Model model, Principal principal,
      HttpServletRequest request, HttpServletResponse response, @RequestBody JsonNode jsonData) {

    final String commonErrorMsg = "預覽內容發生錯誤，請重新操作";
    try {
      String scriptInfoId = jsonData.findValue("scriptInfoId").asText();

      if (StringUtils.isBlank(scriptInfoId)) {
        return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, "資料取得異常");
      }

      ScriptServiceVO ssVO = scriptService.getScriptInfoByScriptInfoId(scriptInfoId);

      if (ssVO != null) {
        String viewType = jsonData.findValue("type").asText();

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("script", ssVO.getScriptName());

        if (StringUtils.equals(viewType, "A")) {
          retMap.put("content", ssVO.getActionScript());

        } else if (StringUtils.equals(viewType, "C")) {
          retMap.put("content", ssVO.getCheckScript());
        }

        return new AppResponse(HttpServletResponse.SC_OK, "資料取得正常", retMap);

      } else {
        return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, commonErrorMsg);
      }

    } catch (ServiceLayerException sle) {
      return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, commonErrorMsg);

    } catch (Exception e) {
      log.error(e.toString(), e);
      return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, commonErrorMsg);
    }
  }
}
