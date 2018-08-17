package com.cmap.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
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
import com.cmap.comm.enums.ConnectionMode;
import com.cmap.comm.enums.Step;
import com.cmap.dao.ConfigVersionInfoDAO;
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.ScriptListDAO;
import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.dao.vo.ScriptListDAOVO;
import com.cmap.exception.FileOperationException;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.ConfigVersionInfo;
import com.cmap.model.DeviceList;
import com.cmap.security.SecurityUtil;
import com.cmap.service.ProvisionService;
import com.cmap.service.StepService;
import com.cmap.service.VersionService;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.ProvisionServiceVO;
import com.cmap.service.vo.StepServiceVO;
import com.cmap.service.vo.VersionServiceVO;
import com.cmap.utils.ConnectUtils;
import com.cmap.utils.FileUtils;
import com.cmap.utils.impl.CommonUtils;
import com.cmap.utils.impl.FtpFileUtils;
import com.cmap.utils.impl.SshUtils;
import com.cmap.utils.impl.TFtpFileUtils;

@Service("stepService")
@Transactional
public class StepServiceImpl implements StepService {
	@Log
	private static Logger log;

	@Autowired
	private ConfigVersionInfoDAO configVersionInfoDAO;

	@Autowired
	private DeviceListDAO deviceListDAO;

	@Autowired
	@Qualifier("scriptListDefaultDAOImpl")
	private ScriptListDAO scriptListDefaultDAO;

	@Autowired
	private VersionService versionService;

	@Autowired
	private ProvisionService provisionService;

