package com.cmap.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmap.i18n.DatabaseMessageSourceBase;

@Controller
@RequestMapping("/i18n")
public class I18nController {
	private static Log log = LogFactory.getLog(I18nController.class);
	
	@Autowired
	private DatabaseMessageSourceBase databaseMessageSourceBase;
	
	@RequestMapping(value = "/reload", method = RequestMethod.GET)
	public String reloadI18n(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		try {
			databaseMessageSourceBase.init();
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
		}
		return null;
	}
}
