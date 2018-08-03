package com.cmap.utils.impl;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;

import com.cmap.Env;
import com.cmap.dao.vo.ScriptListDAOVO;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.utils.ConnectUtils;

public class TelnetUtils implements ConnectUtils {
	
	private TelnetClient telnet = null;
	private InputStream in;
	private PrintStream out;
	private final String prompt = "$";
	
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
				telnet.connect("192.168.26.254", 23);
				System.out.println("Telnet connect success!");
				
				in = telnet.getInputStream();
				out = new PrintStream(telnet.getOutputStream());
				
				readUntil("Username: ");
				write("cisco");
				
				readUntil("Password: ");
				write("cisco");
				
				readUntil(">");
				write("enable");
				
				readUntil("Password: ");
				write("cisco");
				
				readUntil("#");
				write("terminal length 0");
				
				readUntil("#");
				write("show running-config");
				
				readUntil("#");
				
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean login(final String account, final String password) throws Exception {
		boolean result = false;
		try {
			
			
		} catch (Exception e) {
			
		}
		return false;
	}

	private void write(String cmd) {
		try {
			out.println(cmd);
			out.flush();
			System.out.println("cmd: "+cmd);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String readUntil(String pattern) throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			char lastChar = pattern.charAt(pattern.length()-1);
			char ch = (char)in.read();
			
			int runTime = 0;
			System.out.println("<readUntil>******************************************************************");
			while (true) {
				sb.append(ch);
				System.out.print(ch);
				
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						System.out.println("");
						System.out.println("</readUntil>******************************************************************");
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
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public List<String> sendCommands(List<ScriptListDAOVO> scriptList, ConfigInfoVO configInfoVO) throws Exception {
		try {
			checkTelnetStatus();
			
			String cmd = "show running-config";
			write(cmd);
//			return readUntil("end");
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			throw e;
		}
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
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
