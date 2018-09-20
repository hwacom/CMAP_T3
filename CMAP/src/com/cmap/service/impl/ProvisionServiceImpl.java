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
import com.cmap.model.ProvisionLogDetail;
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

	private ProvisionServiceVO transMasterEntity2VO(ProvisionLogDetail entity) {
		ProvisionServiceVO psVO = new ProvisionServiceVO();

		return psVO;
	}

	@Override
	public List<ProvisionServiceVO> findProvisionLog(ProvisionServiceVO psVO) throws ServiceLayerException {
		List<ProvisionServiceVO> retList = new ArrayList<ProvisionServiceVO>();

		try {
			List<ProvisionLogDetail> entities = provisionLogDAO.findProvisionLogByDAOVO(null);

			if (entities != null && !entities.isEmpty()) {
				ProvisionServiceVO retPsVO;
				for (ProvisionLogDetail entity : entities) {
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
	public boolean insertProvisionLog(ProvisionServiceVO masterVO) throws ServiceLayerException {
		try {
			ProvisionLogMaster masterEntity = null;
			List<ProvisionLogDetail> detailEntities = null;
			List<ProvisionLogStep> stepEntities = null;
			List<ProvisionLogDevice> deviceEntities = null;
			List<ProvisionLogRetry> retryEntities = null;

			if (masterVO != null) {
				final String logMasterId = UUID.randomUUID().toString();
				final String userName = masterVO.getUserName();

				/*
				 * Convert ProvisionLogMaster entity
				 */
				masterEntity = new ProvisionLogMaster();
				BeanUtils.copyProperties(masterVO, masterEntity);
				masterEntity.setBeginTime(new Timestamp(masterVO.getBeginTime().getTime()));
				masterEntity.setEndTime(new Timestamp(masterVO.getEndTime().getTime()));
				masterEntity.setLogMasterId(logMasterId);
				masterEntity.setSpendTimeInSeconds(
						CommonUtils.calculateSpendTime(masterVO.getBeginTime(), masterVO.getEndTime()));
				masterEntity.setCreateBy(userName);
				masterEntity.setCreateTime(new Timestamp((new Date()).getTime()));

				detailEntities = new ArrayList<ProvisionLogDetail>();
				stepEntities = new ArrayList<ProvisionLogStep>();
				deviceEntities = new ArrayList<ProvisionLogDevice>();
				retryEntities = new ArrayList<ProvisionLogRetry>();

				ProvisionLogDetail detailEntity = null;
				List<ProvisionServiceVO> detailVOs = masterVO.getDetailVO();
				for (ProvisionServiceVO detailVO : detailVOs) {
					final String logDetailId = UUID.randomUUID().toString();
					final String logStepId = UUID.randomUUID().toString();

					/*
					 * Convert ProvisionLogDetail entity
					 */
					detailEntity = new ProvisionLogDetail();
					BeanUtils.copyProperties(detailVO, detailEntity);
					detailEntity.setProvisionLogMaster(masterEntity);
					detailEntity.setLogDetailId(logDetailId);
					detailEntity.setSpendTimeInSeconds(
							CommonUtils.calculateSpendTime(detailVO.getBeginTime(), detailVO.getEndTime()));
					detailEntity.setBeginTime(new Timestamp(detailVO.getBeginTime().getTime()));
					detailEntity.setEndTime(new Timestamp(detailVO.getEndTime().getTime()));
					detailEntity.setCreateBy(userName);
					detailEntity.setCreateTime(new Timestamp((new Date()).getTime()));
					detailEntities.add(detailEntity);

					/*
					 * Convert ProvisionLogStep entities
					 */
					List<ProvisionServiceVO> stepVOList = detailVO.getStepVO();
					if (stepVOList != null && !stepVOList.isEmpty()) {

						ProvisionLogStep stepEntity = null;
						for (ProvisionServiceVO stepVO : stepVOList) {
							stepEntity = new ProvisionLogStep();
							BeanUtils.copyProperties(stepVO, stepEntity);
							stepEntity.setLogStepId(logStepId);
							stepEntity.setProvisionLogDetail(detailEntity);
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

				provisionLogDAO.insertProvisionLog(masterEntity, detailEntities, stepEntities, deviceEntities, retryEntities);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			return false;
		}

		return true;
	}
}
