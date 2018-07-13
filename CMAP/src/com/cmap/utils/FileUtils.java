package com.cmap.utils;

import org.apache.commons.net.ftp.FTPFile;

public interface FileUtils {
	
	public final static String SERVER_IP = "192.168.26.150";
	public final static int SERVER_PORT = 21;
	public final static int DEFAULT_TIME_OUT = 5000;
	public final static int CONNECT_TIME_OUT = 600000;

	public boolean connect(String account, String password) throws Exception;
	
	public boolean changeDir(String targetDirPath) throws Exception;
	
	public FTPFile[] listFiles() throws Exception;
	
	public boolean uploadFiles() throws Exception;
	
	public boolean disconnect() throws Exception;
}
