package com.cmap.utils.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.model.ConfigVersionInfo;
import com.cmap.security.SecurityUtil;
import com.cmap.service.vo.ConfigInfoVO;

public class CommonUtils {

	public static String composeConfigFileName(ConfigInfoVO vo, int seqNo) {
		String fileName = "";

		/*
		fileName = Constants.FORMAT_CONFIG_FILE_NAME.format(new Date()) 			// 日期戳記: YYYYMMDDHH24MI
					.concat("_")													// 分隔符號: _
					.concat(vo.getGroupId())										// 群組代碼: [Group_Id]
					.concat("_")													// 分隔符號: _
					.concat(vo.getDeviceId())										// 裝置代碼: [Device_Id]
					.concat("_")													// 分隔符號: _
					.concat(vo.getDeviceName())										// 裝置名稱: [Device_Name]
					.concat("_")													// 分隔符號: _
					.concat(vo.getConfigType())										// 組態檔類別: running/startup
					.concat("-")													// 分隔符號: -
					.concat(StringUtils.leftPad(String.valueOf(seqNo), 3, "0")) 	// 流水號: [seqNo]
					.concat(".")													// 小數點: .
					.concat(Env.CONFIG_FILE_EXTENSION_NAME);						// 副檔名: [系統參數]
		 */

		fileName = vo.getDeviceName()												// 裝置名稱: [Device_Name]
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
}
