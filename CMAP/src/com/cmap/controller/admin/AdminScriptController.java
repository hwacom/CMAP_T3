package com.cmap.controller.admin;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.annotation.Log;
import com.cmap.controller.BaseController;

@Controller
@RequestMapping("/admin/script")
public class AdminScriptController extends BaseController {
	@Log
	private static Logger log;

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String adminScript(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			
		} catch (Exception e) {
			log.error(e.toString(), e);
			
		} finally {
		}
		
		return "admin/admin_script";
	}
}
