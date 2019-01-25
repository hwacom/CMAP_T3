package com.cmap.utils.impl;

import static net.sf.expectit.filter.Filters.removeColors;
import static net.sf.expectit.filter.Filters.removeNonPrintable;
import static net.sf.expectit.matcher.Matchers.contains;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.exception.CommandExecuteException;
import com.cmap.exception.ConnectionException;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.ScriptServiceVO;
import com.cmap.service.vo.StepServiceVO;
import com.cmap.utils.ConnectUtils;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Shell;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

public class SshUtils extends CommonUtils implements ConnectUtils {
	private static Logger log = LoggerFactory.getLogger(SshUtils.class);

	private SSHClient ssh = null;

	public SshUtils() throws Exception {
		if (ssh == null) {
			ssh = new SSHClient();
			ssh.setConnectTimeout(Env.SSH_CONNECT_TIME_OUT);
			ssh.setTimeout(Env.SSH_SOCKET_TIME_OUT);
			log.info("***************** [ SSH start ] ******************");
		}
	}

	private void checkSshStatus() throws Exception {
		boolean conError = false;

		if (ssh == null) {
			conError = true;

		} else {
			if (!ssh.isConnected()) {
				//				conError = true;
			}
		}

		if (conError) {
			throw new Exception("SSH connect interrupted!");
		}
	}

	@Override
	public boolean connect(final String ipAddress, final Integer port) throws Exception {
		try {
			if (ssh == null) {
				throw new IllegalStateException("SSH connect interrupted!");
			}

			ssh.addHostKeyVerifier(
					new HostKeyVerifier() {
						@Override
						public boolean verify(String s, int i, PublicKey publicKey) {
							return true;
						}
					});

			ssh.connect(
					ipAddress,
					port == null ? Env.SSH_DEFAULT_PORT : port);
			log.info("SSH connect success!!");

		} catch (Exception e) {
			throw new ConnectionException("[SSH connect failed] " + ipAddress + ":" + port + " >> " + e.getMessage());
		}
		return true;
	}

	@Override
	public boolean login(final String account, final String password) throws Exception {
		try {
			checkSshStatus();

			ssh.authPassword(account, password);
			log.info("SSH login success!!");

		} catch (Exception e) {
			throw new Exception("[SSH login failed] >> " + e.getMessage());
		}
		return true;
	}

	@Override
	public List<String> sendCommands(List<ScriptServiceVO> scriptList, ConfigInfoVO configInfoVO, StepServiceVO ssVO) throws Exception {
		List<String> cmdOutputs = new ArrayList<String>();
		try {
			checkSshStatus();

			if (scriptList == null || (scriptList != null && scriptList.isEmpty())) {
				throw new IllegalArgumentException("[SSH] >> 傳入腳本資料為空");
			}

			Session session = ssh.startSession();
			session.allocateDefaultPTY();
			Shell shell = session.startShell();
			Expect expect = new ExpectBuilder()
					.withOutput(shell.getOutputStream())
					.withInputs(shell.getInputStream(), shell.getErrorStream())
					.withEchoInput(System.out)
					.withEchoOutput(System.err)
					.withInputFilters(removeColors(), removeNonPrintable())
					.withExceptionOnFailure()
					.build();

			StringBuilder processLog = new StringBuilder();
			try {
				String output;
				for (ScriptServiceVO vo : scriptList) {
					output = "";

					/*
					 * 預期命令送出後結束符號，針對VM設備的config檔因為內含有「#」符號，判斷會有問題
					 * e.g. 「#」 > 「NK-HeNBGW-04#」
					 */
					String expectedTerminalSymbol = vo.getExpectedTerminalSymbol();
					if (StringUtils.contains(expectedTerminalSymbol, Constants.DIR_PATH_DEVICE_NAME)) {
						expectedTerminalSymbol = StringUtils.replace(expectedTerminalSymbol, Constants.DIR_PATH_DEVICE_NAME, configInfoVO.getDeviceEngName());
					}

					String[] errorSymbols = StringUtils.isNotBlank(vo.getErrorSymbol()) ? vo.getErrorSymbol().split(Env.COMM_SEPARATE_SYMBOL) : null;

					// 送出命令
					output = expect.sendLine(replaceContentSign(vo.getScriptContent(), configInfoVO, vo.getRemark()))
							.expect(contains(expectedTerminalSymbol))
							.getBefore();

					processLog.append(output + expectedTerminalSymbol);

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
				throw e;

			} finally {
				log.info(processLog.toString());
				ssVO.setCmdProcessLog(processLog.toString());

				expect.close();
				session.close();
			}

		} catch (CommandExecuteException cee) {
			throw cee;
		} catch (Exception e) {
			throw new Exception("[SSH send command failed] >> " + e.getMessage());
		}

		return cmdOutputs;
	}

	@Override
	public boolean logout() throws Exception {
		try {
			checkSshStatus();

		} catch (Exception e) {
			throw new Exception("[SSH logout failed] >> " + e.getMessage());
		}
		return true;
	}

	@Override
	public boolean disconnect() throws Exception {
		try {
			checkSshStatus();

			ssh.disconnect();
			log.info("SSH disconnect success!!");

		} catch (Exception e) {
			throw new Exception("[SSH disconnect failed] >> " + e.getMessage());
		}
		return true;
	}

}
