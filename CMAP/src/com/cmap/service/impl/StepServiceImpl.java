package com.cmap.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.comm.ConnectionMode;
import com.cmap.comm.Step;
import com.cmap.dao.ConfigVersionInfoDAO;
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.ScriptListDAO;
import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.dao.vo.ScriptListDAOVO;
import com.cmap.exception.ConnectionException;
import com.cmap.model.ConfigVersionInfo;
import com.cmap.model.DeviceList;
import com.cmap.service.StepService;
import com.cmap.service.vo.ConfigInfoVO;
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
	private static Logger log = LoggerFactory.getLogger(StepServiceImpl.class);

	@Autowired
	private ConfigVersionInfoDAO configVersionInfoDAO;
	
	@Autowired
	private DeviceListDAO deviceListDAO;
	
	@Autowired
	@Qualifier("scriptListDefaultDAOImpl")
	private ScriptListDAO scriptListDefaultDAO;
	
	@Override
	public boolean doBackupStep(String deviceListId, boolean jobTrigger) {
		final int RETRY_TIMES = StringUtils.isNotBlank(Env.RETRY_TIMES) ? Integer.parseInt(Env.RETRY_TIMES) : 1;
		int round = 1;
		
		boolean success = true;
		while (round <= RETRY_TIMES) {
			System.out.println("Round: "+round+" of "+RETRY_TIMES+". (retry times)");
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
							break;
						
						case FIND_DEVICE_CONNECT_INFO:
							ciVO = findDeviceConfigInfo(ciVO, deviceListId);
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
							outputList = sendCmds(connectUtils, scripts, ciVO);
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
				
				success = true;
				break;
				
			}	catch (Exception e) {
				log.error(e.toString(), e);
				e.printStackTrace();
				
				success = false;
				
			} finally {
				round++;
			}
		}
		
		return success;
	}
	
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

	// Step1.取得預設腳本內容
	private List<ScriptListDAOVO> loadDefaultScript(List<ScriptListDAOVO> script) throws Exception {
		if (script != null && !script.isEmpty()) {
			return script;
		}
		
		script = scriptListDefaultDAO.findScriptListByScriptCode(Env.DEFAULT_BACKUP_SCRIPT_CODE);
		
		if (script == null || (script != null && script.isEmpty())) {
			throw new Exception("未設定[備份]預設腳本");
		}
		
		return script;
	}
	
	// Step2.查找設備連線資訊
	private ConfigInfoVO findDeviceConfigInfo(ConfigInfoVO configInfoVO, String deviceListId) throws Exception {
		DeviceList device = deviceListDAO.findDeviceListByDeviceListId(deviceListId);
		
		if (device == null) {
			throw new Exception("[device_id: " + deviceListId + "] >> 查無設備資料");
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
		configInfoVO.setFtpAccount(Env.FTP_LOGIN_ACCOUNT);
		configInfoVO.setFtpPassword(Env.FTP_LOGIN_PASSWORD);
		
		configInfoVO.settFtpIP(Env.TFTP_HOST_IP);
		
		return configInfoVO;
	}
	
	// Step3.查找設備連線帳密
	private void findDeviceLoginInfo() throws Exception {
		
	}
	
	// Step4.連線設備
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
	
	// Step5.登入
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
	
	// Step6.送出腳本指令取回Config內容
	private List<String> sendCmds(ConnectUtils connectUtils, List<ScriptListDAOVO> scriptList,  ConfigInfoVO configInfoVO) throws Exception {
		return connectUtils.sendCommands(scriptList, configInfoVO);
	}
	
	// Step6.定義輸出檔案名稱
	private void defineFileName(ConfigInfoVO configInfoVO) throws Exception {
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
		String tFtpTargetFilePath = configInfoVO.getConfigFileDirPath().concat(File.separator).concat(fileName);
		
		configInfoVO.setConfigFileName(fileName);
		configInfoVO.settFtpFilePath(tFtpTargetFilePath);
	}
	
	// Step7.查找此群組+設備今日是否已有備份紀錄，決定此次備份檔流水
	private List<ConfigInfoVO> composeOutputVO(ConfigInfoVO configInfoVO, List<String> outputList) throws Exception {
		List<ConfigInfoVO> voList = new ArrayList<ConfigInfoVO>();
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
	
	// Step8.建立FTP連線
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
	
	private void login2FileServer(FileUtils fileUtils, ConfigInfoVO ciVO) throws Exception {
		// 8-2. FTP連線
		fileUtils.login(
			StringUtils.isBlank(ciVO.getFtpAccount()) ? Env.FTP_LOGIN_ACCOUNT : ciVO.getFtpAccount(), 
			StringUtils.isBlank(ciVO.getFtpPassword()) ? Env.FTP_LOGIN_PASSWORD : ciVO.getFtpPassword()
		);
	}
	
	// Step8.輸出檔案透過FTP落地保存
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
	
	// Step9.寫入DB資料
	private void record2DB(List<ConfigInfoVO> ciVOList, boolean jobTrigger) {
		for (ConfigInfoVO ciVO : ciVOList) {
			configVersionInfoDAO.insertConfigVersionInfo(CommonUtils.composeModelEntityByConfigInfoVO(ciVO, jobTrigger));
		}
	}
	
	// Step.從TFTP下載資料
	private List<ConfigInfoVO> downloadFile(FileUtils fileUtils, List<VersionServiceVO> vsVOs, ConfigInfoVO ciVO) throws Exception {
		List<ConfigInfoVO> ciVOList = new ArrayList<ConfigInfoVO>();
		
		ConfigInfoVO tmpVO = null;
		int i = 1;
		for (VersionServiceVO vsVO : vsVOs) {
			tmpVO = (ConfigInfoVO)ciVO.clone();
			tmpVO.setConfigFileDirPath(vsVO.getConfigFileDirPath());
			tmpVO.setFileFullName(vsVO.getFileFullName());
			
			System.out.println("Downloading file name ("+i+"/"+vsVOs.size()+"): "+tmpVO.getConfigFileDirPath()+File.separator+tmpVO.getFileFullName());
			final String fileContent = fileUtils.downloadFilesString(tmpVO);
			tmpVO.setConfigContent(fileContent);
			tmpVO.setConfigFileName(vsVO.getFileFullName());
			
			ciVOList.add(tmpVO);
			i++;
		}
		return ciVOList;
	}

	@Override
	public void doBackupFileUpload2FTPStep(List<VersionServiceVO> vsVOs, ConfigInfoVO ciVO, boolean jobTrigger) {
		final int RETRY_TIMES = StringUtils.isNotBlank(Env.RETRY_TIMES) ? Integer.parseInt(Env.RETRY_TIMES) : 1;
		int round = 1;
		
		boolean success = true;
		while (round <= RETRY_TIMES) {
			System.out.println("Round: "+round+" of "+RETRY_TIMES+". (retry times)");
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
				FileUtils fileUtils = null;					// 連線FileServer吳建

				for (Step _step : steps) {
					System.out.println(_step);
					
					switch (_step) {
						case CONNECT_FILE_SERVER_4_DOWNLOAD:
							fileUtils = connect2FileServer(fileUtils, downloadMode, ciVO);
							break;
							
						case DOWNLOAD_FILE:
							outputVOList = downloadFile(fileUtils, vsVOs, ciVO);
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
				e.printStackTrace();
				
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
}
