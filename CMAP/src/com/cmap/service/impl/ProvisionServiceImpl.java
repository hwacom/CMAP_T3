package com.cmap.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.annotation.Log;
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.ProvisionLogDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DeviceList;
import com.cmap.model.ProvisionLogDevice;
import com.cmap.model.ProvisionLogMaster;
import com.cmap.model.ProvisionLogRetry;
import com.cmap.model.ProvisionLogStep;
import com.cmap.service.ProvisionService;
import com.cmap.service.vo.ProvisionServiceVO;
import com.cmap.utils.impl.CommonUtils;

@Service("provisionService")
@Transactional
public class ProvisionServiceImpl implements ProvisionService {
	@Log
	private static Logger log;

	@Autowired
	private ProvisionLogDAO provisionLogDAO;

	@Autowired
	private DeviceListDAO deviceListDAO;

	private ProvisionServiceVO transMasterEntity2VO(ProvisionLogMaster entity) {
		ProvisionServiceVO psVO = new ProvisionServiceVO();

		return psVO;
	}

	@Override
	public List<ProvisionServiceVO> findProvisionLog(ProvisionServiceVO psVO) throws ServiceLayerException {
		List<ProvisionServiceVO> retList = new ArrayList<ProvisionServiceVO>();

		try {
			List<ProvisionLogMaster> entities = provisionLogDAO.findProvisionLogByDAOVO(null);

			if (entities != null && !entities.isEmpty()) {
				ProvisionServiceVO retPsVO;
				for (ProvisionLogMaster entity : entities) {
					retPsVO = transMasterEntity2VO(entity);
					retList.add(retPsVO);
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e);
		}

		return retList;
	}

	@Override
	public boolean insertProvisionLog(ProvisionServiceVO psVO) throws ServiceLayerException {
		try {
			ProvisionLogMaster masterEntity = null;
			List<ProvisionLogStep> stepEntities = null;
			List<ProvisionLogDevice> deviceEntities = null;
			List<ProvisionLogRetry> retryEntities = null;

			ProvisionServiceVO masterVO = psVO;
			if (masterVO != null) {
				final String userName = masterVO.getUserName();
				final String logMasterId = UUID.randomUUID().toString();
				final String logStepId = UUID.randomUUID().toString();

				/*
				 * Convert ProvisionLogMaster entity
				 */
				masterEntity = new ProvisionLogMaster();
				BeanUtils.copyProperties(masterVO, masterEntity);
				masterEntity.setLogMasterId(logMasterId);
				masterEntity.setSpendTimeInSeconds(
						CommonUtils.calculateSpendTime(masterVO.getBeginTime(), masterVO.getEndTime()));
				masterEntity.setBeginTime(new Timestamp(masterVO.getBeginTime().getTime()));
				masterEntity.setEndTime(new Timestamp(masterVO.getEndTime().getTime()));
				masterEntity.setCreateBy(userName);
				masterEntity.setCreateTime(new Timestamp((new Date()).getTime()));

				/*
				 * Convert ProvisionLogStep entities
				 */
				List<ProvisionServiceVO> stepVOList = masterVO.getStepVO();
				if (stepVOList != null && !stepVOList.isEmpty()) {
					stepEntities = new ArrayList<ProvisionLogStep>();

					ProvisionLogStep stepEntity = null;
					for (ProvisionServiceVO stepVO : stepVOList) {
						stepEntity = new ProvisionLogStep();
						BeanUtils.copyProperties(stepVO, stepEntity);
						stepEntity.setLogStepId(logStepId);
						stepEntity.setProvisionLogMaster(masterEntity);
						stepEntity.setSpendTimeInSeconds(
							CommonUtils.calculateSpendTime(stepVO.getBeginTime(), stepVO.getEndTime()));
						stepEntity.setBeginTime(new Timestamp(stepVO.getBeginTime().getTime()));
						stepEntity.setEndTime(new Timestamp(stepVO.getEndTime().getTime()));
						stepEntity.setCreateBy(userName);
						stepEntity.setCreateTime(new Timestamp((new Date()).getTime()));

						stepEntities.add(stepEntity);

						/*
						 * Convert ProvisionLogDevice entities
						 */
						List<ProvisionServiceVO> deviceVOList = stepVO.getDeviceVO();
						if (deviceVOList != null && !deviceVOList.isEmpty()) {
							deviceEntities = new ArrayList<ProvisionLogDevice>();

							DeviceList deviceListEntity = null;
							ProvisionLogDevice deviceEntity = null;
							for (ProvisionServiceVO deviceVO : deviceVOList) {
								deviceEntity = new ProvisionLogDevice();
								BeanUtils.copyProperties(deviceVO, deviceEntity);
								deviceEntity.setLogDeviceId(UUID.randomUUID().toString());
								deviceEntity.setProvisionLogStep(stepEntity);

								deviceListEntity = deviceListDAO.findDeviceListByDeviceListId(deviceVO.getDeviceListId());
								deviceEntity.setDeviceList(deviceListEntity);

								deviceEntity.setCreateBy(userName);
								deviceEntity.setCreateTime(new Timestamp((new Date()).getTime()));

								deviceEntities.add(deviceEntity);
							}
						}

						/*
						 * Convert ProvisionLogRetry entities
						 */
						List<ProvisionServiceVO> retryVOList = stepVO.getRetryVO();
						if (retryVOList != null && !retryVOList.isEmpty()) {
							retryEntities = new ArrayList<ProvisionLogRetry>();

							ProvisionLogRetry retryEntity = null;
							for (ProvisionServiceVO retryVO : retryVOList) {
								retryEntity = new ProvisionLogRetry();
								BeanUtils.copyProperties(retryVO, retryEntity);
								retryEntity.setLogRetryId(UUID.randomUUID().toString());
								retryEntity.setProvisionLogStep(stepEntity);
								retryEntity.setCreateBy(userName);
								retryEntity.setCreateTime(new Timestamp((new Date()).getTime()));
								retryEntities.add(retryEntity);
							}
						}
					}
				}
			}

			provisionLogDAO.insertProvisionLog(masterEntity, stepEntities, deviceEntities, retryEntities);

		} catch (Exception e) {
			log.error(e.toString(), e);
			return false;
		}

		return true;
	}
}
