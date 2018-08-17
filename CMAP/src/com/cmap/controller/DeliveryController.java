package com.cmap.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.annotation.Log;
import com.cmap.service.DeliveryService;

@Controller
@RequestMapping("/delivery")
public class DeliveryController extends BaseController {
	@Log
	private static Logger log;

	@Autowired
	private DeliveryService deliveryService;

	private void initMenu(Model model, HttpServletRequest request) {
		Map<String, String> groupListMap = null;
		Map<String, String> deviceListMap = null;
		try {
			groupListMap = getGroupList(request);

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			model.addAttribute("group", "");
			model.addAttribute("groupList", groupListMap);

			model.addAttribute("device", "");
			model.addAttribute("deviceList", deviceListMap);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String main(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {

		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}

		return "delivery/delivery_main";
	}
}
