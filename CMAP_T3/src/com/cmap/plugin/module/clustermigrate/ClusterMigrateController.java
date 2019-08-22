package com.cmap.plugin.module.clustermigrate;

import java.security.Principal;
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
import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;
import com.cmap.exception.ServiceLayerException;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/plugin/module/clustermigrate")
public class ClusterMigrateController extends BaseController {
    @Log
    private static Logger log;

    @Autowired
    private ClusterMigrateService clusterMigrateService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String main(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {


        } catch (Exception e) {
            log.error(e.toString(), e);
        }

        return "plugin/module_cluster_migrate";
    }

    @RequestMapping(value = "setting/{migrateClusterName}", method = RequestMethod.GET)
    public @ResponseBody String migrateSetting(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
            @PathVariable(required=true) String migrateClusterName) {
        String retVal = "READY_OK";
        try {
            clusterMigrateService.settingMigrate(migrateClusterName, true);

        } catch (Exception e) {
            log.error(e.toString(), e);
            retVal = "ERROR";
        }

        return retVal;
    }

    @RequestMapping(value = "/goTest", method = RequestMethod.GET, produces="application/json;odata=verbose")
    public String goTest(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

        ClusterMigrateVO retVO = null;
        try {
            retVO = clusterMigrateService.doClusterMigrate(null);
            System.out.println("Process result flag: " + retVO.getProcessResultFlag());
            System.out.println("Process result msg: " + retVO.getProcessResultMsg());
            System.out.println("Process remark: " + retVO.getProcessRemark());

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return "plugin/module_cluster_migrate";
    }

    @RequestMapping(value = "/service/restart/{serviceName}", method = RequestMethod.GET)
    public @ResponseBody String serviceRestart(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
            @PathVariable(required=true) String serviceName) {

        ClusterMigrateVO retVO = null;
        try {
            retVO = clusterMigrateService.doServiceRestart(serviceName);
            System.out.println("Process result flag: " + retVO.getProcessResultFlag());
            System.out.println("Process result msg: " + retVO.getProcessResultMsg());
            System.out.println("Process remark: " + retVO.getProcessRemark());

        } catch (Exception e) {
            log.error(e.toString(), e);
            return "E";
        }
        return retVO.getProcessResultFlag();
    }

    @RequestMapping(value = "/cluster/migrate/{migrateClusterName}", method = RequestMethod.GET)
    public @ResponseBody String clusterMigrate(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
            @PathVariable(required=true) String migrateClusterName) {

        ClusterMigrateVO retVO = null;
        try {
            retVO = clusterMigrateService.doClusterMigrateImmediately(migrateClusterName);
            System.out.println("Process result flag: " + retVO.getProcessResultFlag());
            System.out.println("Process result msg: " + retVO.getProcessResultMsg());
            System.out.println("Process remark: " + retVO.getProcessRemark());

        } catch (Exception e) {
            return "E";
        }
        return retVO.getProcessResultFlag();
    }

    @RequestMapping(value = "/server/reboot/{rebootServerName}", method = RequestMethod.GET)
    public @ResponseBody String rebootServer(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
            @PathVariable(required=true) String rebootServerName) {

        ClusterMigrateVO retVO = null;
        try {
            retVO = clusterMigrateService.doReboot(rebootServerName);
            System.out.println("Process result flag: " + retVO.getProcessResultFlag());
            System.out.println("Process result msg: " + retVO.getProcessResultMsg());
            System.out.println("Process remark: " + retVO.getProcessRemark());

        } catch (Exception e) {
            return "E";
        }
        return retVO.getProcessResultFlag();
    }

    @RequestMapping(value = "/go", method = RequestMethod.POST, produces="application/json;odata=verbose")
    public @ResponseBody AppResponse go(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
            @RequestBody JsonNode jsonData) {

        int responseCode = HttpServletResponse.SC_OK;
        String responseMsg = "切換流程正常";
        ClusterMigrateVO retVO = null;
        try {
            JsonNode logIdNode = jsonData.findValue("logId");

            Integer logId = null;

            if (logIdNode != null) {
                logId = Integer.parseInt(logIdNode.asText());
            }

            retVO = clusterMigrateService.doClusterMigrate(logId);

        } catch (ServiceLayerException sle) {
            responseCode = HttpServletResponse.SC_EXPECTATION_FAILED;
            responseMsg = sle.getMessage();

        } catch (Exception e) {
            log.error(e.toString(), e);

            responseCode = HttpServletResponse.SC_EXPECTATION_FAILED;
            responseMsg = e.getMessage();

        } finally {
            AppResponse app = new AppResponse(responseCode, responseMsg);

            if (retVO != null) {
                app.putData("PROCESS_RESULT_FLAG", retVO.getProcessResultFlag());
                app.putData("PROCESS_RESULT_MSG", retVO.getProcessResultMsg());
                app.putData("PROCESS_MSG", retVO.getProcessRemark());
            }

            return app;
        }
    }
}