	@Override
	public StepServiceVO doBackupStep(String deviceListId, boolean jobTrigger) {
		StepServiceVO retVO = new StepServiceVO();

		ProvisionServiceVO psMasterVO = new ProvisionServiceVO();
		ProvisionServiceVO psStepVO = new ProvisionServiceVO();
		ProvisionServiceVO psRetryVO;
		ProvisionServiceVO psDeviceVO;

		final int RETRY_TIMES = StringUtils.isNotBlank(Env.RETRY_TIMES) ? Integer.parseInt(Env.RETRY_TIMES) : 1;
		int round = 1;

		/*
		 * Provision_Log_Master & Step
		 */
		final String userName = jobTrigger ? Env.USER_NAME_JOB : SecurityUtil.getSecurityUser() != null ? SecurityUtil.getSecurityUser().getUsername() : Constants.SYS;
		final String userIp = jobTrigger ? Env.USER_IP_JOB : SecurityUtil.getSecurityUser() != null ? SecurityUtil.getSecurityUser().getUser().getIp() : Constants.UNKNOWN;

		psMasterVO.setUserName(userName);
		psMasterVO.setUserIp(userIp);
		psMasterVO.setBeginTime(new Date());
		psMasterVO.setRemark(jobTrigger ? Env.PROVISION_REASON_OF_JOB : null);
		psStepVO.setBeginTime(new Date());

		retVO.setActionBy(userName);
		retVO.setActionFromIp(userIp);
		retVO.setBeginTime(new Date());

		boolean retryRound = false;
		while (round <= RETRY_TIMES) {
			log.info("Round: "+round+" of "+RETRY_TIMES+". (retry times)");
			try {
				Step[] steps = null;
				ConnectionMode deviceMode = null;
				ConnectionMode fileServerMode = null;

				switch (Env.DEFAULT_BACKUP_SCRIPT_CODE) {
				case "SYS_001":
					steps = Env.BACKUP_BY_TELNET;
					deviceMode = ConnectionMode.SSH;
					fileServerMode = ConnectionMode.FTP;
					break;

				case "SYS_002":
					steps = Env.BACKUP_BY_TFTP;
					deviceMode = ConnectionMode.SSH;
					fileServerMode = ConnectionMode.TFTP;
					break;
				}

				List<ScriptListDAOVO> scripts = null;

				ConfigInfoVO ciVO = null;					// 裝置相關設定資訊VO
				ConnectUtils connectUtils = null;			// 連線裝置物件
				List<String> outputList = null;				// 命令Output內容List
				List<ConfigInfoVO> outputVOList = null;		// Output VO
				FileUtils fileUtils = null;					// 連線FileServer吳建

				for (Step _step : steps) {

					switch (_step) {
					case LOAD_DEFAULT_SCRIPT:
						scripts = loadDefaultScript(scripts);

						/*
						 * Provision_Log_Step
						 */
						final String scriptName = (scripts != null && !scripts.isEmpty()) ? scripts.get(0).getScriptName() : null;
						psStepVO.setScriptCode(Env.DEFAULT_BACKUP_SCRIPT_CODE);
						psStepVO.setRemark(scriptName);

						retVO.setScriptCode(Env.DEFAULT_BACKUP_SCRIPT_CODE);

						break;

					case FIND_DEVICE_CONNECT_INFO:
						ciVO = findDeviceConfigInfo(ciVO, deviceListId);
						ciVO.setTimes(String.valueOf(round));

						/*
						 * Provision_Log_Device
						 */
						if (!retryRound) {
							psDeviceVO = new ProvisionServiceVO();
							psDeviceVO.setDeviceListId(deviceListId);
							psDeviceVO.setOrderNum(1);
							psStepVO.getDeviceVO().add(psDeviceVO); // add DeviceVO to StepVO

							retVO.setDeviceName(ciVO.getDeviceName());
							retVO.setDeviceIp(ciVO.getDeviceIp());
						}

						break;

					case FIND_DEVICE_LOGIN_INFO:
						findDeviceLoginInfo();
						break;

					case CONNECT_DEVICE:
						connectUtils = connect2Device(connectUtils, deviceMode, ciVO);
						break;

					case LOGIN_DEVICE:
						login2Device(connectUtils, ciVO);
						break;

					case SEND_COMMANDS:
						outputList = sendCmds(connectUtils, scripts, ciVO, retVO);
						break;

					case COMPARE_CONTENTS:
						outputList = compareContents(ciVO, outputList, fileUtils, fileServerMode, retVO);
						break;

					case DEFINE_OUTPUT_FILE_NAME:
						defineFileName(ciVO);
						break;

					case COMPOSE_OUTPUT_VO:
						outputVOList = composeOutputVO(ciVO, outputList);
						break;

					case CONNECT_FILE_SERVER_4_UPLOAD:
						fileUtils = connect2FileServer(fileUtils, fileServerMode, ciVO);
						break;

					case LOGIN_FILE_SERVER_4_UPLOAD:
						login2FileServer(fileUtils, ciVO);
						break;

					case UPLOAD_FILE_SERVER:
						upload2FTP(fileUtils, outputVOList);
						break;

					case RECORD_DB:
						record2DB(outputVOList, jobTrigger);
						break;

					case CLOSE_DEVICE_CONNECTION:
						closeDeviceConnection(connectUtils);
						break;

					case CLOSE_FILE_SERVER_CONNECTION:
						closeFileServerConnection(fileUtils);
						break;
					}
				}

				retVO.setSuccess(true);
				break;

			}	catch (Exception e) {
				log.error(e.toString(), e);

				/*
				 * Provision_Log_Retry
				 */
				psRetryVO = new ProvisionServiceVO();
				psRetryVO.setResult(Result.ERROR.toString());
				psRetryVO.setMessage(e.toString());
				psRetryVO.setRetryOrder(round);
				psStepVO.getRetryVO().add(psRetryVO); // add RetryVO to StepVO

				retVO.setSuccess(false);
				retVO.setResutl(Result.ERROR);
				retVO.setMessage(e.toString());

				retryRound = true;
				round++;
			}
		}

		/*
		 * Provision_Log_Step
		 */
		psStepVO.setEndTime(new Date());
		psStepVO.setResult(retVO.getResutl().toString());
		psStepVO.setMessage(retVO.getMessage());
		psStepVO.setRetryTimes(round-1);
		psStepVO.setProcessLog(retVO.getCmdProcessLog());

		/*
		 * Provision_Log_Master
		 */
		psMasterVO.setEndTime(new Date());
		psMasterVO.setResult(retVO.getResutl().toString());
		psMasterVO.setMessage(retVO.getMessage());
		psMasterVO.getStepVO().add(psStepVO); // add StepVO to MasterVO

		try {
			provisionService.insertProvisionLog(psMasterVO); // Insert provision log
		} catch (ServiceLayerException e) {
			log.error(e.toString(), e);
		}

		retVO.setEndTime(new Date());
		retVO.setRetryTimes(round);

		return retVO;
	}

