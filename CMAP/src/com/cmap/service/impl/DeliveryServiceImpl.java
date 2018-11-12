package com.cmap.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.annotation.Log;
import com.cmap.dao.ScriptInfoDAO;
import com.cmap.dao.vo.ScriptInfoDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.ScriptInfo;
import com.cmap.service.DeliveryService;
import com.cmap.service.vo.DeliveryServiceVO;

@Service("deliveryService")
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
	@Log
	private static Logger log;

	@Autowired
	private ScriptInfoDAO scriptInfoDAO;

	@Override
	public long countDeviceList(DeliveryServiceVO dsVO) throws ServiceLayerException {
		// TODO 自動產生的方法 Stub
		return 0;
	}

	@Override
	public List<DeliveryServiceVO> findDeviceList(DeliveryServiceVO dsVO, Integer startRow, Integer pageLength) throws ServiceLayerException {
		// TODO 自動產生的方法 Stub
		return null;
	}

	@Override
	public long countScriptList(DeliveryServiceVO dsVO) throws ServiceLayerException {
		long retVal = 0;
		try {
			ScriptInfoDAOVO siDAOVO = new ScriptInfoDAOVO();
			BeanUtils.copyProperties(dsVO, siDAOVO);

			retVal = scriptInfoDAO.countScriptInfo(siDAOVO);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢發生異常，請嘗試重新操作");
		}
		return retVal;
	}

	@Override
	public List<DeliveryServiceVO> findScriptList(DeliveryServiceVO dsVO, Integer startRow, Integer pageLength) throws ServiceLayerException {
		List<DeliveryServiceVO> retList = new ArrayList<DeliveryServiceVO>();
		try {
			ScriptInfoDAOVO siDAOVO = new ScriptInfoDAOVO();
			BeanUtils.copyProperties(dsVO, siDAOVO);

			List<ScriptInfo> entities = scriptInfoDAO.findScriptInfo(siDAOVO, startRow, pageLength);

			DeliveryServiceVO vo;
			if (entities != null && !(entities.isEmpty())) {
				for (ScriptInfo entity : entities) {
					vo = new DeliveryServiceVO();
					BeanUtils.copyProperties(entity, vo);
					vo.setScriptTypeName(entity.getScriptType().getScriptTypeName());

					retList.add(vo);
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢發生異常，請嘗試重新操作");
		}
		return retList;
	}

	@Override
	public DeliveryServiceVO getScriptInfoById(String scriptInfoId) throws ServiceLayerException {
		DeliveryServiceVO retVO = new DeliveryServiceVO();
		try {
			ScriptInfoDAOVO siDAOVO = new ScriptInfoDAOVO();
			siDAOVO.setQueryScriptInfoId(scriptInfoId);

			List<ScriptInfo> entities = scriptInfoDAO.findScriptInfo(siDAOVO, null, null);

			if (entities != null && !entities.isEmpty()) {
				ScriptInfo entity = entities.get(0);
				BeanUtils.copyProperties(entity, retVO);
				retVO.setScriptTypeName(entity.getScriptType().getScriptTypeName());

			} else {
				throw new ServiceLayerException("查詢此腳本資料，請重新查詢");
			}

		} catch (ServiceLayerException sle) {
			log.error(sle.toString(), sle);
			throw sle;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢發生異常，請嘗試重新操作");
		}
		return retVO;
	}
}
