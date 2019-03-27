package com.cmap.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.cmap.Constants;

public class AuthUnsuccessHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		final Exception error = (Exception)request.getSession().getAttribute(Constants.ERROR);

		if (error != null) {
			request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, error);

		} else {
			request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
		}

		request.getRequestDispatcher("login").forward(request,response);
	}

}
