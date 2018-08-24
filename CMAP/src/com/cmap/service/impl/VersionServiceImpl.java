package com.cmap.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.ConfigVersionInfoDAO;
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.ScriptListDAO;
import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.dao.vo.DeviceListDAOVO;
import com.cmap.dao.vo.ScriptListDAOVO;
import com.cmap.exception.ConnectionException;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.ConfigVersionInfo;
import com.cmap.model.DeviceList;
import com.cmap.security.SecurityUtil;
import com.cmap.service.StepService;
import com.cmap.service.StepService.Result;
import com.cmap.service.VersionService;
import com.cmap.service.impl.jobs.BaseJobImpl;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.StepServiceVO;
import com.cmap.service.vo.VersionServiceVO;
import com.cmap.utils.ConnectUtils;
import com.cmap.utils.FileUtils;
import com.cmap.utils.impl.CommonUtils;
import com.cmap.utils.impl.FtpFileUtils;
import com.cmap.utils.impl.SshUtils;
import com.cmap.utils.impl.TFtpFileUtils;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

@Service("versionService")
@Transactional
public class VersionServiceImpl implements VersionService {
	@Log
	private static Logger log;

	@Autowired
	private StepService stepService;

	@Autowired
	private ConfigVersionInfoDAO configVersionInfoDAO;

	@Autowired
	private DeviceListDAO deviceListDAO;

	@Autowired
	@Qualifier("scriptListDefaultDAOImpl")
	private ScriptListDAO scriptListDefaultDAO;

	/**
	 * 查找使用者有權限之群組+設備的資料筆數 for UI分頁區塊中的total使用
	 */
	@Override
	public long countUserPermissionAllVersionInfo(List<String> groupList, List<String> deviceList, String configType) throws ServiceLayerException {
		long retCount = 0;
		ConfigVersionInfoDAOVO cviDAOVO;
		try {
			cviDAOVO = new ConfigVersionInfoDAOVO();
			cviDAOVO.setQueryGroup1List(groupList);
			cviDAOVO.setQueryDevice1List(deviceList);
			cviDAOVO.setQueryConfigType(configType);

			retCount = configVersionInfoDAO.countConfigVersionInfoByDAOVO(cviDAOVO);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw e;
		}

		return retCount;
	}

