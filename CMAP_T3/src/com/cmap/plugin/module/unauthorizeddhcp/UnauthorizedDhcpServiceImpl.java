package com.cmap.plugin.module.unauthorizeddhcp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmap.annotation.Log;
import com.cmap.exception.ServiceLayerException;
import com.cmap.plugin.module.ip.mapping.IpMappingServiceVO;

@Service("unauthorizedDhcpService")
public class UnauthorizedDhcpServiceImpl implements UnauthorizedDhcpService {
	@Log
    private static Logger log;
	 
	@Autowired
	private UnauthorizedDhcpDAO unauthorizedDhcpDAO;
	
	@Override
	public long countUnauthorizedDhcpRecord(UnauthorizedDhcpServiceVO udsVO) throws ServiceLayerException {
		long retVal = 0;
		try {
			retVal = unauthorizedDhcpDAO.countUnauthorizedDhcpRecord(udsVO);
			
		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢異常，請重新操作");
		}
		return retVal;
	}

	@Override
	public List<UnauthorizedDhcpServiceVO> findUnauthorizedDhcpRecord(UnauthorizedDhcpServiceVO udsVO)
			throws ServiceLayerException {
		List<UnauthorizedDhcpServiceVO> retList = new ArrayList<>();
		try {
			int startRow = udsVO.getStartNum();
        	int pageLength = udsVO.getPageLength();
			List<Object[]> objList = unauthorizedDhcpDAO.findUnauthorizedDhcpRecord(udsVO, startRow, pageLength);
			
			if (objList != null && !objList.isEmpty()) {
				UnauthorizedDhcpServiceVO vo;
        		for (Object[] obj : objList) {
        			vo = new UnauthorizedDhcpServiceVO();
        			
        			// 轉換 SQL Date & Time
        			java.sql.Date date = (java.sql.Date)obj[0];
        			java.sql.Time time = (java.sql.Time)obj[1];
        			
        			Calendar dateCal = Calendar.getInstance();
        			dateCal.setTime(date);
        			
        			Calendar timeCal = Calendar.getInstance();
        			timeCal.setTime(time);
        			
        			dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        			dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        			dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
        			
        			SimpleDateFormat FORMAT_YYYYMMDD_HH24MISS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        			vo.setDateTime(FORMAT_YYYYMMDD_HH24MISS.format(dateCal.getTime()));
        			vo.setGroupId(Objects.toString(obj[2]));
        			vo.setGroupName(Objects.toString(obj[3]));
        			vo.setDeviceId(Objects.toString(obj[4]));
        			vo.setDeviceName(Objects.toString(obj[5]));
        			vo.setPortId(Objects.toString(obj[6]));
        			vo.setPortName(Objects.toString(obj[7]));
        			retList.add(vo);
        		}
        	}
			
		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢異常，請重新操作");
		}
		return retList;
	}

}
