package com.cmap.plugin.module.wifipollrer;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;
import com.cmap.security.SecurityUtil;

@Controller
@RequestMapping("/plugin/module/wifiPoller")
public class WifiPollerController extends BaseController {
	@Log
	private static Logger log;

	/**
	 * 初始化選單
	 * @param model
	 * @param request
	 */
	private void initMenu(Model model, HttpServletRequest request) {
		Map<String, String> groupListMap = null;
		Map<String, String> deviceListMap = null;
		try {
			groupListMap = getGroupList(request);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			model.addAttribute("queryGroup", "");
			model.addAttribute("groupList", groupListMap);
			model.addAttribute("queryDevice", "");
			model.addAttribute("deviceList", deviceListMap);

			model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
		}
	}


	@RequestMapping(value = "", method = RequestMethod.GET)
	public String wifiPoller(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}
		return "plugin/module_wifi_poller";
	}

}
