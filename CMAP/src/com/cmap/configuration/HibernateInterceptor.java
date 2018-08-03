package com.cmap.configuration;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import com.cmap.Constants;
import com.cmap.security.SecurityUtil;

@Component
public class HibernateInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3452575961455943227L;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		try {
			System.out.println("***[HibernateInterceptor]: onSave ***");
			String _user = Constants.SYS;
			try {
				_user = SecurityUtil.getSecurityUser().getUsername();
			} catch (Exception e) {
			}
			
			new PropertyDescriptor("updateBy", entity.getClass()).getWriteMethod().invoke(entity, _user);
			new PropertyDescriptor("updateTime", entity.getClass()).getWriteMethod().invoke(entity, new Timestamp(new Date().getTime()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.onSave(entity, id, state, propertyNames, types);
	}
	
}
