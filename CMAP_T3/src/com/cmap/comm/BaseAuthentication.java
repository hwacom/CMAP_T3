package com.cmap.comm;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.utils.impl.EncryptUtils;

public class BaseAuthentication {

	private static String obtainEncodedUsername(String username) {
		try {
			return Base64.getEncoder().encodeToString(username.getBytes(Constants.CHARSET_UTF8));
		} catch (UnsupportedEncodingException e) {
			return username;
		}
	}

	private static String obtainEncodedPassword(String password) {
		return StringUtils.upperCase(EncryptUtils.getSha256(password));
	}

	public static void setAdminRole2Session(HttpServletRequest request) {
		String currentRoles = (String)request.getSession().getAttribute(Constants.USERROLE);

		if (currentRoles != null) {
			if (currentRoles.indexOf(Constants.USERROLE_ADMIN) == -1) {
				currentRoles = currentRoles.concat(Env.COMM_SEPARATE_SYMBOL).concat(Constants.USERROLE_ADMIN);
				request.getSession().setAttribute(Constants.USERROLE, currentRoles);
			}

		} else {
			request.getSession().setAttribute(Constants.USERROLE, Constants.USERROLE_ADMIN);
		}
	}

	public static void authAdminUser(HttpServletRequest request, String username, String password) {
		String encodedUsername = obtainEncodedUsername(username);
		String encodedPassword = obtainEncodedPassword(password);

		if (StringUtils.equals(encodedUsername, Env.ADMIN_USERNAME)
				&& StringUtils.equals(encodedPassword, Env.ADMIN_PASSWORD)) {

			setAdminRole2Session(request);
			request.getSession().setAttribute(Constants.ISADMIN, true);

		} else {
			request.getSession().setAttribute(Constants.ISADMIN, false);
		}
	}

	public static void authAdminRole(HttpServletRequest request, String username) {
		final List<String> adminRoleUsernames = Env.ADMIN_ROLE_USERNAME;

		if (adminRoleUsernames != null && !adminRoleUsernames.isEmpty()) {
			for (String adminUser : adminRoleUsernames) {
				if (username.equals(adminUser)) {
					setAdminRole2Session(request);
					break;
				}
			}
		}
	}
}
