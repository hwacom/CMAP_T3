package com.cmap.plugin.module.vmswitch;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cmap.AppResponse;
import com.cmap.Constants;
import com.cmap.PrtgJsonResponse;
import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;
import com.cmap.exception.ServiceLayerException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/plugin/module/vmswitch")
public class VmSwitchController extends BaseController {
	@Log
	private static Logger log;

	@Autowired
	private VmSwitchService vmSwitchService;

	private String logKey = null;

	private void clearSessionAttr(HttpServletRequest request) {
	    request.getSession().removeAttribute(Constants.PREVIOUS_URL);
	}

	@RequestMapping(value = "/push", produces = "text/event-stream;charset=utf-8")
    @ResponseBody
    public String push() {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	    if (logKey == null) {
	        return "data:{\"time\":\"" + sdf.format(new Date()) + "\", \"step\":\"<NONE>\", \"process\":\"\"}\n\n";
	    }

	    String retJson = "";
	    try {
	        ModuleVmProcessLog pLog = vmSwitchService.findFistOneNotPushedLogByLogKey(logKey);

	        pLog.setPushed("Y");
	        pLog.setUpdateBy("SYS");
	        pLog.setUpdateTime(new Timestamp(new Date().getTime()));
	        vmSwitchService.updateLog(pLog);

	        retJson = "retry:200\n"
                        + "data:{"
                        + "\"time\":\"" + sdf.format(new Date()) + "\""
                        + ", \"step\":\"" + pLog.getStep() + "\""
                        + ", \"result\":\"" + pLog.getResult() + "\""
                        + ", \"msg\":\"" + pLog.getMessage() + "\"}\n\n";

	        return retJson;

	    } catch (Exception e) {
	        retJson = "retry:200\n"
                        + "data:{"
                        + "\"time\":\"" + sdf.format(new Date()) + "\""
                        + ", \"step\":\"" + "" + "\""
                        + ", \"result\":\"<ERROR>\""
                        + ", \"msg\":\"" + e.getMessage() + "\"}\n\n";

            return retJson;
	    }
    }

	@RequestMapping(value = "", method = RequestMethod.GET)
    public String main(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
	    try {
	        logKey = "VM_SWITCH_LOG_" + Constants.FORMAT_YYYYMMDD_HH24MISS_NOSYMBOL.format(new Date());

	        String apiVmName = Objects.toString(request.getSession().getAttribute(Constants.VM_SWITCH_HOST_NAME));

	        model.addAttribute("VM_NAME", apiVmName);
	        model.addAttribute("STEP", "@BeGIn..");

	    } catch (Exception e) {
            log.error(e.toString(), e);

        } finally {
            clearSessionAttr(request);
        }

	    return "plugin/module_vm_switch";
	}

	@RequestMapping(value = "/result", method = RequestMethod.GET)
    public String result(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {

        } catch (Exception e) {
            log.error(e.toString(), e);

        } finally {
        }

        return "plugin/module_vm_switch_result";
    }