	@Override
	public void doBackupFileUpload2FTPStep(List<VersionServiceVO> vsVOs, ConfigInfoVO ciVO, boolean jobTrigger) {
		final int RETRY_TIMES = StringUtils.isNotBlank(Env.RETRY_TIMES) ? Integer.parseInt(Env.RETRY_TIMES) : 1;
		int round = 1;

		boolean success = true;
		while (round <= RETRY_TIMES) {
			log.info("Round: "+round+" of "+RETRY_TIMES+". (retry times)");
			try {
				Step[] steps = null;
				ConnectionMode downloadMode = null;
				ConnectionMode uploadMode = null;

				switch (Env.DEFAULT_BACKUP_SCRIPT_CODE) {
				case "SYS_001":
					steps = null;
					downloadMode = ConnectionMode.FTP;
					uploadMode = ConnectionMode.FTP;
					break;

				case "SYS_002":
					steps = Env.BACKUP_FILE_DOWNLOAD_FROM_TFTP_AND_UPLOAD_2_FTP;
					downloadMode = ConnectionMode.TFTP;
					uploadMode = ConnectionMode.FTP;
					break;
				}

				List<ConfigInfoVO> outputVOList = null;		// Output VO
				FileUtils fileUtils = null;					// 連線FileServer物件

				for (Step _step : steps) {
					log.info(_step.toString());

					switch (_step) {
					case CONNECT_FILE_SERVER_4_DOWNLOAD:
						fileUtils = connect2FileServer(fileUtils, downloadMode, ciVO);
						break;

					case DOWNLOAD_FILE:
						outputVOList = downloadFile(fileUtils, vsVOs, ciVO, true);
						break;

					case CONNECT_FILE_SERVER_4_UPLOAD:
						fileUtils = connect2FileServer(fileUtils, uploadMode, ciVO);
						break;

					case LOGIN_FILE_SERVER_4_UPLOAD:
						login2FileServer(fileUtils, ciVO);
						break;

					case UPLOAD_FILE_SERVER:
						upload2FTP(fileUtils, outputVOList);
						break;

					case CLOSE_FILE_SERVER_CONNECTION:
						closeFileServerConnection(fileUtils);
						break;
					}
				}

				success = true;

			} catch (Exception e) {
				log.error(e.toString(), e);

				success = false;

			} finally {
				if (success) {
					break;
				} else {
					round++;
				}
			}
		}
	}

	/**
	 * 關閉與裝置的連線
	 * @param connectUtils
	 */
	private void closeDeviceConnection(ConnectUtils connectUtils) {
		try {
			if (connectUtils != null) {
				connectUtils.disconnect();
			}

		} catch (Exception e) {

		} finally {
			connectUtils = null;
		}
	}

	/**
	 * 關閉與FTP/TFTP的連線
	 * @param fileUtils
	 */
	private void closeFileServerConnection(FileUtils fileUtils) {
		try {
			if (fileUtils != null) {
				fileUtils.disconnect();
			}

		} catch (Exception e) {

		} finally {
			fileUtils = null;
		}
	}

