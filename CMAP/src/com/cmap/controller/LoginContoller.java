package com.cmap.controller;

import java.security.Principal;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmap.annotation.Log;

@Controller
@RequestMapping("/")
public class LoginContoller extends BaseController {
	@Log
	private static Logger log;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (null == principal) {
				return "redirect:/login";
			}

			RequestDispatcher rd = request.getRequestDispatcher("/version/manage");
			rd.forward(request,response);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return null;
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String indexPage(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (null == principal) {
				return "redirect:/login";
			}

			RequestDispatcher rd = request.getRequestDispatcher("/version/manage");
			rd.forward(request,response);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return null;
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String loginPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "langType", defaultValue = "en_US") String langType,
			Locale locale,
			Principal principal,
			Model model) {
		LocaleContextHolder.getLocale();
		return "login";
	}
}
