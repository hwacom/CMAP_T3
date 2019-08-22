package com.cmap.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cmap.PrtgJsonResponse;
import com.cmap.annotation.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/___test___")
public class TestController {
    @Log
    private static Logger log;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String testMain(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

        try {


        } catch (Exception e) {
            log.error(e.toString(), e);
        }

        return "test/test";
    }

    @RequestMapping(value = "/getPrtgJsonResponse", method = {RequestMethod.POST, RequestMethod.GET}, produces="application/json")
    public @ResponseBody String getPrtgJsonResponse(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

        try {
            Map<String, String> vMap = new HashMap<>();
            vMap.put("VM_STATUS", "0");

            PrtgJsonResponse prtgRes = new PrtgJsonResponse(vMap, "0:正常 / 1:SSH不通 / 2:No subscribers");

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(prtgRes);
            System.out.println(jsonString);
            return jsonString;

        } catch (Exception e) {
            log.error(e.toString(), e);
            return null;
        }
    }
}
