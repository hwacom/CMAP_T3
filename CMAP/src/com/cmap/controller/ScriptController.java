package com.cmap.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.annotation.Log;

@Controller
@RequestMapping("/script")
public class ScriptController extends BaseController {
	@Log
	private static Logger log;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String main(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		
		return "script/script_main";
	}
}