	/**
	 * [Step] 取得預設腳本內容
	 * @param script
	 * @return
	 * @throws ServiceLayerException
	 */
	private List<ScriptListDAOVO> loadDefaultScript(List<ScriptListDAOVO> script) throws ServiceLayerException {
		if (script != null && !script.isEmpty()) {
			return script;
		}

		script = scriptListDefaultDAO.findScriptListByScriptCode(Env.DEFAULT_BACKUP_SCRIPT_CODE);

		if (script == null || (script != null && script.isEmpty())) {
			throw new ServiceLayerException("未設定[備份]預設腳本");
		}

		return script;
	}

	/**
	 * [Step] 查找設備連線資訊
	 * @param configInfoVO
	 * @param deviceListId
	 * @return
	 * @throws ServiceLayerException
	 */
	private ConfigInfoVO findDeviceConfigInfo(ConfigInfoVO configInfoVO, String deviceListId) throws ServiceLayerException {
		DeviceList device = deviceListDAO.findDeviceListByDeviceListId(deviceListId);

		if (device == null) {
			throw new ServiceLayerException("[device_id: " + deviceListId + "] >> 查無設備資料");
		}

		configInfoVO = new ConfigInfoVO();
		BeanUtils.copyProperties(device, configInfoVO);

		/**
		 * TODO 預留裝置登入帳密BY設備設定
		 */
		configInfoVO.setAccount(Env.DEFAULT_DEVICE_LOGIN_ACCOUNT);
		configInfoVO.setPassword(Env.DEFAULT_DEVICE_LOGIN_PASSWORD);
		configInfoVO.setEnablePassword(Env.DEFAULT_DEVICE_ENABLE_PASSWORD);

		/**
		 * TODO 預留裝置落地檔上傳FTP/TFTP位址BY設備設定
		 */
		configInfoVO.setFtpIP(Env.FTP_HOST_IP);
		configInfoVO.setFtpPort(Env.FTP_HOST_PORT);
		configInfoVO.setFtpAccount(Env.FTP_LOGIN_ACCOUNT);
		configInfoVO.setFtpPassword(Env.FTP_LOGIN_PASSWORD);

		configInfoVO.settFtpIP(Env.TFTP_HOST_IP);
		configInfoVO.settFtpPort(Env.TFTP_HOST_PORT);

		return configInfoVO;
	}

	/**
	 * [Step] 查找設備連線帳密
	 * @throws ServiceLayerException
	 */
	private void findDeviceLoginInfo() throws ServiceLayerException {

	}

	/**
	 * [Step] 連線設備
	 * @param connectUtils
	 * @param _mode
	 * @param ciVO
	 * @return
	 * @throws Exception
	 */
	private ConnectUtils connect2Device(ConnectUtils connectUtils, ConnectionMode _mode, ConfigInfoVO ciVO) throws Exception {
		switch (_mode) {
		case TELNET:
			break;

		case SSH:
			connectUtils = new SshUtils();
			connectUtils.connect(ciVO.getDeviceIp(), null);
			break;
		}

		return connectUtils;
	}

	/**
	 * [Step] 登入
	 * @param connectUtils
	 * @param ciVO
	 * @throws Exception
	 */
	private void login2Device(ConnectUtils connectUtils, ConfigInfoVO ciVO) throws Exception {
		connectUtils.login(
				StringUtils.isBlank(ciVO.getAccount()) ? Env.DEFAULT_DEVICE_LOGIN_ACCOUNT : ciVO.getAccount(),
						StringUtils.isBlank(ciVO.getPassword()) ? Env.DEFAULT_DEVICE_LOGIN_PASSWORD : ciVO.getPassword()
				);

		/*
		Method method = obj.getClass().getMethod("login", new Class[]{String.class,String.class});
		method.invoke(
				obj,
				new String[] {
							StringUtils.isBlank(ciVO.getAccount()) ? Env.DEFAULT_DEVICE_LOGIN_ACCOUNT : ciVO.getAccount(),
							StringUtils.isBlank(ciVO.getPassword()) ? Env.DEFAULT_DEVICE_LOGIN_PASSWORD : ciVO.getPassword()
						});
		 */
	}