	/**
	 * 檢核 VM 當前狀態是否正常 for PRTG 呼叫 API 入口
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @param apiVmName
	 * @return
	 */
	@RequestMapping(value = "/chkVmStatus/{apiVmName}", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody String chkStatus(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
	        @PathVariable(required=true) String apiVmName) {

	    String jsonString = "";
	    String chkResult = Constants.VM_STATUS_FINE;
	    try {
	        // Step 1. 判斷VM狀態
	        VmSwitchVO vmSwitchVO = new VmSwitchVO();
	        vmSwitchVO.setApiVmName(apiVmName);

	        vmSwitchVO = vmSwitchService.chkVmStatus(vmSwitchVO);
	        chkResult = vmSwitchVO.getVmStatus();

	        // Step 2. 回傳JSON格式
	        Map<String, String> vMap = new HashMap<>();
            vMap.put("VM_STATUS", chkResult);

            String sensorText = "0:正常 / 1:SSH不通 / 2:No subscribers";

	        PrtgJsonResponse prtgRes = new PrtgJsonResponse(vMap, sensorText);

            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(prtgRes);
            System.out.println(new Date() + " >>> " + jsonString);

	    } catch (Exception e) {
            log.error(e.toString(), e);
	    }

	    return jsonString;
	}

	/**
	 * 檢核 VM 當前狀態是否正常 for VM 切換 UI
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @param jsonData
	 * @return
	 */
	@RequestMapping(value = "/chkStatus", method = RequestMethod.POST, produces="application/json;odata=verbose")
    public @ResponseBody AppResponse chkStatus(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
            @RequestBody JsonNode jsonData) {
	    try {
	        final String apiVmName = Objects.toString(request.getSession().getAttribute(Constants.VM_SWITCH_HOST_NAME));

	        VmSwitchVO vmSwitchVO = new VmSwitchVO();
            vmSwitchVO.setApiVmName(apiVmName);

            vmSwitchVO = vmSwitchService.chkVmStatus(vmSwitchVO);

	        AppResponse app = new AppResponse(HttpServletResponse.SC_OK, "檢查當前VM狀態");
	        app.putData("VM_NOW_FAILURE", vmSwitchVO.isVmNowFailure());
	        app.putData("VM_STATUS_MSG", vmSwitchVO.getVmStatusMsg());

            return app;

	    } catch (Exception e) {
	        log.error(e.toString(), e);

            AppResponse app = new AppResponse(HttpServletResponse.SC_EXPECTATION_FAILED, "檢查當前VM狀態失敗");
            app.putData("VM_NOW_FAILURE", false);
            app.putData("VM_STATUS_MSG", e.getMessage());

            return app;
	    }
	}

	/**
	 * 確認執行備援切換
	 * @param model
	 * @param principal
	 * @param request
	 * @param response
	 * @param jsonData
	 * @return
	 */
	@RequestMapping(value = "/go", method = RequestMethod.POST, produces="application/json;odata=verbose")
    public @ResponseBody AppResponse go(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
            @RequestBody JsonNode jsonData) {

	    String resultMsg = "N/A";
	    int responseCode = HttpServletResponse.SC_EXPECTATION_FAILED;

	    try {
            final String apiVmName = Objects.toString(request.getSession().getAttribute(Constants.VM_SWITCH_HOST_NAME));

            VmSwitchVO vmSwitchVO = new VmSwitchVO();
            vmSwitchVO.setApiVmName(apiVmName);

            vmSwitchVO.setLogKey(logKey);
            resultMsg = vmSwitchService.powerOff(vmSwitchVO);
            responseCode = HttpServletResponse.SC_OK;

        } catch (ServiceLayerException sle) {
            resultMsg = sle.getMessage();
            responseCode = HttpServletResponse.SC_EXPECTATION_FAILED;

        } catch (Exception e) {
            log.error(e.toString(), e);

            resultMsg = e.getMessage();
            responseCode = HttpServletResponse.SC_EXPECTATION_FAILED;

        } finally {
            AppResponse app = new AppResponse(responseCode, resultMsg);
            return app;
        }
    }

	@RequestMapping(value = "power/off/{apiVmName}", method = RequestMethod.GET)
	public String powerOff(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@PathVariable(required=true) String apiVmName) {
		try {
			request.getSession().setAttribute(Constants.VM_SWITCH_HOST_NAME, apiVmName);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {

		}

		return "redirect:/plugin/module/vmswitch";
	}

	@RequestMapping(value = "power/on/{apiVmName}", method = RequestMethod.GET)
	public String powerOn(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@PathVariable(required=true) String apiVmName) {
		try {
			System.out.println("Power ON vm name : [" + apiVmName + "]");

			String ipAddr = getIp(request);
			System.out.println("ACK from IP_addr : [" + ipAddr + "]");

			model.addAttribute("VM_NAME", apiVmName);
			model.addAttribute("IP_ADDR", ipAddr);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
            clearSessionAttr(request);
        }

		return "plugin/module_vm_switch";
	}
}
