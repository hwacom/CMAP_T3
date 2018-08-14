package com.cmap.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/script")
public class ScriptController extends BaseController {
	private static Logger log = LoggerFactory.getLogger(ScriptController.class);

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String main(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		
		return "script/script_main";
	}
}
