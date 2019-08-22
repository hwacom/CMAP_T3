package com.cmap.ehcache.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cmap.ehcache.model.MessageBean;

//@Repository("messageDAO")
public interface MessageDAO extends JpaRepository<MessageBean, String> {

}
