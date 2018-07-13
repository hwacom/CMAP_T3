package com.cmap.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.ConfigVersionInfoDAO;
import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.model.ConfigVersionInfo;
import com.cmap.security.SecurityUtil;
import com.cmap.service.CommonService;
import com.cmap.service.VersionService;
import com.cmap.service.vo.VersionServiceVO;

@Service("versionService")
@Transactional
public class VersionServiceImpl implements VersionService {
	private static Log log = LogFactory.getLog(VersionServiceImpl.class);

	@Autowired
	private ConfigVersionInfoDAO configVersionInfoDAO;

	@Override
	public long countUserPermissionAllVersionInfo(List<String> groupList, List<String> deviceList) {
		long retCount = 0;
		ConfigVersionInfoDAOVO cviDAOVO;
		try {
			cviDAOVO = new ConfigVersionInfoDAOVO();
			cviDAOVO.setQueryGroup1List(groupList);
			cviDAOVO.setQueryDevice1List(deviceList);
			
			retCount = configVersionInfoDAO.countConfigVersionInfoByDAOVO(cviDAOVO);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
		
		return retCount;
	}
	
	@Override
	public long countVersionInfo(VersionServiceVO vsVO) {
		long retCount = 0;
		ConfigVersionInfoDAOVO cviDAOVO;
		try {
			cviDAOVO = transServiceVO2DAOVO(vsVO);
			
			retCount = configVersionInfoDAO.countConfigVersionInfoByDAOVO(cviDAOVO);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
		
		return retCount;
	}
	
	@Override
	public List<VersionServiceVO> findVersionInfo(VersionServiceVO vsVO, Integer startRow, Integer pageLength) {
		List<VersionServiceVO> retList = new ArrayList<VersionServiceVO>();
		List<ConfigVersionInfo> modelList;
		ConfigVersionInfoDAOVO cviDAOVO;
		try {
			cviDAOVO = transServiceVO2DAOVO(vsVO);
			
			modelList = configVersionInfoDAO.findConfigVersionInfoByDAOVO(cviDAOVO, startRow, pageLength);
			
			if (modelList != null && !modelList.isEmpty()) {
				retList = transModel2ServiceVO(modelList);
			}
		
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
		return retList;
	}
	
	@Override
	public boolean deleteVersionInfo(List<String> versionIDs) {
		try {
			configVersionInfoDAO.deleteConfigVersionInfoByVersionIds(versionIDs, SecurityUtil.getSecurityUser().getUsername());
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
			return false;
		}
		return true;
	}

	private ConfigVersionInfoDAOVO transServiceVO2DAOVO(VersionServiceVO vsVO) {
		ConfigVersionInfoDAOVO cviVO = new ConfigVersionInfoDAOVO();
		BeanUtils.copyProperties(vsVO, cviVO);
		return cviVO;
	}
	
	private List<VersionServiceVO> transModel2ServiceVO(List<ConfigVersionInfo> modelList) {
		List<VersionServiceVO> retList = new ArrayList<VersionServiceVO>();
		
		VersionServiceVO vsVO;
		for (ConfigVersionInfo cvi : modelList) {
			vsVO = new VersionServiceVO();
			BeanUtils.copyProperties(cvi, vsVO);
			vsVO.setBackupTimeStr(CommonService.FORMAT_YYYYMMDD_HH24MI.format(cvi.getCreateTime()));
			
			retList.add(vsVO);
		}
		
		return retList;
	}
}