	/**
	 * [Step] 執行腳本指令並取回設定要輸出的指令回傳內容
	 * @param connectUtils
	 * @param scriptList
	 * @param configInfoVO
	 * @return
	 * @throws Exception
	 */
	private List<String> sendCmds(ConnectUtils connectUtils, List<ScriptListDAOVO> scriptList,  ConfigInfoVO configInfoVO, StepServiceVO ssVO) throws Exception {
		return connectUtils.sendCommands(scriptList, configInfoVO, ssVO);
	}

	/**
	 * [Step] 跟前一版本比對內容，若內容無差異則不再新增備份檔
	 * @param ciVO
	 * @param outputList
	 * @param fileUtils
	 * @param _mode
	 * @return
	 * @throws Exception
	 */
	private List<String> compareContents(ConfigInfoVO ciVO, List<String> outputList, FileUtils fileUtils, ConnectionMode _mode, StepServiceVO ssVO) throws Exception {
		String type = "";

		boolean haveDiffVersion = false;
		List<String> tmpList = outputList.stream().collect(Collectors.toList());
		ConfigVersionInfoDAOVO daovo;
		for (final String output : tmpList) {
			if (output.indexOf(Env.COMM_SEPARATE_SYMBOL) != -1) {
				type = output.split(Env.COMM_SEPARATE_SYMBOL)[0];
			}

			// 查找此裝置前一版本組態檔資料
			daovo = new ConfigVersionInfoDAOVO();
			daovo.setQueryGroup1(ciVO.getGroupId());
			daovo.setQueryDevice1(ciVO.getDeviceId());
			daovo.setQueryConfigType(type);
			List<Object[]> entityList = configVersionInfoDAO.findConfigVersionInfoByDAOVO4New(daovo, null, null);

			// 當前備份版本正確檔名
			final String nowVersionFileName = StringUtils.replace(ciVO.getConfigFileName(), Env.COMM_SEPARATE_SYMBOL, type);

			// 當前備份版本上傳於temp資料夾內檔名 (若TFTP Server與CMAP系統不是架設在同一台主機時)
			final String nowVersionTempFileName = !Env.TFTP_SERVER_AT_LOCAL ? nowVersionFileName.concat(".").concat(ciVO.getTempFileRandomCode()) : null;

			List<VersionServiceVO> vsVOs;
			if (entityList != null && !entityList.isEmpty()) {
				final ConfigVersionInfo cviEneity = (ConfigVersionInfo)entityList.get(0)[0];
				final DeviceList dlEntity = (DeviceList)entityList.get(0)[1];

				vsVOs = new ArrayList<>();

				//前一版本VO
				VersionServiceVO preVersionVO = new VersionServiceVO();
				preVersionVO.setConfigFileDirPath(dlEntity.getConfigFileDirPath());
				preVersionVO.setFileFullName(cviEneity.getFileFullName());
				vsVOs.add(preVersionVO);

				//當下備份上傳版本VO
				VersionServiceVO nowVersionVO = new VersionServiceVO();

				/*
				 * 若TFTP Server與CMAP系統不是架設在同一台主機上
				 * Config file從Device上傳時會先放置於temp資料夾內(Env.TFTP_TEMP_DIR_PATH)
				 * 比對版本內容時抓取的檔名(FileFullName)也必須調整為temp資料夾內檔名(nowVersionTempFileName，有加上時間細數碼)
				 */
				nowVersionVO.setConfigFileDirPath(
						Env.TFTP_SERVER_AT_LOCAL ? ciVO.getConfigFileDirPath() : Env.TFTP_TEMP_DIR_PATH);
				nowVersionVO.setFileFullName(
						!Env.TFTP_SERVER_AT_LOCAL ? nowVersionTempFileName : nowVersionFileName);
				vsVOs.add(nowVersionVO);

				VersionServiceVO compareRetVO = versionService.compareConfigFiles(vsVOs);

				log.info("[compareContents] >> Env.TFTP_SERVER_AT_LOCAL: " + Env.TFTP_SERVER_AT_LOCAL);
				if (StringUtils.isBlank(compareRetVO.getDiffPos())) {
					log.info("[compareContents] >> outputList.remove");
					/*
					 * 版本內容比對相同:
					 * (1)若[Env.TFTP_SERVER_AT_LOCAL=true]，刪除本機已上傳的檔案;若為false則不處理(另外設定系統排程定期清整temp資料夾內檔案)
					 * (2)移除List內容
					 */
					outputList.remove(output);

					if (Env.TFTP_SERVER_AT_LOCAL) {
						log.info("[compareContents] >> 刪除本機已上傳的檔案");
						//TODO:刪除本機已上傳的檔案
						deleteLocalFile(ciVO);
					}

				} else {
					haveDiffVersion = true;
					/*
					 * 版本內容不同:
					 * (1)若[Env.TFTP_SERVER_AT_LOCAL=false]，將檔案從TFTP temp資料夾copy到Device對應目錄;若為true則不需再作處理
					 */
					if (Env.TFTP_SERVER_AT_LOCAL == null || (Env.TFTP_SERVER_AT_LOCAL != null && !Env.TFTP_SERVER_AT_LOCAL)) {
						final String sourceDirPath = Env.TFTP_TEMP_DIR_PATH;
						final String targetDirPath = ciVO.getConfigFileDirPath().concat(File.separator).concat(nowVersionFileName);
						ciVO.setFileFullName(nowVersionTempFileName);

						log.info("[compareContents] >> fileUtils.moveFiles");
						fileUtils.moveFiles(ciVO, sourceDirPath, targetDirPath);
					}
				}

			} else {
				haveDiffVersion = true;
				/*
				 * 若沒有前一版本(系統首次備份):
				 * (1)若[Env.TFTP_SERVER_AT_LOCAL=false]，將檔案從TFTP temp資料夾copy到Device對應目錄;若為true則不需再作處理
				 */
				if (Env.TFTP_SERVER_AT_LOCAL == null || (Env.TFTP_SERVER_AT_LOCAL != null && !Env.TFTP_SERVER_AT_LOCAL)) {
					final String sourceDirPath = Env.TFTP_TEMP_DIR_PATH;
					final String targetDirPath = ciVO.getConfigFileDirPath().concat(File.separator).concat(nowVersionFileName);
					ciVO.setFileFullName(nowVersionTempFileName);

					log.info("[compareContents] >> fileUtils.moveFiles");
					fileUtils.moveFiles(ciVO, sourceDirPath, targetDirPath);
				}
			}
		}

		if (!haveDiffVersion) {
			ssVO.setResutl(Result.NO_DIFFERENT);
			ssVO.setMessage("版本無差異");
		} else {
			ssVO.setResutl(Result.SUCCESS);
		}

		return outputList;
	}

