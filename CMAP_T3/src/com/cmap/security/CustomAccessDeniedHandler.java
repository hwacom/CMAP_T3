package com.cmap.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.cmap.annotation.Log;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Log
    private static Logger log;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.error("[Warning] User: " + auth.getName()
                + " attempted to access the protected URL: "
                + request.getRequestURI());
        }

        response.sendRedirect(request.getContextPath() + "/accessDenied");
    }
}