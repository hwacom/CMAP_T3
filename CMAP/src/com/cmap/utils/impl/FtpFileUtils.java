package com.cmap.utils.impl;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.cmap.utils.FileUtils;

public class FtpFileUtils implements FileUtils {
	
	FTPClient ftp = null;
	
	public FtpFileUtils() {
		try {
			ftp = new FTPClient();
			ftp.setDefaultTimeout(DEFAULT_TIME_OUT);
			
			ftp.connect(SERVER_IP, SERVER_PORT);
			ftp.setConnectTimeout(CONNECT_TIME_OUT);
			ftp.setSoTimeout(CONNECT_TIME_OUT);
			
			int replyCode = ftp.getReplyCode();
			System.out.println(FTPReply.isPositiveCompletion(replyCode));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkFtpStatus() throws Exception {
		if (ftp == null) {
			throw new Exception("FTP status was down!");
		}
	}

	@Override
	public boolean connect(String account, String password) throws Exception {
		try {
			checkFtpStatus();
			
	        System.out.println(ftp.login(account, password));
	        
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}

	@Override
	public boolean changeDir(String targetDirPath) throws Exception {
		try {
			checkFtpStatus();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	

	@Override
	public FTPFile[] listFiles() throws Exception {
		FTPFile[] retVal = null;
		try {
			checkFtpStatus();
			
			retVal = ftp.listFiles();
			for (FTPFile ff : retVal) {
				System.out.println("[Name]: "+ff.getName()+", [Type]: "+ff.getType()+", [Size]: "+ff.getSize());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean uploadFiles() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect() throws Exception {
		try {
			checkFtpStatus();
			
			System.out.println(ftp.logout()); 
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (ftp != null) {
				ftp.disconnect();
			}
		}
		
		return true;
	}
}