	/**
	 * [Step] 定義輸出檔案名稱
	 * @param configInfoVO
	 * @throws ServiceLayerException
	 */
	private void defineFileName(ConfigInfoVO configInfoVO) throws ServiceLayerException {
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

		String fileName = CommonUtils.composeConfigFileName(configInfoVO, seqNo);
		String tFtpTargetFilePath =
				(Env.TFTP_SERVER_AT_LOCAL ? configInfoVO.getConfigFileDirPath() : Env.TFTP_TEMP_DIR_PATH).concat(File.separator).concat(fileName);

		/*
		 * 若 TFTP Server 與 CMAP系統 不是架設在同一台主機上
		 * 因組態檔案名稱時間戳記僅有到「分」，若同一分鐘內備份多次，會因為檔名重複而命令執行失敗
		 * 因此，若此條件下，將上傳到temp資料夾的檔案名稱加上時間細數碼
		 */
		long miles = System.currentTimeMillis();
		long seconds = TimeUnit.MILLISECONDS.toSeconds(miles) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(miles));
		String tFtpTempFileRandomCode =
				(!Env.TFTP_SERVER_AT_LOCAL
						? String.valueOf(seconds).concat(".").concat(configInfoVO.getTimes())
								: null);

		String tempFilePath = tFtpTargetFilePath.concat(".").concat(tFtpTempFileRandomCode);

