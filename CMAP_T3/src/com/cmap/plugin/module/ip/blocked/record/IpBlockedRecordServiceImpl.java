package com.cmap.plugin.module.ip.blocked.record;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Constants;
import com.cmap.annotation.Log;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DeviceList;
import com.cmap.service.impl.CommonServiceImpl;

@Service("ipRecordService")
@Transactional
public class IpBlockedRecordServiceImpl extends CommonServiceImpl implements IpBlockedRecordService {
    @Log
    private static Logger log;

    @Autowired
    private IpBlockedRecordDAO ipRecordDAO;


    @Override
    public long countModuleBlockedIpList(IpBlockedRecordVO irVO) throws ServiceLayerException {
        long retVal = 0;
        try {
            retVal = ipRecordDAO.countModuleBlockedIpList(irVO);

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("查詢失敗!");
        }
        return retVal;
    }

    @Override
    public List<IpBlockedRecordVO> findModuleBlockedIpList(IpBlockedRecordVO irVO, Integer startRow, Integer pageLength)
            throws ServiceLayerException {
        List<IpBlockedRecordVO> retList = new ArrayList<>();
        try {
            List<Object[]> entities = ipRecordDAO.findModuleBlockedIpList(irVO, startRow, pageLength);

            if (entities == null || (entities != null && entities.isEmpty())) {
                throw new ServiceLayerException("查無資料!");
            }

            ModuleBlockedIpList bilEntity;
            DeviceList dlEntity;
            IpBlockedRecordVO vo;
            for (Object[] entity : entities) {
                bilEntity = (ModuleBlockedIpList)entity[0];
                dlEntity = (DeviceList)entity[1];
                vo = new IpBlockedRecordVO();

                BeanUtils.copyProperties(bilEntity, vo);
                vo.setBlockTimeStr(Constants.FORMAT_YYYYMMDD_HH24MISS.format(bilEntity.getBlockTime()));
                vo.setOpenTimeStr(bilEntity.getOpenTime() != null ? Constants.FORMAT_YYYYMMDD_HH24MISS.format(bilEntity.getOpenTime()) : null);
                vo.setUpdateTimeStr(Constants.FORMAT_YYYYMMDD_HH24MISS.format(bilEntity.getUpdateTime()));
                vo.setGroupName(dlEntity.getGroupName());
                vo.setStatusFlag(StringUtils.equals(bilEntity.getStatusFlag(), "B")
                                    ? "B-封鎖"
                                    : StringUtils.equals(bilEntity.getStatusFlag(), "O") ? "O-開通" : "N/A");

                retList.add(vo);
            }

        } catch (ServiceLayerException sle) {
            log.error(sle.toString(), sle);
            throw sle;

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("查詢失敗!");
        }
        return retList;
    }
}
