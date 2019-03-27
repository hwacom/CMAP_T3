package com.cmap.configuration.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionListener implements HttpSessionListener {
	private static Logger log = LoggerFactory.getLogger(SessionListener.class);

	@Override
    public void sessionCreated(HttpSessionEvent event) {
        log.info("session created");
        event.getSession().setMaxInactiveInterval(3600);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    	log.info("session destroyed");
    }
}
