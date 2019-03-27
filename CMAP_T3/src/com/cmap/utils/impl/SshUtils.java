package com.cmap.utils.impl;

import static net.sf.expectit.filter.Filters.removeColors;
import static net.sf.expectit.filter.Filters.removeNonPrintable;
import static net.sf.expectit.matcher.Matchers.contains;
import static net.sf.expectit.matcher.Matchers.eof;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.exception.CommandExecuteException;
import com.cmap.exception.ConnectionException;
import com.cmap.service.vo.CommonServiceVO;
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
				throw new IllegalStateException("SSH connect interrupted! >>> [ " + ipAddress + ":" + port + " ]");
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
			log.info("SSH connect success!! >>> [ " + ipAddress + ":" + port + " ]");

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
			log.info("SSH login success!! >>> [ account: " + account + " , password: " + password + " ]");

		} catch (Exception e) {
			throw new Exception("[SSH login failed] >> " + e.getMessage());
		}
		return true;
	}

	private CommonServiceVO sendCommand(CommonServiceVO csVO, Expect expect, ConfigInfoVO configInfoVO, ScriptServiceVO scriptVO, StringBuilder processLog, List<String> cmdOutputs) throws Exception {
		String output = "";
		String[] errorSymbols = StringUtils.isNotBlank(scriptVO.getErrorSymbol()) ? scriptVO.getErrorSymbol().split(Env.COMM_SEPARATE_SYMBOL) : null;

		/*
		 * 預期命令送出後結束符號，針對VM設備的config檔因為內含有「#」符號，判斷會有問題
		 * e.g. 「#」 > 「NK-HeNBGW-04#」
		 */
		String expectedTerminalSymbol = replaceExpectedTerminalSymbol(scriptVO.getExpectedTerminalSymbol(), configInfoVO);

		int runTime = 1;	// 迴圈執行次數

		List<String> cmdsList = configInfoVO.getConfigContentList();

		if (StringUtils.equals(scriptVO.getRepeatFlag(), Constants.DATA_Y)) {
			runTime = cmdsList.size();
		}

		for (int i=0; i<runTime; i++) {
			String cli = (cmdsList != null && !cmdsList.isEmpty()) ? cmdsList.get(i) : null;
			String sendCli = replaceContentSign(csVO, scriptVO, configInfoVO, cli);
			output = sendCli;

			/*
			 * 替換腳本參數(replaceContentSign)後送出命令，並等候至預期的結束符號(expectedTerminalSymbol)，將output結果取出
			 */
			final String cmdRemark = scriptVO.getRemark();
			if (StringUtils.equals(cmdRemark, Constants.SCRIPT_REMARK_OF_NO_EXPECT)) {
				expect.sendLine(sendCli);

			} else if (StringUtils.equals(cmdRemark, Constants.SCRIPT_REMARK_OF_NEW_LINE)) {
				output = expect.sendLine()
								.expect(contains(expectedTerminalSymbol))
								.getBefore();

			} else if (StringUtils.contains(cmdRemark, Constants.SCRIPT_REMARK_OF_WAIT_AND_TIMEOUT)) {
				int time = cmdRemark.indexOf("+") != -1 ? Integer.valueOf(cmdRemark.substring(cmdRemark.indexOf("+") + 1)) : 10;

				try {
					expect.sendLine(sendCli).withTimeout(time, TimeUnit.SECONDS);
					expect.expect(contains(expectedTerminalSymbol));

				} catch (Exception e) {
					// 送出 reload 指令後設備不會有回應，Timeout不處理
					e.printStackTrace();
					csVO.setNeedCloseSession(false); // 送出 reload 指令後設備不會有回應，Session會被reset，以此標記在finally時不需做session.close，否則會報錯
				}

			} else if (StringUtils.equals(cmdRemark, Constants.SCRIPT_REMARK_OF_DISCONNECT)) {
				try {
					expect.sendLine(sendCli)
							.expect(contains(expectedTerminalSymbol));

				} catch (Exception e) {
					// 送出 reload 指令後設備不會有回應，Timeout不處理
					e.printStackTrace();
					csVO.setNeedCloseSession(false); // 送出 reload 指令後設備不會有回應，Session會被reset，以此標記在finally時不需做session.close，否則會報錯
				}

			} else if (StringUtils.equals(cmdRemark, Constants.SCRIPT_REMARK_OF_EXIT)) {
				expect.sendLine(sendCli)
						.expect(eof());

			} else {
				output = expect.sendLine(sendCli)
						   .expect(contains(expectedTerminalSymbol))
						   .getBefore();
			}

			processLog.append(output + expectedTerminalSymbol);

			boolean success = true;

			if (errorSymbols != null) {
				/*
				 * 判斷當前命令執行結果是否有錯
				 */
				for (String errSymbol : errorSymbols) {
					success = output.toUpperCase().contains(errSymbol) ? false : true;

					if (!success) {
						throw new CommandExecuteException("[Command execute failed!] >> output: " + output);
					}
				}
			}

			if (success) {
				if (scriptVO.getOutput() != null && scriptVO.getOutput().equals(Constants.DATA_Y)) {
					csVO = processOutput(csVO, scriptVO, output, cmdOutputs);
				}
			}
		}

		return csVO;
	}

	@Override
	public List<String> sendCommands(List<ScriptServiceVO> scriptList, ConfigInfoVO configInfoVO, StepServiceVO ssVO) throws Exception {
		List<String> cmdOutputs = new ArrayList<>();
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
					.withTimeout(Env.DEFAULT_SSH_RESPONSE_TIMEOUT_SECONDS, TimeUnit.SECONDS)	// 預設Timeout為10秒
					.withExceptionOnFailure()
					.build();

			CommonServiceVO csVO = new CommonServiceVO();
			StringBuilder processLog = new StringBuilder();
			try {
				for (ScriptServiceVO scriptVO : scriptList) {
					// 送出命令
					csVO = sendCommand(csVO, expect, configInfoVO, scriptVO, processLog, cmdOutputs);
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
				//log.info(processLog.toString());
				ssVO.setCmdProcessLog(processLog.toString());

				expect.close();

				if (csVO.isNeedCloseSession()) {
					session.close();
				}
			}

		} catch (CommandExecuteException cee) {
			throw cee;
		} catch (Exception e) {
			log.error(e.toString(), e);
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
