package com.cmap.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.annotation.Log;
import com.cmap.i18n.DatabaseMessageSourceBase;

@Controller
@RequestMapping("/i18n")
public class I18nController {
	@Log
	private static Logger log;
	
	@Autowired
	private DatabaseMessageSourceBase databaseMessageSourceBase;
	
	@RequestMapping(value = "/reload", method = RequestMethod.GET)
	public String reloadI18n(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			databaseMessageSourceBase.init();
			
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return null;
	}
}
