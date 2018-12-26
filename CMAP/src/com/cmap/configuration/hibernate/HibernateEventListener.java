package com.cmap.configuration.hibernate;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.event.spi.PostCommitDeleteEventListener;
import org.hibernate.event.spi.PostCommitInsertEventListener;
import org.hibernate.event.spi.PostCommitUpdateEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.cmap.Constants;
import com.cmap.annotation.Log;
import com.cmap.dao.impl.BaseDaoHibernate;
import com.cmap.model.UserOperationLog;

@Component
//@Transactional
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class HibernateEventListener extends BaseDaoHibernate implements PostCommitDeleteEventListener,PostCommitInsertEventListener,PostCommitUpdateEventListener {
	@Log
	private static Logger log;

	/**
	 *
	 */
	private static final long serialVersionUID = -5087485859647703651L;

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		final String updateBy = getUpdateBy(event.getDeletedState(), event.getPersister().getPropertyNames());
		//log.info("***[onPostDelete]: Entity >> " + event.getEntity().getClass().getSimpleName() + ", Id: " + event.getId() + ", updateBy: " + updateBy);

		StringBuffer des = new StringBuffer();//操作描述
		des.append("[DELETE]::{");
		String del = arrayToString(event.getDeletedState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());//判断删除了哪些成员，并进行拼接
		des.append(del);
		des.append("}");

		//log.info(des.toString());
		//log.info("******************************************************************************");

		if (!event.getEntity().getClass().isAssignableFrom(UserOperationLog.class)) { // UserOperationLog 本身異動不作紀錄
			saveLog(event.getSession(), event.getEntity().getClass().getSimpleName(), event.getId(), des, updateBy);
		}
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		final String updateBy = getUpdateBy(event.getState(), event.getPersister().getPropertyNames());
		//log.info("***[onPostInsert]: Entity >> " + event.getEntity().getClass().getSimpleName() + ", Id: " + event.getId() + ", updateBy: " + updateBy);
		/*
		if(!(event.getEntity() instanceof Useroper)){//当是对用户操作表的插入时，不进行操作，否则进入死循环
		 */
		StringBuffer des = new StringBuffer();//操作描述
		des.append("[INSERT]::{");
		String inser = arrayToString(event.getState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());//判断添加的哪些成员，并拼接成字符串
		des.append(inser);
		des.append("}");

		//log.info(des.toString());
		//log.info("******************************************************************************");

		if (!event.getEntity().getClass().isAssignableFrom(UserOperationLog.class)) { // UserOperationLog 本身異動不作紀錄
			saveLog(event.getSession(), event.getEntity().getClass().getSimpleName(), event.getId(), des, updateBy);
		}
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		final String updateBy = getUpdateBy(event.getState(), event.getPersister().getPropertyNames());
		//log.info("***[onPostUpdate]: Entity >> " + event.getEntity().getClass().getSimpleName() + ", Id: " + event.getId() + ", updateBy: " + updateBy);

		StringBuffer des = new StringBuffer();//操作描述
		des.append("[UPDATE]::{");
		String diff = arrayDiff(event.getState(), event.getOldState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());//判断修改了哪些部分，并拼接成字符串
		des.append(diff);
		des.append("}");

		//log.info(des.toString());
		//log.info("******************************************************************************");

		if (!event.getEntity().getClass().isAssignableFrom(UserOperationLog.class)) { // UserOperationLog 本身異動不作紀錄
			saveLog(event.getSession(), event.getEntity().getClass().getSimpleName(), event.getId(), des, updateBy);
		}
	}

	@Override
	public void onPostUpdateCommitFailed(PostUpdateEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostInsertCommitFailed(PostInsertEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostDeleteCommitFailed(PostDeleteEvent event) {
		// TODO Auto-generated method stub

	}

	public void saveLog(Session session, String tableName, Serializable targetId, StringBuffer des, String updateBy){
		UserOperationLog entity = new UserOperationLog();
		entity.setLogId(UUID.randomUUID().toString());
		entity.setUserName(updateBy);
		entity.setTableName(tableName);
		entity.setTargetId(targetId.toString());
		entity.setDescription(des.toString());
		entity.setOperateTime(new Timestamp((new Date()).getTime()));

		Session logSession = session.getSessionFactory().openSession(); //重新打开一个新的Hibernate session，并在使用完进行关闭，不可使用原session。
		logSession.beginTransaction();
		logSession.save(entity);
		logSession.getTransaction().commit();
		logSession.close();
	}

	/**
	 * 数组转字符串
	 * @param o 成员值
	 * @param names 成员名
	 * @param types 成员类型
	 * @return
	 */
	private String arrayToString(Object[] oldEntity, String[] fieldNames, Type[] fieldTypes){
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < oldEntity.length; i++){
			if(fieldTypes[i].isAssociationType()) {
				continue;
			}
			result.append(fieldNames[i] + ":" + oldEntity[i] + ";");
		}
		if(result.toString().equals("")) {
			result.append(";");
		}
		return result.substring(0, result.length()-1);
	}

	/**
	 * 数组不同部分转字符串
	 * @param n 成员新值
	 * @param o 成员原值
	 * @param names 成员名
	 * @param types 成员类型
	 * @return
	 */
	private String arrayDiff(Object[] newEntity, Object[] oldEntity, String[] fieldNames, Type[] fieldTypes){
		StringBuffer result = new StringBuffer();
		//各参数数组均按序传进来的，按index取值即可
		for(int i = 0; i < newEntity.length; i++){
			if(fieldTypes[i].isAssociationType()) {
				continue;
			}
			//如不相等，则加入字符串中
			if(!String.valueOf(newEntity[i]).equals(String.valueOf(oldEntity[i]))){
				result.append(fieldNames[i]+":" + oldEntity[i] + ">" + newEntity[i] + ";");
			}
		}
		return result.substring(0, result.length()-1);
	}

	/**
	 * 取出此次異動的update_by
	 * @param newEntity
	 * @param fieldNames
	 * @return
	 */
	private String getUpdateBy(Object[] newEntity, String[] fieldNames) {
		String updateBy = "unknown";
		for (int i = 0; i < fieldNames.length; i++) {
			if (fieldNames[i].equals(Constants.FIELD_NAME_UPDATE_BY)) {
				updateBy = newEntity[i].toString();
				break;
			}
		}
		return updateBy;
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		// TODO Auto-generated method stub
		return true;
	}
}
