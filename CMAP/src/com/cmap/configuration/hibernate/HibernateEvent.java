package com.cmap.configuration.hibernate;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateEvent {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private HibernateEventListener hibernateEventListener;

	@PostConstruct
	public void registerListeners() {
		EventListenerRegistry registry = (
				(SessionFactoryImpl) sessionFactory
				).getServiceRegistry().getService(EventListenerRegistry.class);

		registry.getEventListenerGroup(EventType.POST_COMMIT_INSERT).appendListener(hibernateEventListener);//对实体保存的监听
		registry.getEventListenerGroup(EventType.POST_COMMIT_UPDATE).appendListener(hibernateEventListener);//对实体修改的监听
		registry.getEventListenerGroup(EventType.POST_COMMIT_DELETE).appendListener(hibernateEventListener);//对实体删除的监听
	}
}
