package com.cmap.utils.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.model.ConfigVersionInfo;
import com.cmap.security.SecurityUtil;
import com.cmap.service.vo.ConfigInfoVO;

public class CommonUtils {
	private static Logger log = LoggerFactory.getLogger(CommonUtils.class);

	public static String composeConfigFileName(ConfigInfoVO vo, int seqNo) {

		String fileName = StringUtils.isNotBlank(Env.BACKUP_FILENAME_FORMAT) ? Env.BACKUP_FILENAME_FORMAT : "";

		/*
		 * [dName]-[dIP]-[configType]-[date]-[seqNo].[extName]
		 * Ex: 1F_Center-192.168.1.1-RUNNING_CONFIG-201808210914-001.cfg
		 */
		// 裝置名稱: [Device_Name]
		if (fileName.indexOf(Constants.DIR_PATH_DEVICE_ID) != -1) {
			fileName = StringUtils.replace(fileName, Constants.DIR_PATH_DEVICE_ID, vo.getDeviceId());
		}
		// 裝置名稱: [Device_Name]
		if (fileName.indexOf(Constants.DIR_PATH_DEVICE_NAME) != -1) {
			fileName = StringUtils.replace(fileName, Constants.DIR_PATH_DEVICE_NAME, vo.getDeviceEngName());
		}
		// 裝置IP: [Device_Ip]
		if (fileName.indexOf(Constants.DIR_PATH_DEVICE_IP) != -1) {
			fileName = StringUtils.replace(fileName, Constants.DIR_PATH_DEVICE_IP, vo.getDeviceIp());
		}
		// 裝置名稱: [Device_Name]
		if (fileName.indexOf(Constants.DIR_PATH_DEVICE_SYSTEM) != -1) {
			fileName = StringUtils.replace(fileName, Constants.DIR_PATH_DEVICE_SYSTEM, vo.getSystemVersion());
		}
		// 組態檔類別: running/startup
		if (fileName.indexOf(Constants.DIR_PATH_CONFIG_TYPE) != -1) {
			fileName = StringUtils.replace(fileName, Constants.DIR_PATH_CONFIG_TYPE, (StringUtils.isBlank(vo.getConfigType()) ? Env.COMM_SEPARATE_SYMBOL : vo.getConfigType()));
		}
		// 日期戳記: YYYYMMDDHH24MI
		if (fileName.indexOf(Constants.DIR_PATH_DATE) != -1) {
			fileName = StringUtils.replace(fileName, Constants.DIR_PATH_DATE, Env.BACKUP_FILENAME_DATE_FORMAT.format(new Date()));
		}
		// 流水號: [seqNo]
		if (fileName.indexOf(Constants.DIR_PATH_SEQ_NO) != -1) {
			fileName = StringUtils.replace(
					fileName, Constants.DIR_PATH_SEQ_NO,
					StringUtils.leftPad(String.valueOf(seqNo), StringUtils.isNotBlank(Env.BACKUP_FILENAME_SEQ_NO_LENGTH) ? Integer.valueOf(Env.BACKUP_FILENAME_SEQ_NO_LENGTH) : 3, "0"));
		}
		// 副檔名: [系統參數]
		if (fileName.indexOf(Constants.DIR_PATH_EXT_NAME) != -1) {
			fileName = StringUtils.replace(fileName, Constants.DIR_PATH_EXT_NAME, Env.CONFIG_FILE_EXTENSION_NAME);
		}

		/*
		fileName = vo.getDeviceEngName()										// 裝置名稱: [Device_Name]
				.concat("-")													// 分隔符號: -
				.concat(vo.getDeviceIp())										// 裝置IP: [Device_Ip]
				.concat("-")													// 分隔符號: -
				.concat(
						StringUtils.isBlank(vo.getConfigType())
						? Env.COMM_SEPARATE_SYMBOL
								: vo.getConfigType())							// 組態檔類別: running/startup
				.concat("-")													// 分隔符號: -
				.concat(Constants.FORMAT_CONFIG_FILE_NAME.format(new Date())) 	// 日期戳記: YYYYMMDDHH24MI
				.concat("-")													// 分隔符號: -
				.concat(StringUtils.leftPad(String.valueOf(seqNo), 3, "0")) 	// 流水號: [seqNo]
				.concat(".")													// 小數點: .
				.concat(Env.CONFIG_FILE_EXTENSION_NAME);						// 副檔名: [系統參數]
		 */

		return fileName;
	}

	public static ConfigVersionInfo composeModelEntityByConfigInfoVO(ConfigInfoVO ciVO, boolean jobTrigger) {
		String _user = "";

		try {
			_user = jobTrigger ? Constants.SYS : SecurityUtil.getSecurityUser().getUsername();

		} catch (NullPointerException npe) {
			_user = Constants.SYS;
		}

		ConfigVersionInfo entity = new ConfigVersionInfo(
				null
				,ciVO.getGroupId()
				,ciVO.getGroupName()
				,ciVO.getDeviceId()
				,ciVO.getDeviceName()
				,ciVO.getSystemVersion()
				,ciVO.getConfigType()
				,StringUtils.substring(ciVO.getConfigFileName(), 0, ciVO.getConfigFileName().lastIndexOf("."))
				,ciVO.getConfigFileName()
				,Constants.DATA_MARK_NOT_DELETE
				,null
				,null
				,new Timestamp((new Date()).getTime())
				,_user
				,new Timestamp((new Date()).getTime())
				,_user
				);
		return entity;
	}

