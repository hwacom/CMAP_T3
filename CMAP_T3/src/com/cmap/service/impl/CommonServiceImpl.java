package com.cmap.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DeviceDAO;
import com.cmap.dao.GroupSubnetDAO;
import com.cmap.dao.MenuItemDAO;
import com.cmap.dao.ProtocolDAO;
import com.cmap.dao.PrtgDAO;
import com.cmap.dao.ScriptTypeDAO;
import com.cmap.exception.AuthenticateException;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DeviceList;
import com.cmap.model.DeviceLoginInfo;
import com.cmap.model.GroupSubnetSetting;
import com.cmap.model.MenuItem;
import com.cmap.model.ProtocolSpec;
import com.cmap.model.PrtgAccountMapping;
import com.cmap.model.ScriptType;
import com.cmap.security.SecurityUtil;
import com.cmap.service.CommonService;
import com.cmap.service.vo.CommonServiceVO;
import com.cmap.service.vo.PrtgServiceVO;
import com.cmap.utils.ApiUtils;
import com.cmap.utils.impl.PrtgApiUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService {
	@Log
	private static Logger log;

	@Autowired
	private DeviceDAO deviceDAO;

	@Autowired
	private MenuItemDAO menuItemDAO;

	@Autowired
	private ScriptTypeDAO scriptTypeDAO;

	@Autowired
	private PrtgDAO prtgDAO;

	@Autowired
	private ProtocolDAO protocolDAO;

	@Autowired
    private JavaMailSenderImpl mailSender;

	@Autowired
	private GroupSubnetDAO groupSubnetDAO;

	@Override
    public String convertByteSizeUnit(BigDecimal sizeByte, Integer targetUnit) {
        int scale = Env.NET_FLOW_SIZE_SCALE;
        BigDecimal unitSize = new BigDecimal("1024");
        BigDecimal sizeKb = sizeByte.divide(unitSize, scale, RoundingMode.HALF_UP);
        BigDecimal sizeMb = (sizeByte.divide(unitSize)).divide(unitSize, scale, RoundingMode.HALF_UP);
        BigDecimal sizeGb = (sizeByte.divide(unitSize).divide(unitSize)).divide(unitSize, scale, RoundingMode.HALF_UP);
        BigDecimal sizeTb = (sizeByte.divide(unitSize).divide(unitSize).divide(unitSize)).divide(unitSize, scale, RoundingMode.HALF_UP);
        BigDecimal unitBaseSize = new BigDecimal("1.00"); //有超過下一單位的數量1時再轉換 (e.g. 100MB不到1GB，不轉換成0.1GB；1120MB有超過1GB，轉換成1.xxGB)

        /*
         * targetUnit : 目標最高轉換至哪個單位
         * 1=B / 2=KB / 3=MB / 4=GB / 5=TB
         */
        String convertedSize = "";
        if (targetUnit >= 5 && sizeTb.compareTo(unitBaseSize) == 1) {
            convertedSize = Constants.NUMBER_FORMAT_THOUSAND_SIGN.format(sizeTb) + " TB";

        } else if (targetUnit >= 4 && sizeGb.compareTo(unitBaseSize) == 1) {
            convertedSize = Constants.NUMBER_FORMAT_THOUSAND_SIGN.format(sizeGb) + " GB";

        } else if (targetUnit >= 3 && sizeMb.compareTo(unitBaseSize) == 1) {
            convertedSize = Constants.NUMBER_FORMAT_THOUSAND_SIGN.format(sizeMb) + " MB";

        } else if (targetUnit >= 2 && sizeKb.compareTo(unitBaseSize) == 1) {
            convertedSize = Constants.NUMBER_FORMAT_THOUSAND_SIGN.format(sizeKb) + " KB";

        } else if (targetUnit >= 1){
            convertedSize = Constants.NUMBER_FORMAT_THOUSAND_SIGN.format(sizeByte) + " B";
        }

        return convertedSize;
    }

	/**
	 * 組合 Local / Remote 落地檔路徑資料夾
	 * @param deviceInfoMap
	 * @param local (true=Local;false=Remote)
	 * @return
	 */
	private String composeFilePath(Map<String, String> deviceInfoMap, boolean local) {
		String dirPath = Env.FTP_DIR_SEPARATE_SYMBOL;

		String groupDirName = local ? Env.DEFAULT_LOCAL_DIR_GROUP_NAME : Env.DEFAULT_REMOTE_DIR_GROUP_NAME;
		if (groupDirName.indexOf(Constants.DIR_PATH_GROUP_ID) != -1) {
			groupDirName = StringUtils.replace(groupDirName, Constants.DIR_PATH_GROUP_ID, deviceInfoMap.get(Constants.GROUP_ID));
		}
		if (groupDirName.indexOf(Constants.DIR_PATH_GROUP_NAME) != -1) {
			groupDirName = StringUtils.replace(groupDirName, Constants.DIR_PATH_GROUP_NAME, deviceInfoMap.get(Constants.GROUP_ENG_NAME));
		}

		String deviceDirName = local ? Env.DEFAULT_LOCAL_DIR_DEVICE_NAME : Env.DEFAULT_REMOTE_DIR_DEVICE_NAME;
		if (deviceDirName.indexOf(Constants.DIR_PATH_DEVICE_ID) != -1) {
			deviceDirName = StringUtils.replace(deviceDirName, Constants.DIR_PATH_DEVICE_ID, deviceInfoMap.get(Constants.DEVICE_ID));
		}
		if (deviceDirName.indexOf(Constants.DIR_PATH_DEVICE_NAME) != -1) {
			deviceDirName = StringUtils.replace(deviceDirName, Constants.DIR_PATH_DEVICE_NAME, deviceInfoMap.get(Constants.DEVICE_ENG_NAME));
		}
		if (deviceDirName.indexOf(Constants.DIR_PATH_DEVICE_IP) != -1) {
			deviceDirName = StringUtils.replace(deviceDirName, Constants.DIR_PATH_DEVICE_IP, deviceInfoMap.get(Constants.DEVICE_IP));
		}
		if (deviceDirName.indexOf(Constants.DIR_PATH_DEVICE_SYSTEM) != -1) {
			deviceDirName = StringUtils.replace(deviceDirName, Constants.DIR_PATH_DEVICE_SYSTEM, deviceInfoMap.get(Constants.DEVICE_SYSTEM));
		}

		dirPath = dirPath.concat(StringUtils.isNotBlank(groupDirName) ? groupDirName : "")
				.concat(StringUtils.isNotBlank(groupDirName) ? Env.FTP_DIR_SEPARATE_SYMBOL : "")
				.concat(StringUtils.isNotBlank(deviceDirName) ? deviceDirName : "");

		return dirPath;
	}

	/**
	 * 呼叫PRTG API取得當前使用者權限下所有群組&設備清單
	 */
	@Override
	public Map<String, String> getGroupAndDeviceMenu(HttpServletRequest request) {
		Map<String, String> retMap = null;
		try {
			ApiUtils prtgApi = new PrtgApiUtils();
			Map[] prtgMap = prtgApi.getGroupAndDeviceMenu(request);

			if (prtgMap != null) {

				final Map<String, String> groupInfoMap = prtgMap[0];
				retMap = groupInfoMap;

				if (prtgMap[1] != null && !((Map<String, Map<String, Map<String, String>>>)prtgMap[1]).isEmpty()) {
					if (request.getSession() != null) {
						request.getSession().setAttribute(Constants.GROUP_DEVICE_MAP, prtgMap[1]);

						List<DeviceList> deviceList = new ArrayList<>();

						DeviceList dl = null;
						Map<String, Map<String, Map<String, String>>> groupDeviceMap = prtgMap[1];
						for (String groupId : groupDeviceMap.keySet()) {
							Map<String, Map<String, String>> deviceMap = groupDeviceMap.get(groupId);

							for (String deviceId : deviceMap.keySet()) {

								Map<String, String> deviceInfoMap = deviceMap.get(deviceId);

								// 先撈取查看此群組+設備ID資料是否已存在
								// Y181203, 改成只看設備ID, 否則若從PRTG內將設備移動到別的群組下會更新不到
								dl = deviceDAO.findDeviceListByGroupAndDeviceId(null, deviceId);

								final String localFileDirPath = composeFilePath(deviceInfoMap, true);
								final String remoteFileDirPath = composeFilePath(deviceInfoMap, false);
								boolean noNeedToAddOrModify = true;
								if (dl == null) {
									// 不存在表示後面要新增
									noNeedToAddOrModify = false;

									dl = new DeviceList();
									dl.setGroupId(groupId);
									dl.setDeviceId(deviceId);

									dl.setConfigFileDirPath(localFileDirPath);
									dl.setRemoteFileDirPath(remoteFileDirPath);

									dl.setDeleteFlag(Constants.DATA_MARK_NOT_DELETE);
									dl.setCreateBy(Constants.SYS);
									dl.setCreateTime(new Timestamp((new Date()).getTime()));

								} else {
									// 若已存在，確認以下欄位是否有異動，若其中一項有異動的話則後面要進行更新
									// Y181203, 增加判斷群組ID是否有異動
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getGroupId()) ? "" : dl.getGroupId()).equals(deviceInfoMap.get(Constants.GROUP_ID));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getGroupName()) ? "" : dl.getGroupName()).equals(deviceInfoMap.get(Constants.GROUP_NAME));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getGroupEngName()) ? "" : dl.getGroupEngName()).equals(deviceInfoMap.get(Constants.GROUP_ENG_NAME));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getDeviceName()) ? "" : dl.getDeviceName()).equals(deviceInfoMap.get(Constants.DEVICE_NAME));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getDeviceEngName()) ? "" : dl.getDeviceEngName()).equals(deviceInfoMap.get(Constants.DEVICE_ENG_NAME));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getDeviceIp()) ? "" : dl.getDeviceIp()).equals(deviceInfoMap.get(Constants.DEVICE_IP));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getDeviceModel()) ? "" : dl.getDeviceModel()).equals(deviceInfoMap.get(Constants.DEVICE_MODEL));
									}
									if (noNeedToAddOrModify) {
                                        noNeedToAddOrModify =
                                                (StringUtils.isBlank(dl.getDeviceLayer()) ? "" : dl.getDeviceLayer()).equals(deviceInfoMap.get(Constants.DEVICE_LAYER));
                                    }
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getConfigFileDirPath()) ? "" : dl.getConfigFileDirPath()).equals(localFileDirPath);
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getRemoteFileDirPath()) ? "" : dl.getRemoteFileDirPath()).equals(remoteFileDirPath);
									}
								}

								if (!noNeedToAddOrModify) {
									dl.setGroupName(deviceInfoMap.get(Constants.GROUP_NAME));
									dl.setGroupEngName(deviceInfoMap.get(Constants.GROUP_ENG_NAME));
									dl.setDeviceName(deviceInfoMap.get(Constants.DEVICE_NAME));
									dl.setDeviceEngName(deviceInfoMap.get(Constants.DEVICE_ENG_NAME));
									dl.setDeviceIp(deviceInfoMap.get(Constants.DEVICE_IP));
									dl.setDeviceModel(deviceInfoMap.get(Constants.DEVICE_MODEL));
									dl.setDeviceLayer(deviceInfoMap.get(Constants.DEVICE_LAYER));
									dl.setConfigFileDirPath(localFileDirPath);
									dl.setRemoteFileDirPath(remoteFileDirPath);
									dl.setUpdateBy(Constants.SYS);
									dl.setUpdateTime(new Timestamp((new Date()).getTime()));

									deviceList.add(dl);
								}
							}
						}

						// 更新 or 寫入 DEVICE_LIST 資料
						if (deviceList != null && !deviceList.isEmpty()) {
							updateDeviceList(deviceList);
						}
					}
				}
			}

		} catch (AuthenticateException ae) {
			log.error(ae.toString());

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retMap;
	}

	@Override
	public void updateDeviceList(List<DeviceList> deviceList) {
		// 更新 or 寫入 DEVICE_LIST 資料
		deviceDAO.saveOrUpdateDeviceListByModel(deviceList);
	}

	@Override
	public Map<String, String> getMenuItem(String menuCode, boolean combineOrderDotLabel) {
		Map<String, String> retMap = new LinkedHashMap<>();

		try {
			List<MenuItem> itemList = menuItemDAO.findMenuItemByMenuCode(menuCode);

			for (MenuItem item : itemList) {
				retMap.put(
						item.getOptionValue(),
						combineOrderDotLabel ? String.valueOf(item.getOptionOrder()).concat(Env.MENU_ITEM_COMBINE_SYMBOL).concat(item.getOptionLabel())
								: item.getOptionLabel());
			}

		} catch (Exception e) {

		}

		return retMap;
	}

	@Override
	public Map<String, String> getScriptTypeMenu(String defaultFlag) {
		Map<String, String> retMap = new LinkedHashMap<>();

		try {
			List<ScriptType> scriptTypeList = scriptTypeDAO.findScriptTypeByDefaultFlag(defaultFlag);

			for (ScriptType type : scriptTypeList) {
				retMap.put(type.getScriptTypeCode(), type.getScriptTypeName());
			}

		} catch (Exception e) {

		}
		return retMap;
	}

	protected String currentUserName() {
		try {
			if (SecurityUtil.getSecurityUser() == null) {
		        return Env.USER_NAME_JOB;
		    } else {
		        return SecurityUtil.getSecurityUser().getUsername();
		    }
		} catch (Exception e) {
			return Env.USER_NAME_JOB;
		}
	}

	protected Timestamp currentTimestamp() {
		return new Timestamp((new Date()).getTime());
	}

	protected Object transJSON2Object(String jsonStr, Class<?> mClass) {
		ObjectMapper oMapper = new ObjectMapper();
		try {
			return oMapper.readValue(jsonStr, mClass);

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public PrtgServiceVO findPrtgLoginInfo(String sourceId) {
		PrtgServiceVO retVO = null;
		try {
			PrtgAccountMapping mapping = prtgDAO.findPrtgAccountMappingBySourceId(sourceId);

			if (mapping != null) {
				retVO = new PrtgServiceVO();
				retVO.setAccount(mapping.getPrtgAccount());
				retVO.setPassword(mapping.getPrtgPassword());
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retVO;
	}

	public String base64Decoder(String base64String) {
		final Base64.Decoder decoder = Base64.getDecoder();
		try {
			return new String(decoder.decode(base64String), Constants.CHARSET_UTF8);

		} catch (UnsupportedEncodingException e) {
			log.error(e.toString(), e);
			return base64String;
		}
	}

	@Override
	public Map<Integer, CommonServiceVO> getProtoclSpecMap() {
		Map<Integer, CommonServiceVO> retMap = new HashMap<>();
		try {
			List<ProtocolSpec> reList = protocolDAO.findAllProtocolSpec();

			CommonServiceVO csVO;
			for (ProtocolSpec ps : reList) {
				csVO = new CommonServiceVO();
				BeanUtils.copyProperties(csVO, ps);

				retMap.put(ps.getProtocolNo(), csVO);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retMap;
	}

    @Override
    public void sendMail(String[] toAddress, String[] ccAddress, String[] bccAddress,
            String subject, String mailContent, ArrayList<String> filePathList) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mailMsg.setFrom(Env.MAIL_FROM_ADDRESS);
        mailMsg.setTo(toAddress);
        mailMsg.setCc(ccAddress);
        mailMsg.setBcc(bccAddress);
        mailMsg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
        mailMsg.setText(mailContent, true);

        if (filePathList != null) {
            for (int i = 0; i < filePathList.size(); i++) {
                File file = new File(filePathList.get(i));
                if (file != null) {
                    mailMsg.addAttachment(file.getName(), file);
                }
            }
        }

        mailSender.send(mimeMessage);
    }

    @Override
    public String getUserName() {
        String retVal = "N/A";
        try {
            retVal = SecurityUtil.getSecurityUser() == null ? "JOB" : SecurityUtil.getSecurityUser().getUsername();

        } catch (Exception e) {
//            log.error(e.toString(), e);
        }
        return retVal;
    }

    public static void generateAndSendEmail() throws AddressException, MessagingException {
        Properties mailServerProperties;
        Session getMailSession;
        MimeMessage generateMailMessage;

        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("shinhwa520.ken@gmail.com"));
        generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("ken.lin@hwacom.com"));
        generateMailMessage.setSubject("Greetings from Crunchify..");
        String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", "shinhwa520@gmail.com", "@Ken52034!");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    /**
     * 寄發 HTML 格式 MAIL
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param htmlContent
     */
    public static void sendHtmlEmail(String[] to, String[] cc, String[] bcc, String subject, String htmlContent) throws ServiceLayerException {
        HtmlEmail email = new HtmlEmail();

        try {
            String host = Env.MAIL_SERVER_HOST;
            String port = Env.MAIL_SERVER_PORT;

            String from = Env.MAIL_FROM_ADDRESS;
            String fromName = Env.MAIL_FROM_USERNAME;

            String user = Env.MAIL_SERVER_ACCOUNT;
            String pwd = Env.MAIL_SERVER_PASSWORD;

            email.setStartTLSEnabled(true); // 是否TLS檢驗，某些email需要TLS安全檢驗，同理有SSL檢驗
            email.setHostName(host);
            email.setAuthenticator(new DefaultAuthenticator(user, pwd)); // 使用者帳號及密碼
            email.setSmtpPort(Integer.parseInt(port));

            email.setFrom(from, fromName);
            email.setCharset("utf-8");

            email.addTo(to); // 接收方

            if (cc != null && cc.length > 0) {
                email.addCc(cc); //副本
            }

            if (bcc != null && bcc.length > 0) {
                email.addBcc(bcc); //密件副本
            }

            email.setSubject(subject); // 標題
            email.setTextMsg("Your email client does not support HTML messages");
            email.setHtmlMsg(htmlContent); // 内容
            email.send();

        } catch (EmailException e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("發信失敗 (" + e.getMessage() + ")");

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("發信失敗 (" + e.getMessage() + ")");
        }
    }

    @Override
    public String getGroupSubnetSetting(String groupId, String ipVersion) {
        String retVal = "";
        try {
            List<GroupSubnetSetting> settings = groupSubnetDAO.getGroupSubnetSettingByGroupId(groupId);

            if (settings == null || (settings != null && settings.isEmpty())) {
                log.error("GROUP_ID = " + groupId + " >>> 查無網段設定(GroupSubnetSetting) !!");
                return retVal;
            }

            int idx = 1;
            for (GroupSubnetSetting setting : settings) {
                switch (ipVersion) {
                    case Constants.IPV4:
                        retVal += setting.getIpv4Subnet();
                        break;

                    case Constants.IPV6:
                        retVal += setting.getIpv6Subnet();
                        break;
                }

                if (idx < settings.size()) {
                    retVal += Env.COMM_SEPARATE_SYMBOL;
                }
                idx++;
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return retVal;
    }

    @Override
    public boolean chkIpInGroupSubnet(String cidr, String ip, String ipVersion) {
        boolean retVal = false;

        try {
            switch (ipVersion) {
                case Constants.IPV4:
                    retVal = chkIpInGroupSubnetForIPv4(cidr, ip);
                    break;

                case Constants.IPV6:
                    retVal = chkIpInGroupSubnetForIPv6(cidr, ip);
                    break;
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return retVal;
    }

    /**
     * 確認IP是否位在設定的網段(cidr)內 for IPv4版本
     * @param cidr
     * @param ip
     * @return
     */
    private boolean chkIpInGroupSubnetForIPv4(String cidr, String ip) {
        if (StringUtils.isBlank(cidr)) {
            return false;
        }

        // 一個群組可能會有多組網段設定
        String[] cidrs = cidr.split(Env.COMM_SEPARATE_SYMBOL);

        boolean result = false;
        for (String cidrStr : cidrs) {
            String[] ips = ip.split("\\.");
            int ipAddr = (Integer.parseInt(ips[0]) << 24)
                            | (Integer.parseInt(ips[1]) << 16)
                            | (Integer.parseInt(ips[2]) << 8)
                            | Integer.parseInt(ips[3]);

            int type = Integer.parseInt(cidrStr.replaceAll(".*/", ""));
            int mask = 0xFFFFFFFF << (32 - type);

            String cidrIp = cidrStr.replaceAll("/.*", "");
            String[] cidrIps = cidrIp.split("\\.");
            int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                                | (Integer.parseInt(cidrIps[1]) << 16)
                                | (Integer.parseInt(cidrIps[2]) << 8)
                                | Integer.parseInt(cidrIps[3]);
            result = (ipAddr & mask) == (cidrIpAddr & mask);

            if (result) {
                // 只要IP落在其中一組網段內則終止迴圈
                break;
            }
        }
        return result;
    }

    /**
     * 確認IP是否位在設定的網段(cidr)內 for IPv6版本
     * @param cidr
     * @param ip
     * @return
     */
    private boolean chkIpInGroupSubnetForIPv6(String cidr, String ip) {
        return false;
    }

	@Override
	public DeviceLoginInfo findDeviceLoginInfo(String deviceListId, String groupId, String deviceId) {
		try {
			DeviceLoginInfo loginInfo = deviceDAO.findDeviceLoginInfo(deviceListId, groupId, deviceId);

			if (loginInfo == null) {
			    if (!StringUtils.equals(deviceListId, Constants.DATA_STAR_SYMBOL)) {
			        // 若by【資料ID + 設備ID】查找不到，則再往上一層by【群組ID + 設備ID】查找
			    	deviceListId = Constants.DATA_STAR_SYMBOL;

			    } else if (!StringUtils.equals(deviceId, Constants.DATA_STAR_SYMBOL)) {
			        // 若by【設備ID】查找不到，則再往上一層by【群組ID】查找  (PS: 最上層為群組ID)
			    	deviceId = Constants.DATA_STAR_SYMBOL;
			    	
			    } else if (!StringUtils.equals(deviceId, Constants.DATA_STAR_SYMBOL)) {
			    	// 若by【群組ID】查找不到，則再往上一層by【* + * + *】查找
			    	groupId = Constants.DATA_STAR_SYMBOL;
			    	
			    } else {
			    	return null;
			    }
			    
			    return findDeviceLoginInfo(deviceListId, groupId, deviceId);

			} else {
				return loginInfo;
			}
			
		} catch (Exception e) {
			log.error(e.toString(), e);
			return null;
		}
	}
}
