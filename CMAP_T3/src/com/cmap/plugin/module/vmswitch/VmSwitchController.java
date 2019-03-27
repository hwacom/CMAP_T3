package com.cmap.plugin.module.vmswitch;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;

@Controller
@RequestMapping("/plugin/module/vmswitch")
public class VmSwitchController extends BaseController {
	@Log
	private static Logger log;

	@Autowired
	private VmSwitchService vmSwitchService;

	@RequestMapping(value = "power/off/{apiVmName}", method = RequestMethod.GET)
	public String powerOff(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
			@PathVariable(required=true) String apiVmName) {
		try {
			System.out.println("Power OFF vm name : [" + apiVmName + "]");

			String ipAddr = getIp(request);
			System.out.println("ACK from IP_addr : [" + ipAddr + "]");

			VmSwitchVO vmSwitchVO = new VmSwitchVO();
			vmSwitchVO.setApiVmName(apiVmName);
			vmSwitchService.powerOff(vmSwitchVO);

			model.addAttribute("VM_NAME", apiVmName);
			model.addAttribute("IP_ADDR", ipAddr);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return "plugin/module_vm_switch";
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
		}

		return "plugin/module_vm_switch";
	}
}