		configInfoVO.setConfigFileName(fileName);
		configInfoVO.settFtpFilePath(tFtpTargetFilePath);
		configInfoVO.setTempFileRandomCode(tFtpTempFileRandomCode);
		configInfoVO.setTempFilePath(tempFilePath);
	}

	/**
	 * [Step] 查找此群組+設備今日是否已有備份紀錄，決定此次備份檔流水
	 * @param configInfoVO
	 * @param outputList
	 * @return
	 * @throws ServiceLayerException
	 * @throws CloneNotSupportedException
	 */
	private List<ConfigInfoVO> composeOutputVO(ConfigInfoVO configInfoVO, List<String> outputList) throws ServiceLayerException, CloneNotSupportedException {
		List<ConfigInfoVO> voList = new ArrayList<>();
		String type = "";
		String content = "";

		ConfigInfoVO vo;
		for (String output : outputList) {
			if (output.indexOf(Env.COMM_SEPARATE_SYMBOL) != -1) {
				type = output.split(Env.COMM_SEPARATE_SYMBOL)[0];
				content = output.split(Env.COMM_SEPARATE_SYMBOL)[1];
			} else {
				content = output;
			}

			vo = (ConfigInfoVO)configInfoVO.clone();
			vo.setConfigType(type);
			vo.setConfigContent(content);

			String configFileName = vo.getConfigFileName();
			if (configFileName.indexOf(Env.COMM_SEPARATE_SYMBOL) != -1) {
				configFileName = StringUtils.replace(configFileName, Env.COMM_SEPARATE_SYMBOL, type);
				vo.setConfigFileName(configFileName);
			}

			voList.add(vo);
		}

		return voList;
	}

	/**
	 * [Step] 建立FTP/TFTP連線
	 * @param fileUtils
	 * @param _mode
	 * @param ciVO
	 * @return
	 * @throws Exception
	 */
	private FileUtils connect2FileServer(FileUtils fileUtils, ConnectionMode _mode, ConfigInfoVO ciVO) throws Exception {
		switch (_mode) {
		case FTP:
			// By FTP
			fileUtils = new FtpFileUtils();
			fileUtils.connect(ciVO.getFtpIP(), ciVO.getFtpPort());
			break;

		case TFTP:
			// By TFTP
			fileUtils = new TFtpFileUtils();
			fileUtils.connect(ciVO.gettFtpIP(), ciVO.gettFtpPort());
		}

		return fileUtils;
	}

	/**
	 * 刪除本機檔案
	 * @param ciVO
	 * @return
	 * @throws FileOperationException
	 */
	private boolean deleteLocalFile(ConfigInfoVO ciVO) throws FileOperationException {
		try {
			final String filePath = Env.TFTP_LOCAL_ROOT_DIR_PATH.concat(ciVO.getConfigFileDirPath()).concat(ciVO.getFileFullName());

			Path path = Paths.get(filePath);
			if (Files.isRegularFile(path) & Files.isReadable(path) & Files.isExecutable(path)) {
				Files.delete(path);
				return true;

			} else {
				throw new FileOperationException("[組態檔內容相同，但無法刪除檔案] >> " + filePath);
			}

		} catch (Exception e) {
			throw new FileOperationException("[組態檔內容相同，但刪除檔案過程異常] >> " + e.toString());
		}
	}

	/**
	 * 移動本機檔案
	 * @param ciVO
	 * @return
	 * @throws FileOperationException
	 */
	private boolean moveLocalFile(ConfigInfoVO ciVO) throws FileOperationException {
		try {
			final String source = Env.TFTP_LOCAL_ROOT_DIR_PATH.concat(Env.TFTP_TEMP_DIR_PATH).concat(ciVO.getFileFullName());
			final String target = Env.TFTP_LOCAL_ROOT_DIR_PATH.concat(ciVO.getConfigFileDirPath()).concat(ciVO.getFileFullName());

			final Path sourcePath = Paths.get(source);
			final Path targetPath = Paths.get(target);

			if (Files.isRegularFile(sourcePath) & Files.isReadable(sourcePath) & Files.isExecutable(sourcePath)) {
				Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
				return true;

			} else {
				throw new FileOperationException("[組態檔內容相同，但無法移動檔案] >> " + source);
			}

		} catch (Exception e) {
			throw new FileOperationException("[組態檔內容相同，但移動檔案過程異常] >> " + e.toString());
		}
	}

	/**
	 * [Step] 登入FTP
	 * @param fileUtils
	 * @param ciVO
	 * @throws Exception
	 */
	private void login2FileServer(FileUtils fileUtils, ConfigInfoVO ciVO) throws Exception {
		fileUtils.login(
				StringUtils.isBlank(ciVO.getFtpAccount()) ? Env.FTP_LOGIN_ACCOUNT : ciVO.getFtpAccount(),
						StringUtils.isBlank(ciVO.getFtpPassword()) ? Env.FTP_LOGIN_PASSWORD : ciVO.getFtpPassword()
				);
	}

	/**
	 * [Step] 輸出檔案透過FTP落地保存
	 * @param ftpUtils
	 * @param ciVOList
	 * @throws Exception
	 */
	private void upload2FTP(FileUtils ftpUtils, List<ConfigInfoVO> ciVOList) throws Exception {

		String configFileDirPath = "";
		if (ciVOList != null && !ciVOList.isEmpty()) {
			configFileDirPath = ciVOList.get(0).getConfigFileDirPath();

			// 8-3. 移動作業目錄至指定的裝置
			ftpUtils.changeDir(configFileDirPath, true);

			// 8-4. 上傳檔案
			for (ConfigInfoVO ciVO : ciVOList) {
				ftpUtils.uploadFiles(
						ciVO.getConfigFileName(),
						IOUtils.toInputStream(ciVO.getConfigContent(), Constants.CHARSET_UTF8)
						);
			}
		}
	}

	/**
	 * [Step] 寫入DB資料
	 * @param ciVOList
	 * @param jobTrigger
	 */
	private void record2DB(List<ConfigInfoVO> ciVOList, boolean jobTrigger) {
		for (ConfigInfoVO ciVO : ciVOList) {
			configVersionInfoDAO.insertConfigVersionInfo(CommonUtils.composeModelEntityByConfigInfoVO(ciVO, jobTrigger));
		}
	}

	/**
	 * [Step] 從FTP/TFTP下載資料
	 * @param fileUtils
	 * @param vsVOs
	 * @param ciVO
	 * @param returnFileString
	 * @return
	 * @throws Exception
	 */
	private List<ConfigInfoVO> downloadFile(FileUtils fileUtils, List<VersionServiceVO> vsVOs, ConfigInfoVO ciVO, boolean returnFileString) throws Exception {
		List<ConfigInfoVO> ciVOList = new ArrayList<>();

		ConfigInfoVO tmpVO = null;
		int i = 1;
		for (VersionServiceVO vsVO : vsVOs) {
			tmpVO = (ConfigInfoVO)ciVO.clone();
			tmpVO.setConfigFileDirPath(vsVO.getConfigFileDirPath());
			tmpVO.setFileFullName(vsVO.getFileFullName());

			log.info("Downloading file name ("+i+"/"+vsVOs.size()+"): "+tmpVO.getConfigFileDirPath()+File.separator+tmpVO.getFileFullName());

			if (returnFileString) {
				final String fileContent = fileUtils.downloadFilesString(tmpVO);
				tmpVO.setConfigContent(fileContent);

			} else {
				final List<String> fileContentList = fileUtils.downloadFiles(ciVO);
				tmpVO.setConfigContentList(fileContentList);
			}

			tmpVO.setConfigFileName(vsVO.getFileFullName());

			ciVOList.add(tmpVO);
			i++;
		}
		return ciVOList;
	}
}
