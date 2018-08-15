package com.cmap.configuration.hibernate;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.event.spi.PostCommitDeleteEventListener;
import org.hibernate.event.spi.PostCommitInsertEventListener;
import org.hibernate.event.spi.PostCommitUpdateEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateEventListener implements PostCommitDeleteEventListener,PostCommitInsertEventListener,PostCommitUpdateEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5087485859647703651L;
	
	@Autowired
    private HttpSession httpSession;

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		System.out.println("***[onPostDelete]: Entity >> " + event.getEntity().getClass().getSimpleName() + ", Id: " + event.getId());
		
		StringBuffer des = new StringBuffer();//操作描述
        des.append("删除操作，删除内容{");
        String del = arrayToString(event.getDeletedState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());//判断删除了哪些成员，并进行拼接
        des.append(del);
        des.append("}");
        
        System.out.println(des);
        System.out.println("******************************************************************************");
        
        /*
        saveLog(event.getSession(), event.getEntity().getClass().getSimpleName(), event.getId(), des);
        */
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		System.out.println("***[onPostInsert]: Entity >> " + event.getEntity().getClass().getSimpleName() + ", Id: " + event.getId());
		/*
		if(!(event.getEntity() instanceof Useroper)){//当是对用户操作表的插入时，不进行操作，否则进入死循环
		*/
            StringBuffer des = new StringBuffer();//操作描述
            des.append("新建操作，新建内容{");
            String inser = arrayToString(event.getState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());//判断添加的哪些成员，并拼接成字符串
            des.append(inser);
            des.append("}");
            
            System.out.println(des);
        	System.out.println("******************************************************************************");
        
        /*
            saveLog(event.getSession(), event.getEntity().getClass().getSimpleName(), event.getId(), des);
        }
        */
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		System.out.println("***[onPostUpdate]: Entity >> " + event.getEntity().getClass().getSimpleName() + ", Id: " + event.getId());
		
		StringBuffer des = new StringBuffer();//操作描述
        des.append("更新操作，更新内容{");
        String diff = arrayDiff(event.getState(), event.getOldState(), event.getPersister().getPropertyNames(), event.getPersister().getPropertyTypes());//判断修改了哪些部分，并拼接成字符串
        des.append(diff);
        des.append("}");
        
        System.out.println(des);
        System.out.println("******************************************************************************");
        
        /*
        saveLog(event.getSession(), event.getEntity().getClass().getSimpleName(), event.getId(), des);
        */
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

	private void saveLog(Session session, String tableName, Serializable targetId, StringBuffer des){
		/*
        int currUserId = (int) httpSession.getAttribute(Constant.CURRENT_USERID);//获取操作人
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);//操作日期
        String sql = "INSERT useroper(UserID,TableName,TargetID,OperDesc,OperTime) VALUES("+currUserId+",'"+tableName+"',"+targetId+",'"+des.toString()+"','"+time+"')";
        Session logSession = session.getSessionFactory().openSession();//重新打开一个新的Hibernate session，并在使用完进行关闭，不可使用原session。
        logSession.createSQLQuery(sql).executeUpdate();
        logSession.close();
        */
    }
	
	/**
     * 数组转字符串
     * @param o 成员值
     * @param names 成员名
     * @param types 成员类型
     * @return
     */
    private String arrayToString(Object[] o, String[] names, Type[] types){
        StringBuffer result = new StringBuffer();
        for(int i=0;i<o.length;i++){
            if(types[i].isAssociationType())//外键忽略处理
                continue;
            result.append(names[i]+":"+o[i]+";");
        }
        if(result.toString().equals(""))
            result.append(";");
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
    private String arrayDiff(Object[] n, Object[] o, String[] names, Type[] types){
        StringBuffer result = new StringBuffer();
        //各参数数组均按序传进来的，按index取值即可
        for(int i=0;i<n.length;i++){
            if(types[i].isAssociationType())//外键忽略处理
                continue;
            //如不相等，则加入字符串中
            if(!String.valueOf(n[i]).equals(String.valueOf(o[i]))){
                result.append(names[i]+":"+o[i]+">"+n[i]+";");
            }
        }
        return result.substring(0, result.length()-1);
    }
    
    @Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		// TODO Auto-generated method stub
		return true;
	}
}
