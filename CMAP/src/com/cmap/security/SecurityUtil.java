package com.cmap.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

	public static SecurityUser getSecurityUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof UserDetails) {
            return (SecurityUser) object;
        } else {
            return null;
        }
    }
}
