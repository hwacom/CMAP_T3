package com.cmap.service;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.SysMailServiceVO;

public interface SysMailService {

    public SysMailServiceVO executeSendMail(String mailListSettingId) throws ServiceLayerException;
}
