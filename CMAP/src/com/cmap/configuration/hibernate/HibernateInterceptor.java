package com.cmap.configuration.hibernate;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.cmap.Constants;
import com.cmap.annotation.Log;
import com.cmap.security.SecurityUtil;

@Component
public class HibernateInterceptor extends EmptyInterceptor {
	@Log
	private static Logger log;

	/**
	 *
	 */
	private static final long serialVersionUID = -3452575961455943227L;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		try {
			//log.info("***[HibernateInterceptor]: onSave ***");
			String _user = Constants.SYS;
			try {
				_user = SecurityUtil.getSecurityUser().getUsername();
			} catch (Exception e) {
			}

			try {
				new PropertyDescriptor("updateBy", entity.getClass()).getWriteMethod().invoke(entity, _user);
				new PropertyDescriptor("updateTime", entity.getClass()).getWriteMethod().invoke(entity, new Timestamp(new Date().getTime()));
			} catch (IntrospectionException ie) {
				// Method not found >> 不處理
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return super.onSave(entity, id, state, propertyNames, types);
	}

}
