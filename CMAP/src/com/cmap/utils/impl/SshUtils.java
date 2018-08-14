package com.cmap.utils.impl;

import static net.sf.expectit.filter.Filters.removeColors;
import static net.sf.expectit.filter.Filters.removeNonPrintable;
import static net.sf.expectit.matcher.Matchers.contains;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.dao.vo.ScriptListDAOVO;
import com.cmap.exception.ConnectionException;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.utils.ConnectUtils;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Shell;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

public class SshUtils implements ConnectUtils {
	private static Log log = LogFactory.getLog(SshUtils.class);
	
	private SSHClient ssh = null;

	public SshUtils() throws Exception {
		if (ssh == null) {
			ssh = new SSHClient();
			ssh.setConnectTimeout(Env.SSH_CONNECT_TIME_OUT);
			ssh.setTimeout(Env.SSH_SOCKET_TIME_OUT);
			System.out.println("***************** [ SSH start ] ******************");
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
			System.out.println("SSH connect success!!");
			
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
			System.out.println("SSH login success!!");
			
		} catch (Exception e) {
			throw new Exception("[SSH login failed] >> " + e.getMessage());
		}
		return true;
	}
	
	private String replaceContentSign(String cmd, ConfigInfoVO configInfoVO, String remark) {
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
			
			if (StringUtils.isNotBlank(remark)) {
				tFtpFilePath = StringUtils.replace(tFtpFilePath, Env.COMM_SEPARATE_SYMBOL, remark);
			}
			
			cmd = StringUtils.replace(cmd, Env.SIGN_TFTP_OUTPUT_FILE_PATH, tFtpFilePath);
		}
		
		return cmd;
	}

	@Override
	public List<String> sendCommands(List<ScriptListDAOVO> scriptList, ConfigInfoVO configInfoVO) throws Exception {
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
	        
	        try {
	        	String output;
	        	for (ScriptListDAOVO vo : scriptList) {
	        		output = "";
	        		
	        		if (vo.getOutput() != null && vo.getOutput().equals(Constants.DATA_Y)) {
	        			output = expect.sendLine(replaceContentSign(vo.getScriptContent(), configInfoVO, vo.getRemark()))
	        						   .expect(contains(vo.getExpectedTerminalSymbol()))
	        						   .getBefore();
	        			
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
	        			
	        		} else {
	        			expect.sendLine(
	        					replaceContentSign(vo.getScriptContent(), configInfoVO, vo.getRemark()))	//替換特殊字元內容
 					   		  	.expect(contains(vo.getExpectedTerminalSymbol()));
	        		}
	        	}
	        	/*
	        	expect.sendLine("cisco").expect(contains("#"));
	        	expect.sendLine("terminal length 0").expect(contains("#"));
	        	String fullStr = expect.sendLine("sh run").expect(contains("#")).getBefore();
	        	cmdResult = cutContent(fullStr, 3, 2, System.lineSeparator());
	        	System.out.println("*****************************************************************");
	        	System.out.println(cmdResult);
	        	System.out.println("*****************************************************************");
	        	*/
	        	
	        } finally {
	            expect.close();
	            session.close();
	        }
			
		} catch (Exception e) {
			throw new Exception("[SSH send command failed] >> " + e.getMessage());
		}
		
		return cmdOutputs;
	}
	
	/**
	 * 命令回傳結果內容處理
	 * @param content 原始回傳內容
	 * @param headCutCount 開頭往後要去除的行數
	 * @param tailCutCount 結尾往前要去除的行數
	 * @param splitBy 行段落用什麼符號分行
	 * @return
	 */
	private String cutContent(String content, int headCutCount, int tailCutCount, String splitBy) {
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
			System.out.println("SSH disconnect success!!");
			
		} catch (Exception e) {
			throw new Exception("[SSH disconnect failed] >> " + e.getMessage());
		}
		return true;
	}

}
