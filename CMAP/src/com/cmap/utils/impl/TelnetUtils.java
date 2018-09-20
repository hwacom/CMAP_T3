package com.cmap.utils.impl;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.dao.vo.ScriptListDAOVO;
import com.cmap.exception.CommandExecuteException;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.StepServiceVO;
import com.cmap.utils.ConnectUtils;

public class TelnetUtils extends CommonUtils implements ConnectUtils {
	private static Logger log = LoggerFactory.getLogger(TelnetUtils.class);

	private TelnetClient telnet = null;
	private InputStream in;
	private PrintStream out;
	private final String prompt = "$";
	private StringBuilder processLog = new StringBuilder();

	public TelnetUtils() throws Exception {
		if (telnet == null) {
			telnet = new TelnetClient();
			telnet.setConnectTimeout(Env.TELNET_CONNECT_TIME_OUT);
			telnet.setDefaultTimeout(Env.TELNET_DEFAULT_TIME_OUT);
		}
	}

	private void checkTelnetStatus() throws Exception {
		boolean conError = false;

		if (telnet == null) {
			conError = true;

		} else {
			if (!telnet.isConnected() || !telnet.isAvailable()) {
				conError = true;
			}
		}

		if (conError) {
			throw new Exception("Telnet connect interrupted!");
		}
	}

	@Override
	public boolean connect(final String ipAddress, final Integer port) throws Exception {
		boolean result = false;
		try {
			if (telnet != null) {
				telnet.connect(
						ipAddress,
						port == null ? Env.TELNET_DEFAULT_PORT : port);
				log.info("Telnet connect success!");

				in = telnet.getInputStream();
				out = new PrintStream(telnet.getOutputStream());
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return result;
	}

	@Override
	public boolean login(final String account, final String password) throws Exception {
		String output = "";
		try {
			output = readUntil("username: ");
			write(account);

			processLog.append(output + account);

			output = readUntil("password: ");
			write(password);

			processLog.append(output + password);

		} catch (Exception e) {

		}
		return false;
	}

	private void write(String cmd) {
		try {
			out.println(cmd);
			out.flush();
			log.info("cmd: "+cmd);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
	}

	private String readUntil(String pattern) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			char lastChar = pattern.charAt(pattern.length()-1);
			char ch = (char)in.read();

			int runTime = 0;
			log.info("<readUntil>******************************************************************");
			while (true) {
				sb.append(ch);
				log.info(String.valueOf(ch));

				if (ch == lastChar) {
					if (Strings.toUpperCase(sb.toString()).endsWith(Strings.toUpperCase(pattern))) {
						log.info("");
						log.info("</readUntil>******************************************************************");
						return sb.toString();
					}
				}

				if (runTime > 10000) {
					return sb.toString();
				}

				ch = (char)in.read();
				runTime++;
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw e;
		}
	}

	@Override
	public List<String> sendCommands(List<ScriptListDAOVO> scriptList, ConfigInfoVO configInfoVO, StepServiceVO ssVO) throws Exception {
		List<String> cmdOutputs = new ArrayList<String>();
		try {
			checkTelnetStatus();

			try {
				String cmd;
				String output;
				for (ScriptListDAOVO vo : scriptList) {
					output = "";

					String[] errorSymbols = StringUtils.isNotBlank(vo.getErrorSymbol()) ? vo.getErrorSymbol().split(Env.COMM_SEPARATE_SYMBOL) : null;

					// 送出命令
					cmd = replaceContentSign(vo.getScriptContent(), configInfoVO, vo.getRemark());
					write(cmd);
					output = readUntil(vo.getExpectedTerminalSymbol());

					processLog.append(output);

					boolean success = true;

					if (errorSymbols != null) {
						for (String errSymbol : errorSymbols) {
							success = output.toUpperCase().contains(errSymbol) ? false : true;

							if (!success) {
								throw new CommandExecuteException("[Command execute failed!] >> output: " + output);
							}
						}
					}

					if (success) {
						if (vo.getOutput() != null && vo.getOutput().equals(Constants.DATA_Y)) {
							cmdOutputs.add(
									vo.getRemark()
									.concat(Env.COMM_SEPARATE_SYMBOL)
									.concat(
											cutContent(
													output,
													StringUtils.isNotBlank(vo.getHeadCuttingLines()) ? Integer.valueOf(vo.getHeadCuttingLines()) : 0,
															StringUtils.isNotBlank(vo.getTailCuttingLines()) ? Integer.valueOf(vo.getTailCuttingLines()) : 0,
																	System.lineSeparator()
													)
											)
									);
						}
					}
				}

				/*
	        	expect.sendLine("cisco").expect(contains("#"));
	        	expect.sendLine("terminal length 0").expect(contains("#"));
	        	String fullStr = expect.sendLine("sh run").expect(contains("#")).getBefore();
	        	cmdResult = cutContent(fullStr, 3, 2, System.lineSeparator());
	        	log.info("*****************************************************************");
	        	log.info(cmdResult);
	        	log.info("*****************************************************************");
				 */

			} catch (Exception e) {
				log.error(e.toString(), e);
				throw e;

			} finally {
				log.info(processLog.toString());
				ssVO.setCmdProcessLog(processLog.toString());
			}

		} catch (CommandExecuteException cee) {
			throw cee;
		} catch (Exception e) {
			throw new Exception("[TELNET send command failed] >> " + e.getMessage());
		}

		return cmdOutputs;
	}

	@Override
	public boolean logout() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect() throws Exception {
		boolean result = false;
		try {

			if (telnet != null) {
				telnet.disconnect();
				telnet = null;
				result = true;
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return result;
	}

}
