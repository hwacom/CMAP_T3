package com.cmap.ehcache.service;

import java.util.List;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import com.cmap.ehcache.dao.MessageDAO;
import com.cmap.ehcache.model.MessageBean;

//@CacheConfig(cacheNames = "MessageBean")
//@Service
public class MessageCacheService {

    //@Autowired
    private MessageDAO messageDAO;

    //@Autowired
    private CacheManager cacheManager;

    /**
     * 新增用户
     */
    //@CachePut(key = "#messageBean.id")
    public MessageBean insertMessage(MessageBean messageBean) {
        System.out.println("\n----------正在執行insertMessage()----------");
        messageBean = this.messageDAO.save(messageBean);
        return messageBean;
    }

    /**
     * 查詢用户
     */
    public List<MessageBean> findAll() {
        System.out.println("\n----------正在執行findAll()----------");
        return this.messageDAO.findAll();
    }

    /**
     * 經由id查詢單個用户
     */
    public MessageBean findMessageById(String id) {
        System.out.println("\n----------正在執行findMessageById()----------");
        MessageBean messageBean = this.messageDAO.findById(id).get();
        return messageBean;
    }

    /**
     * 修改單個用户
     */
    //@CachePut(key = "#messageBean.id")
    public MessageBean updateMessage(MessageBean messageBean) {
        System.out.println("\n----------正在執行updateMessage()----------");
        return this.messageDAO.save(messageBean);
    }

    /**
     * 經由id刪除單個用户
     */
    //@CacheEvict(key = "#id")
    public void deleteMessageById(String id) {
        System.out.println("\n----------正在執行deleteMessageById()----------");
        this.messageDAO.deleteById(id);
    }

    /**
     *  刪除單個用户
     * @param messageBean
     */
    //@CacheEvict(key = "#messageBean.id")
    public void deleteMessage(MessageBean messageBean) {
        System.out.println("\n----------正在執行deleteMessage()----------");
        this.messageDAO.delete(messageBean);
    }

    /**
     *  刪除多個用户
     * @param messageBean
     */
    public void deleteMessages(List<MessageBean> users) {
        System.out.println("\n----------正在執行deleteMessages()----------");

        // 透過cacheManager移除快取物件
        Cache cache = cacheManager.getCache("MessageBean");
        for (MessageBean messageBean : users) {
            cache.evict(messageBean.getId());
        }

        this.messageDAO.deleteAll(users);
    }
}
