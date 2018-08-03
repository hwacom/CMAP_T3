package com.cmap.utils.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPClient;

import com.cmap.service.vo.VersionServiceVO;
import com.cmap.utils.FileUtils;

public class TFtpFileUtils implements FileUtils {
	private static Log log = LogFactory.getLog(TFtpFileUtils.class);
	
	private String hostIp;
	private Integer hostPort;
	
	TFTPClient tftp = null;
	
	public TFtpFileUtils() throws Exception {
		try {
			tftp = new TFTPClient();
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
			tftp = null;
			throw new Exception("[TFTP initial failed] >> " + e.toString());
		}
	}
	
	private void checkTFtpStatus() throws Exception {
		boolean conError = false;
		
		if (tftp == null) {
			conError = true;
			
		} else {
			if (!tftp.isOpen()) {
				conError = true;
			}
		}
		
		if (conError) {
			disconnect();
			throw new Exception("FTP connect interrupted!");
		}
	}

	@Override
	public boolean connect(final String hostIp, final Integer hostPort) throws Exception {
		try {
			this.hostIp = hostIp;
			this.hostPort = hostPort;
			
			if (tftp != null) {
				tftp.open();
				
//				tftp.setDefaultTimeout(Env.TFTP_DEFAULT_TIME_OUT);
//				tftp.setSoTimeout(Env.TFTP_SOCKET_TIME_OUT);
				
				System.out.println("TFTP opened");
			}
			
		} catch (Exception e) {
			throw new Exception("[TFTP open failed] >> " + e.toString());
		}
		
		return true;
	}
	
	@Override
	public boolean login(String account, String password) throws Exception {
		return true;
	}

	@Override
	public boolean changeDir(String targetDirPath, boolean createFolder) throws Exception {
		return true;
	}
	

	@Override
	public FTPFile[] listFiles() throws Exception {
		return null;
	}

	@Override
	public boolean uploadFiles(String fileName, InputStream inputStream) throws Exception {
		return true;
	}

	@Override
	public List<String> downloadFiles(VersionServiceVO vsVO) throws Exception {
		List<String> fileContentList = null;
		
		BufferedReader bufReader = null;
		InputStreamReader isReader = null;
		
		ByteArrayInputStream iStream = null;
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		try {
			checkTFtpStatus();
			fileContentList = new ArrayList<String>();
			
			String targetFileName = vsVO.getConfigFileDirPath().concat(File.separator).concat(vsVO.getFileFullName());
			
			tftp.receiveFile(targetFileName, TFTP.BINARY_MODE, oStream, hostIp, hostPort);
			
			iStream = new ByteArrayInputStream(oStream.toByteArray());
			isReader = new InputStreamReader(iStream, StandardCharsets.UTF_8);
			bufReader = new BufferedReader(isReader);
			
			String line;
			while ((line = bufReader.readLine()) != null) {
				fileContentList.add(line);
			}
			
			System.out.println("fileContentList.size: "+fileContentList.size());
			
		} catch (Exception e) {
			throw new Exception("[FTP download file failed] >> " + e.toString());
			
		} finally {
			if (oStream != null) {
				oStream.close();
			}
			if (iStream != null) {
				iStream.close();
			}
			if (isReader != null) {
				isReader.close();
			}
			if (bufReader != null) {
				bufReader.close();
			}
		}
		
		return fileContentList;
	}

	@Override
	public boolean disconnect() throws Exception {
		return true;
	}
}