	/**
	 * 依照使用者的查詢條件查找符合的資料筆數
	 */
	@Override
	public long countVersionInfo(VersionServiceVO vsVO) throws ServiceLayerException {
		long retCount = 0;
		ConfigVersionInfoDAOVO cviDAOVO;
		try {
			cviDAOVO = transServiceVO2ConfigVersionInfoDAOVO(vsVO);

			if (vsVO.isQueryNewChkbox()) {
				retCount = configVersionInfoDAO.countConfigVersionInfoByDAOVO4New(cviDAOVO);
			} else {
				retCount = configVersionInfoDAO.countConfigVersionInfoByDAOVO(cviDAOVO);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw e;
		}

		return retCount;
	}

	@Override
	public long countDeviceList(VersionServiceVO vsVO) throws ServiceLayerException {
		long retCount = 0;
		DeviceListDAOVO dlDAOVO;
		try {
			dlDAOVO = transServiceVO2DeviceListDAOVO(vsVO);

			retCount = deviceListDAO.countDeviceListAndLastestVersionByDAOVO(dlDAOVO);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw e;
		}

		return retCount;
	}

	/**
	 * 依照使用者的查詢條件查找符合的資料
	 */
	@Override
	public List<VersionServiceVO> findVersionInfo(VersionServiceVO vsVO, Integer startRow, Integer pageLength) throws ServiceLayerException {
		List<VersionServiceVO> retList = new ArrayList<>();
		List<Object[]> modelList;
		ConfigVersionInfoDAOVO cviDAOVO;
		try {
			cviDAOVO = transServiceVO2ConfigVersionInfoDAOVO(vsVO);

			if (vsVO.isQueryNewChkbox()) {
				modelList = configVersionInfoDAO.findConfigVersionInfoByDAOVO4New(cviDAOVO, startRow, pageLength);
			} else {
				modelList = configVersionInfoDAO.findConfigVersionInfoByDAOVO(cviDAOVO, startRow, pageLength);
			}

			if (modelList != null && !modelList.isEmpty()) {
				retList = transModel2ServiceVO4Version(modelList);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw e;
		}
		return retList;
	}

	@Override
	public List<VersionServiceVO> findDeviceList(VersionServiceVO vsVO, Integer startRow, Integer pageLength) throws ServiceLayerException {
		List<VersionServiceVO> retList = new ArrayList<>();
		List<Object[]> modelList;
		DeviceListDAOVO dlDAOVO;
		try {
			dlDAOVO = transServiceVO2DeviceListDAOVO(vsVO);

			modelList = deviceListDAO.findDeviceListAndLastestVersionByDAOVO(dlDAOVO, startRow, pageLength);

			if (modelList != null && !modelList.isEmpty()) {
				retList = transModel2ServiceVO4Device(modelList);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw e;
		}
		return retList;
	}

	/**
	 * 刪除資料 by VersionIDs (可一次多組)
	 * (刪除並非實質將資料刪除，而是將該筆資料註記為"已刪除"狀態
	 */
	@Override
	public boolean deleteVersionInfo(List<String> versionIDs) throws ServiceLayerException {
		try {
			configVersionInfoDAO.deleteConfigVersionInfoByVersionIds(versionIDs, SecurityUtil.getSecurityUser().getUsername());

		} catch (Exception e) {
			log.error(e.toString(), e);
			return false;
		}
		return true;
	}

	/**
	 * 轉換Service VO為ConfigVersionInfoDAOVO
	 * @param vsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	private ConfigVersionInfoDAOVO transServiceVO2ConfigVersionInfoDAOVO(VersionServiceVO vsVO) throws ServiceLayerException {
		ConfigVersionInfoDAOVO cviVO = new ConfigVersionInfoDAOVO();
		BeanUtils.copyProperties(vsVO, cviVO);
		return cviVO;
	}

	/**
	 * 轉換Service VO為DeviceListDAOVO
	 * @param vsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	private DeviceListDAOVO transServiceVO2DeviceListDAOVO(VersionServiceVO vsVO) throws ServiceLayerException {
		DeviceListDAOVO dlVO = new DeviceListDAOVO();
		BeanUtils.copyProperties(vsVO, dlVO);
		return dlVO;
	}

	/**
	 * 轉換Config_Version_Info Model檔為Service VO
	 * @param modelList
	 * @return
	 */
	private List<VersionServiceVO> transModel2ServiceVO4Version(List<Object[]> modelList) throws ServiceLayerException {
		List<VersionServiceVO> retList = new ArrayList<>();

		ConfigVersionInfo cvi;
		DeviceList dl;
		VersionServiceVO vsVO;
		for (Object[] obj : modelList) {
			cvi = (ConfigVersionInfo)obj[0];
			dl = (DeviceList)obj[1];

			vsVO = new VersionServiceVO();
			BeanUtils.copyProperties(cvi, vsVO);

			if (cvi.getCreateTime() != null) {
				vsVO.setBackupTimeStr(Constants.FORMAT_YYYYMMDD_HH24MI.format(cvi.getCreateTime()));
			}

			vsVO.setDeviceListId(dl.getDeviceListId());
			vsVO.setConfigFileDirPath(dl.getConfigFileDirPath());
			vsVO.setRemoteFileDirPath(dl.getRemoteFileDirPath());

			retList.add(vsVO);
		}

		return retList;
	}

	private List<VersionServiceVO> transModel2ServiceVO4Device(List<Object[]> modelList) throws ServiceLayerException {
		List<VersionServiceVO> retList = new ArrayList<>();

		ConfigVersionInfo cvi;
		DeviceList dl;
		VersionServiceVO vsVO;
		for (Object[] obj : modelList) {
			cvi = (ConfigVersionInfo)obj[0];
			dl = (DeviceList)obj[1];

			vsVO = new VersionServiceVO();
			BeanUtils.copyProperties(dl, vsVO);

			if (cvi.getCreateTime() != null) {
				vsVO.setBackupTimeStr(Constants.FORMAT_YYYYMMDD_HH24MI.format(cvi.getCreateTime()));
			}

			vsVO.setVersionId(cvi.getVersionId());
			vsVO.setConfigVersion(cvi.getConfigVersion());
			vsVO.setConfigType(cvi.getConfigType());

			retList.add(vsVO);
		}

		return retList;
	}

	/**
	 * 取得指定裝置的Config落地檔資訊 for UI查看Config檔內容 or 版本比對
	 */
	@Override
	public List<VersionServiceVO> findConfigFilesInfo(List<String> versionIDs) throws ServiceLayerException {
		List<VersionServiceVO> retList = null;
		try {
			List<ConfigVersionInfo> cviList = configVersionInfoDAO.findConfigVersionInfoByVersionIDs(versionIDs);

			if (cviList != null && !cviList.isEmpty()) {
				retList = new ArrayList<>();

				for (ConfigVersionInfo cvi : cviList) {
					VersionServiceVO vo = new VersionServiceVO();
					BeanUtils.copyProperties(cvi, vo);

					DeviceList dl = deviceListDAO.findDeviceListByGroupAndDeviceId(cvi.getGroupId(), cvi.getDeviceId());

					if (dl != null) {
						BeanUtils.copyProperties(dl, vo);
						retList.add(vo);
					}
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw e;
		}
		return retList;
	}

	/**
	 * 取得裝置Config落地檔 for UI查看Config檔內容 or 版本比對
	 */
	@Override
	public VersionServiceVO getConfigFileContent(VersionServiceVO vsVO) throws ServiceLayerException {
		try {
			FileUtils fileUtils = null;
			List<String> contentList = null;
			StringBuffer sb = null;
			String _hostIp = null;
			Integer _hostPort = null;
			String _loginAccount = null;
			String _loginPassword = null;

			if (StringUtils.isNotBlank(vsVO.getConfigFileDirPath())) {

				// Step1. 建立FileServer傳輸物件
				switch (Env.FILE_TRANSFER_MODE) {
				case FTP:
					fileUtils = new FtpFileUtils();
					_hostIp = Env.FTP_HOST_IP;
					_hostPort = Env.FTP_HOST_PORT;
					_loginAccount = Env.FTP_LOGIN_ACCOUNT;
					_loginPassword = Env.FTP_LOGIN_PASSWORD;
					break;

				case TFTP:
					fileUtils = new TFtpFileUtils();
					_hostIp = Env.TFTP_HOST_IP;
					_hostPort = Env.TFTP_HOST_PORT;
					break;
				}

				// Step2. FTP連線
				fileUtils.connect(_hostIp, _hostPort);

				// Step3. FTP登入
				fileUtils.login(_loginAccount, _loginPassword);

				// Step3. 移動作業目錄至指定的裝置
				fileUtils.changeDir(vsVO.getConfigFileDirPath(), false);

				// Step4. 下載指定的Config落地檔
				ConfigInfoVO ciVO = new ConfigInfoVO();
				BeanUtils.copyProperties(vsVO, ciVO);
				contentList = fileUtils.downloadFiles(ciVO);

				// Step5. 轉換為String for UI輸出
				if (contentList != null && !contentList.isEmpty()) {
					sb = new StringBuffer();

					for (String content : contentList) {
						sb.append(content).append("<br />");
					}

					vsVO.setConfigFileContent(sb.toString());
				}

				// Step6. 關閉FTP連線
				if (fileUtils != null) {
					try {
						fileUtils.disconnect();

					} catch (Exception e) {
						log.error(e.toString(), e);
					}
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e);

		}
		return vsVO;
	}

	/**
	 * 轉換FtpUtils下載組態檔method回傳型態為VO List
	 * @param ftpStringList
	 * @return
	 */
	private List<VersionServiceVO> transFTPStringList2VOList(List<String> ftpStringList) throws ServiceLayerException {
		List<VersionServiceVO> retList = new ArrayList<>();

		VersionServiceVO vo = null;
		for (String line : ftpStringList) {
			vo = new VersionServiceVO();
			vo.setLine(line);
			retList.add(vo);
		}

		return retList;
	}

	/**
	 * 根據DiffUtils差異比對後結果，將對應行數VO標記為差異點
	 * @param sCk
	 * @param tCk
	 * @param diffList
	 */
	private int markDiffList(Chunk sCk, Chunk tCk, int addedCount, List<VersionServiceVO> diffList, VersionServiceVO vo) throws ServiceLayerException {
		int position = sCk.getPosition();
		int sSize = sCk.size();
		int tSize = tCk.size();
		int added = 0;

		for (int i=position+addedCount; i<(position+sSize+addedCount); i++) {
			diffList.get(i).setLineDiff(true);
			vo.setDiffPos(vo.getDiffPos().concat(String.valueOf(i)).concat(","));
		}

		if (tSize > sSize) {
			for (int j=0; j<(tSize-sSize); j++) {
				diffList.add(position+sSize+addedCount, new VersionServiceVO());
				added++;
			}
		}

		return added;
	}

	private String removeDuplicatePos(String diffPos) throws ServiceLayerException {
		String retVal = "";

		String[] diffArray = diffPos.split(",");
		Set<Integer> set = new TreeSet<>();
		for (String dp : diffArray) {
			if (StringUtils.isNotBlank(dp)) {
				set.add(Integer.valueOf(dp));
			}
		}

		int i = 0;
		for (Object obj : set.toArray()) {
			retVal += ObjectUtils.toString(obj);

			if (i < set.size()-1) {
				retVal += ",";
			}

			i++;
		}

		return retVal;
	}

	/**
	 * 版本比對
	 */
	@Override
	public VersionServiceVO compareConfigFiles(List<VersionServiceVO> voList) throws ServiceLayerException {
		VersionServiceVO retVO = new VersionServiceVO();
		FileUtils fileUtils = null;
		List<String> contentOriList = null;
		List<String> contentRevList = null;
		List<VersionServiceVO> retOriList = null;
		List<VersionServiceVO> retRevList = null;

		try {
			for (VersionServiceVO vsVO : voList) {

				String _hostIp = null;
				Integer _hostPort = null;
				String _loginAccount = null;
				String _loginPassword = null;

				if (StringUtils.isNotBlank(vsVO.getConfigFileDirPath())) {

					// Step1. 建立FileServer傳輸物件
					switch (Env.FILE_TRANSFER_MODE) {
					case FTP:
						fileUtils = new FtpFileUtils();
						_hostIp = Env.FTP_HOST_IP;
						_hostPort = Env.FTP_HOST_PORT;
						_loginAccount = Env.FTP_LOGIN_ACCOUNT;
						_loginPassword = Env.FTP_LOGIN_PASSWORD;
						break;

					case TFTP:
						fileUtils = new TFtpFileUtils();
						_hostIp = Env.TFTP_HOST_IP;
						_hostPort = Env.TFTP_HOST_PORT;
						break;
					}

					// Step2. FTP連線
					fileUtils.connect(_hostIp, _hostPort);

					// Step3. FTP登入
					fileUtils.login(_loginAccount, _loginPassword);

					// Step3. 移動作業目錄至指定的裝置
					fileUtils.changeDir(vsVO.getConfigFileDirPath(), false);

					// Step4. 下載指定的Config落地檔
					ConfigInfoVO ciVO = new ConfigInfoVO();
					BeanUtils.copyProperties(vsVO, ciVO);
					List<String> cList = fileUtils.downloadFiles(ciVO);

					if (cList != null && !cList.isEmpty()) {
						if (contentOriList == null) {
							contentOriList = cList;
							retVO.setVersionOri(vsVO.getConfigVersion());
						} else {
							contentRevList = cList;
							retVO.setVersionRev(vsVO.getConfigVersion());
						}

					} else {
						throw new Exception("檔案取得異常");
					}

					// Step5. 關閉FTP連線
					if (fileUtils != null) {
						try {
							fileUtils.disconnect();

						} catch (Exception e) {
							log.error(e.toString(), e);
						}
					}
				}
			}

			retOriList = transFTPStringList2VOList(contentOriList);
			retRevList = transFTPStringList2VOList(contentRevList);

			// Step6. 比對檔案內容差異
			Patch patch = DiffUtils.diff(contentOriList, contentRevList);
			List<Delta> deltaList = patch.getDeltas();
			String diffPos = "";
			int oriAddedLineCount = 0;
			int revAddedLineCount = 0;
			for (Delta da : deltaList) {
				oriAddedLineCount += markDiffList(da.getOriginal(), da.getRevised(), oriAddedLineCount, retOriList, retVO);
				revAddedLineCount += markDiffList(da.getRevised(), da.getOriginal(), revAddedLineCount, retRevList, retVO);

				//				String pos = String.valueOf(da.getOriginal().getPosition()+oriAddedLineCount);
				//				diffPos += retVO.getDiffPos().concat(pos).concat(",");
			}
			//			retVO.setDiffPos(diffPos);
			diffPos = retVO.getDiffPos();
			if (StringUtils.isNotBlank(diffPos)) {
				diffPos = removeDuplicatePos(diffPos);
				retVO.setDiffPos(diffPos);
			}

			log.info("diffPos: " + retVO.getDiffPos());

			//			log.info("**************************************************************************************");
			int max = retOriList.size() >= retRevList.size() ? retOriList.size() : retRevList.size();
			for (int i=0; i<max; i++) {
				retOriList.size();
				retOriList.get(i);
				new VersionServiceVO();
				retRevList.size();
				retRevList.get(i);
				new VersionServiceVO();
			}
			//			log.info("**************************************************************************************");

			retVO.setDiffRetOriList(retOriList);
			retVO.setDiffRetRevList(retRevList);

			String[] uiTextOri = trans2HtmlFormat(retOriList, diffPos, true);
			retVO.setConfigDiffOriContent(uiTextOri[0]);
			retVO.setVersionLineNum(uiTextOri[1]);

			String[] uiTextRev = trans2HtmlFormat(retRevList, null, false);
			retVO.setConfigDiffRevContent(uiTextRev[0]);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e);

		}
		return retVO;
	}

	private String[] trans2HtmlFormat(List<VersionServiceVO> voList, String diffPos, boolean genLineNum) throws ServiceLayerException {
		StringBuffer sb = new StringBuffer();
		StringBuffer ln = new StringBuffer();
		List<String> diffList = new ArrayList<>();

		if (diffPos != null) {
			for (String pos : diffPos.split(",")) {
				if (StringUtils.isNotBlank(pos)) {
					diffList.add(pos);
				}
			}
		}

		int lineNo = 1;
		boolean diff = false;
		for (VersionServiceVO vo : voList) {
			diff = vo.isLineDiff();

			if (vo.getLine().equals(Constants.ADD_LINE)) {
				sb.append("<div class=\"progress\" style=\"margin-top: 5px\">")
				.append("<div class=\"progress-bar\" role=\"progressbar\" aria-valuenow=\"0\" aria-valuemin=\"0\" aria-valuemax=\"100\"></div>")
				.append("</div>")
				.append("</span>");

			} else {
				sb.append(diff ? "<span style=\"color:red\">" : "<span>")
				.append(vo.getLine())
				.append(diff ? "</span>" : "</span>")
				.append("<br />");
			}

			if (genLineNum) {
				ln.append("<span");

				if (!diffList.isEmpty()) {
					for (String dp : diffList) {
						if ((lineNo-1) == Integer.valueOf(dp)) {
							ln.append(" class=\"diffPos\" style=\"color:red\"");
						}
					}
				}

				ln.append(">")
				.append(lineNo)
				.append("</span>")
				.append("<br />");
			}

			lineNo++;
		}

		return new String[] {sb.toString(), ln.toString()};
	}

	@Override
	public VersionServiceVO backupConfig(String configType, List<String> deviceListIDs, boolean jobTrigger) {
		VersionServiceVO retVO = new VersionServiceVO();
		final int totalCount = deviceListIDs.size();
		retVO.setJobExcuteResultRecords(Integer.toString(totalCount));

		int successCount = 0;
		int errorCount = 0;
		int noDiffCount = 0;

		try {
			StepServiceVO ssVO;
			for (String deviceListId : deviceListIDs) {
				ssVO = stepService.doBackupStep(deviceListId, jobTrigger);

				successCount += ssVO.isSuccess() && (ssVO.getResult() != Result.NO_DIFFERENT) ? 1 : 0;
				errorCount += !ssVO.isSuccess() ? 1 : 0;
				noDiffCount += ssVO.getResult() == Result.NO_DIFFERENT ? 1 : 0;

				log.info(ssVO.toString());
			}

			if ((successCount == deviceListIDs.size() || noDiffCount == deviceListIDs.size()) && errorCount == 0) {
				retVO.setJobExcuteResult(BaseJobImpl.Result.SUCCESS);
			} else if (errorCount == deviceListIDs.size()) {
				retVO.setJobExcuteResult(BaseJobImpl.Result.FAILED);
			} else {
				retVO.setJobExcuteResult(BaseJobImpl.Result.PARTIAL_SUCCESS);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		String msg = "";
		String[] args = null;
		if (totalCount == 1) {
			if (successCount == 1) {
				msg = "備份成功";

			} else if (errorCount == 1) {
				msg = "備份失敗";

			} else if (noDiffCount == 1) {
				msg = "版本無差異";
			}

		} else {
			msg = "選定備份 {0} 筆設備: 備份成功 {1} 筆；失敗 {2} 筆；版本無差異 {3} 筆";
			args = new String[] {
					String.valueOf(totalCount),
					String.valueOf(successCount),
					String.valueOf(errorCount),
					String.valueOf(noDiffCount)
			};
		}

		retVO.setRetMsg(CommonUtils.converMsg(msg, args));
		retVO.setJobExcuteRemark(retVO.getRetMsg());

		return retVO;
	}

	@Override
	public VersionServiceVO _backupConfig(String configType, List<String> deviceListIDs, boolean jobTrigger) throws ServiceLayerException {
		VersionServiceVO retVO = new VersionServiceVO();
		ConnectUtils sshUtils = null;
		FileUtils ftpUtils = null;
		final int totalCount = deviceListIDs.size();
		int errorCount = 0;
		try {
			// Step1.取得預設腳本內容
			List<ScriptListDAOVO> scriptList =
					scriptListDefaultDAO.findScriptListByScriptCode(Env.DEFAULT_BACKUP_SCRIPT_CODE);

			if (scriptList == null || (scriptList != null && scriptList.isEmpty())) {
				throw new Exception("未設定[備份]預設腳本");
			}

			List<ConfigInfoVO> voList = new ArrayList<>();
			ConfigInfoVO configInfoVO = null;
			for (String deviceListId : deviceListIDs) {
				try {
					// Step2.查找設備連線資訊
					DeviceList device = deviceListDAO.findDeviceListByDeviceListId(deviceListId);

					if (device == null) {
						throw new Exception("[device_id: " + deviceListId + "] >> 查無設備資料");
					}

					configInfoVO = new ConfigInfoVO();
					BeanUtils.copyProperties(device, configInfoVO);
					configInfoVO.setAccount(Env.DEFAULT_DEVICE_LOGIN_ACCOUNT);
					configInfoVO.setPassword(Env.DEFAULT_DEVICE_LOGIN_PASSWORD);

					// Step3.查找設備連線帳密

					// Step4.連線設備
					sshUtils = new SshUtils();
					sshUtils.connect(device.getDeviceIp(), null);

					// Step5.登入
					sshUtils.login(configInfoVO.getAccount(), configInfoVO.getPassword());

					// Step6.送出腳本指令取回Config內容
					StepServiceVO ssVO = new StepServiceVO();
					List<String> cmdOutput = sshUtils.sendCommands(scriptList, configInfoVO, ssVO);
					if (cmdOutput != null && !cmdOutput.isEmpty()) {

						// Step7.查找此群組+設備今日是否已有備份紀錄，決定此次備份檔流水
						ConfigVersionInfoDAOVO cviDAOVO = new ConfigVersionInfoDAOVO();
						cviDAOVO.setQueryGroup1(configInfoVO.getGroupId());
						cviDAOVO.setQueryDevice1(configInfoVO.getDeviceId());
						cviDAOVO.setQueryDateBegin1(Constants.FORMAT_YYYY_MM_DD.format(new Date()));
						cviDAOVO.setQueryDateEnd1(Constants.FORMAT_YYYY_MM_DD.format(new Date()));
						List<Object[]> modelList = configVersionInfoDAO.findConfigVersionInfoByDAOVO4New(cviDAOVO, null, null);

						int seqNo = 1;
						if (modelList != null && !modelList.isEmpty()) {
							ConfigVersionInfo cvi = (ConfigVersionInfo)modelList.get(0)[0];
							String currentSeq = StringUtils.isNotBlank(cvi.getConfigVersion())
									? cvi.getConfigVersion().substring(cvi.getConfigVersion().length()-3, cvi.getConfigVersion().length())
											: "0";

									seqNo += Integer.valueOf(currentSeq);
						}

						String type = "";
						String content = "";
						for (String output : cmdOutput) {
							if (output.indexOf(Env.COMM_SEPARATE_SYMBOL) != -1) {
								type = output.split(Env.COMM_SEPARATE_SYMBOL)[0];
								content = output.split(Env.COMM_SEPARATE_SYMBOL)[1];
							} else {
								content = output;
							}

							ConfigInfoVO vo = (ConfigInfoVO)configInfoVO.clone();
							vo.setConfigType(type);
							vo.setConfigContent(content);
							CommonUtils.composeConfigFileName(vo, seqNo);

							voList.add(vo);
							seqNo++;
						}
					}

				} catch (ConnectionException ce) {
					log.error(ce.toString());

				} catch (Exception e) {
					log.error(e.toString(), e);

					errorCount++;
					continue;
				}
			}

			log.info("voList.size: "+voList.size());

			if (voList != null && !voList.isEmpty()) {
				// Step8.輸出檔案透過FTP落地保存
				// 8-1. 建立FTP物件
				ftpUtils = new FtpFileUtils();

				// 8-2. FTP連線
				ftpUtils.connect(Env.FTP_HOST_IP, Env.FTP_HOST_PORT);

				// 8-3. FTP登入
				ftpUtils.login(Env.FTP_LOGIN_ACCOUNT, Env.FTP_LOGIN_PASSWORD);


				for (ConfigInfoVO ciVO : voList) {
					try {
						// 8-3. 移動作業目錄至指定的裝置
						ftpUtils.changeDir(ciVO.getConfigFileDirPath(), true);

						// 8-4. 上傳檔案
						ftpUtils.uploadFiles(
								ciVO.getConfigFileName(),
								IOUtils.toInputStream(ciVO.getConfigContent(), Constants.CHARSET_UTF8)
								);

						// Step9.寫入DB資料
						configVersionInfoDAO.insertConfigVersionInfo(CommonUtils.composeModelEntityByConfigInfoVO(ciVO, jobTrigger));

					} catch (Exception e) {
						log.error(e.toString(), e);

						errorCount++;
						continue;
					}
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);

		} finally {
			// 關閉SSH連線
			if (sshUtils != null) {
				try {
					sshUtils.disconnect();
				} catch (Exception e) {
					log.error(e.toString(), e);
				}
			}
			// 關閉FTP連線
			if (ftpUtils != null) {
				try {
					ftpUtils.disconnect();

				} catch (Exception e) {
					log.error(e.toString(), e);
				}
			}

			String msg = "此次備份共 {0} 筆設備，成功 {1} 筆；失敗 {2} 筆";
			String[] args = new String[] {
					String.valueOf(totalCount), String.valueOf(totalCount-errorCount), String.valueOf(errorCount)
			};

			retVO.setRetMsg(CommonUtils.converMsg(msg, args));
		}

		return retVO;
	}

	@Override
	public VersionServiceVO recoverConfig(VersionServiceVO vsVO) throws ServiceLayerException {
		// TODO Auto-generated method stub
		return null;
	}
}
