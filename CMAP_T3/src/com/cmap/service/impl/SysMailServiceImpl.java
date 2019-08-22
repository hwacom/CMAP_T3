package com.cmap.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.SysMailDAO;
import com.cmap.dao.vo.SysMailDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.service.SysMailService;
import com.cmap.service.impl.jobs.BaseJobImpl.Result;
import com.cmap.service.vo.SysMailServiceVO;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Service("sysMailService")
@Transactional
public class SysMailServiceImpl extends CommonServiceImpl implements SysMailService {
    @Log
    private static Logger log;

    @Autowired
    private SysMailDAO sysMailDAO;

    @Override
    public SysMailServiceVO executeSendMail(String mailListSettingId) throws ServiceLayerException {
        SysMailServiceVO retVO = new SysMailServiceVO();
        boolean success = false;
        String settingCode = null;
        try {
            // Step 1. 查找ID設定
            SysMailDAOVO mailListVO = sysMailDAO.getMailListSettingBySettingIdAndCode(mailListSettingId, null);

            if (mailListVO == null) {
                throw new ServiceLayerException("查無[SYS_MAIL_LIST_SETTING]設定 >> settingId: " + mailListSettingId);
            }

            settingCode = mailListVO.getSettingCode();
            final String[] mailTo = mailListVO.getMailTo();
            final String[] mailCc = mailListVO.getMailCc();
            final String[] mailBcc = mailListVO.getMailBcc();
            final String subject = mailListVO.getSubject();

            SysMailDAOVO mailContentVO = sysMailDAO.getMailContentSettingBySettingIdAndCode(null, settingCode);

            if (mailContentVO == null) {
                throw new ServiceLayerException("查無[SYS_MAIL_CONTENT_SETTING]設定 >> settingCode: " + settingCode);
            }

            final String mailContent = mailContentVO.getMailContent();

            // Step 2. 呼叫共用發信
            final int retryTimes = StringUtils.isNotBlank(Env.RETRY_TIMES) ? Integer.parseInt(Env.RETRY_TIMES) : 1;
            int round = 0;

            while (round < retryTimes) {
                try {
                    sendHtmlEmail(mailTo, mailCc, mailBcc, subject, mailContent);
                    success = true;
                    break;

                } catch (ServiceLayerException sle) {
                    round++;

                } catch (Exception e) {
                    round++;
                    log.error(e.toString(), e);
                }
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("寄發MAIL失敗 (" + e.getMessage() + ")");

        } finally {
            retVO.setJobExcuteResult(success ? Result.SUCCESS : Result.FAILED);
            retVO.setJobExcuteResultRecords(success ? "1" : "0");
            retVO.setJobExcuteRemark(settingCode);
        }
        return retVO;
    }
}