	public static String converMsg(String msg, Object... args) {
		if (args != null) {
			for (int i=0; i<args.length; i++) {
				String symbol = "{".concat(String.valueOf(i)).concat("}");
				msg = msg.replace(symbol, args[i].toString());
			}
		}

		return msg;
	}

	public static int calculateSpendTime(Date beginDate, Date endDate) {
		final long beginTime = beginDate.getTime();
		final long endTime = endDate.getTime();
		final long spendTimeInSeconds =
				TimeUnit.MILLISECONDS.toSeconds(endTime-beginTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime-beginTime));

		int spendTime = 0;

		try {
			spendTime = ((Long)spendTimeInSeconds).intValue();

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return spendTime;
	}

	public static int calculateSpendTime(Timestamp beginDate, Timestamp endDate) {
		final long beginTime = beginDate.getTime();
		final long endTime = endDate.getTime();
//		final long spendTimeInSeconds =
//				TimeUnit.MILLISECONDS.toSeconds(endTime-beginTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime-beginTime));
		final long spendTimeInSeconds =
				TimeUnit.MILLISECONDS.toSeconds(endTime-beginTime);

		int spendTime = 0;

		try {
			spendTime = ((Long)spendTimeInSeconds).intValue();

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return spendTime;
	}

	protected String replaceContentSign(String cmd, ConfigInfoVO configInfoVO, String remark) {
		if (cmd.contains(Env.SIGN_ACT)) {
			cmd = StringUtils.replace(cmd, Env.SIGN_ACT, configInfoVO.getAccount());
		}
		if (cmd.contains(Env.SIGN_PWD)) {
			cmd = StringUtils.replace(cmd, Env.SIGN_PWD, configInfoVO.getPassword());
		}
		if (cmd.contains(Env.SIGN_ENABLE_PWD)) {
			cmd = StringUtils.replace(cmd, Env.SIGN_ENABLE_PWD, configInfoVO.getEnablePassword());
		}
		if (cmd.contains(Env.SIGN_TFTP_IP)) {
			cmd = StringUtils.replace(cmd, Env.SIGN_TFTP_IP, configInfoVO.gettFtpIP());
		}
		if (cmd.contains(Env.SIGN_TFTP_OUTPUT_FILE_PATH)) {
			String tFtpFilePath = configInfoVO.gettFtpFilePath();

			if (!Env.TFTP_SERVER_AT_LOCAL) {
				/*
				 * 若 TFTP Server 與 CMAP系統 不是架設在同一台主機上 (因TFTP Client端無法刪除 Server端檔案)
				 * 此時Config file上傳時會先上傳到 TFTP 的 temp 資料夾，待版本內容比對後才決定是否要將 temp 檔案移動到正確的 device 目錄下
				 * 因此，放置在 temp 資料夾內的檔案名稱須加上時間細數碼
				 */
				//				tFtpFilePath = tFtpFilePath.concat("-").concat(configInfoVO.getTempFileRandomCode());
				tFtpFilePath = configInfoVO.getTempFilePath();
			}

			if (StringUtils.isNotBlank(remark)) {
				tFtpFilePath = StringUtils.replace(tFtpFilePath, Env.COMM_SEPARATE_SYMBOL, remark);
			}

			cmd = StringUtils.replace(cmd, Env.SIGN_TFTP_OUTPUT_FILE_PATH, tFtpFilePath);
		}

		return cmd;
	}

	/**
	 * 命令回傳結果內容處理
	 * @param content 原始回傳內容
	 * @param headCutCount 開頭往後要去除的行數
	 * @param tailCutCount 結尾往前要去除的行數
	 * @param splitBy 行段落用什麼符號分行
	 * @return
	 */
	protected String cutContent(String content, int headCutCount, int tailCutCount, String splitBy) {
		String retString = "";
		StringBuffer sb = null;
		try {
			final String[] contentArray = content.split(splitBy);

			if (contentArray != null && contentArray.length != 0) {
				int startAt = (headCutCount > contentArray.length) ? contentArray.length-1 : headCutCount;
				int endAt = ((contentArray.length-tailCutCount) < 0) ? contentArray.length-1 : (contentArray.length-tailCutCount);

				sb = new StringBuffer();
				for (int i=startAt; i<endAt; i++) {
					sb.append(contentArray[i])
					.append(System.lineSeparator());
				}

				retString = sb != null ? sb.toString() : retString;
			}

		} catch (Exception e) {
			throw e;
		}

		return retString;
	}
}
