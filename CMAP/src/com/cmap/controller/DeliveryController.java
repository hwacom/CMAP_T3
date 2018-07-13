package com.cmap.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.service.DeliveryService;

@Controller
@RequestMapping("/delivery")
public class DeliveryController extends BaseController {
	private static Log log = LogFactory.getLog(DeliveryController.class);

	@Autowired
	private DeliveryService deliveryService;
	
	private void initMenu(Model model, HttpServletRequest request) {
		Map<String, String> groupListMap = null;
		Map<String, String> deviceListMap = null;
		try {
			groupListMap = getGroupList(request);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
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
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			
		} finally {
			initMenu(model, request);
		}
		
		return "delivery/delivery_main";
	}
}
