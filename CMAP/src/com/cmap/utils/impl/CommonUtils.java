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
}
