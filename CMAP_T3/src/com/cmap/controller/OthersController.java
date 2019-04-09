package com.cmap.controller;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.cmap.annotation.Log;
import com.cmap.security.SecurityUtil;

@Controller
@RequestMapping("/others")
public class OthersController {
	@Log
	private static Logger log;

	private void initMenu(Model model, HttpServletRequest request) {
		try {

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			model.addAttribute("userInfo", SecurityUtil.getSecurityUser().getUsername());
		}
	}

	@RequestMapping(value = "sys/{sysCode}", method = RequestMethod.GET)
	public String main(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response,
	        @PathVariable(required=true) String sysCode) {
		try {


		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			initMenu(model, request);
		}

		return "others/jumpTo";
	}
}
