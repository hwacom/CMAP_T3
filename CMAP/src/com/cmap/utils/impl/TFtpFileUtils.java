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

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmap.Env;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.utils.FileUtils;

public class TFtpFileUtils implements FileUtils {
	private static Logger log = LoggerFactory.getLogger(TFtpFileUtils.class);

	private String hostIp;
	private Integer hostPort;

	TFTPClient tftp = null;

	public TFtpFileUtils() throws Exception {
		try {
			tftp = new TFTPClient();

		} catch (Exception e) {
			log.error(e.toString(), e);

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
			throw new Exception("[TFTP connect interrupted!]");
		}
	}

	@Override
	public boolean connect(final String hostIp, final Integer hostPort) throws Exception {
		try {
			this.hostIp = hostIp;
			this.hostPort = hostPort;

			if (tftp != null) {
				tftp.open();

				tftp.setDefaultTimeout(Env.TFTP_DEFAULT_TIME_OUT);
				tftp.setSoTimeout(Env.TFTP_SOCKET_TIME_OUT);

				log.info("[TFTP 'connect' opened] >> ip: "+hostIp+", port: "+hostPort);
			}

		} catch (Exception e) {
			throw new Exception("[TFTP 'connect' open failed] >> " + e.toString());
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
		try {
			log.info("[uploadFiles] >> tftp.sendFile");
			tftp.sendFile(fileName, TFTP.BINARY_MODE, inputStream, hostIp, 69);
			log.info("[uploadFiles] >> tftp.sendFile success!!");

		} catch (Exception e) {
			log.error(e.toString(), e);
			return false;

		} finally {
			if (inputStream != null) {
				inputStream.close();
			}

			disconnect();
			connect(hostIp, hostPort);
		}
		return true;
	}

	@Override
	public List<String> downloadFiles(ConfigInfoVO ciVO) throws Exception {
		List<String> fileContentList = null;

		BufferedReader bufReader = null;
		InputStreamReader isReader = null;

		ByteArrayInputStream iStream = null;
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		try {
			checkTFtpStatus();
			fileContentList = new ArrayList<>();

			String targetFileName = ciVO.getConfigFileDirPath().concat(File.separator).concat(ciVO.getFileFullName());
			log.info("[TFTP dowloadFiles] >> targetFileName: "+targetFileName);

			if (hostPort != null) {
				tftp.receiveFile(targetFileName, TFTP.BINARY_MODE, oStream, hostIp, hostPort);
			} else {
				tftp.receiveFile(targetFileName, TFTP.BINARY_MODE, oStream, hostIp);
			}

			iStream = new ByteArrayInputStream(oStream.toByteArray());
			isReader = new InputStreamReader(iStream, StandardCharsets.UTF_8);
			bufReader = new BufferedReader(isReader);

			String line;
			while ((line = bufReader.readLine()) != null) {
				fileContentList.add(line);
			}

			log.info("[TFTP dowloadFiles] >> fileContentList.size: "+fileContentList.size());

		} catch (Exception e) {
			throw new Exception("[TFTP download file failed] >> ip: " + hostIp + ", port: " + hostPort + ", error: " + e.toString());

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

			disconnect();
			connect(hostIp, hostPort);
		}

		return fileContentList;
	}

	@Override
	public String downloadFilesString(ConfigInfoVO ciVO) throws Exception {
		StringBuilder fileContent = new StringBuilder();

		BufferedReader bufReader = null;
		InputStreamReader isReader = null;

		ByteArrayInputStream iStream = null;
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		try {
			checkTFtpStatus();

			String targetFileName = ciVO.getConfigFileDirPath().concat(File.separator).concat(ciVO.getFileFullName());

			log.info("[uploadFiles] >> tftp.receiveFile");
			tftp.receiveFile(targetFileName, TFTP.BINARY_MODE, oStream, hostIp, hostPort);
			log.info("[uploadFiles] >> tftp.receiveFile success!!");

			iStream = new ByteArrayInputStream(oStream.toByteArray());
			isReader = new InputStreamReader(iStream, StandardCharsets.UTF_8);
			bufReader = new BufferedReader(isReader);

			int value = 0;
			while ((value = bufReader.read()) != -1) {
				char c = (char)value;
				fileContent.append(c);
			}

		} catch (Exception e) {
			throw new Exception("[TFTP download file failed] >> " + e.toString());

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

			disconnect();
			connect(hostIp, hostPort);
		}

		return fileContent.toString();
	}

	@Override
	public boolean disconnect() throws Exception {
		tftp.close();
		return true;
	}

	@Override
	public boolean deleteFiles(String targetDirPath, String fileName) throws Exception {
		try {


		} catch (Exception e) {
			log.error(e.toString(), e);
			return false;
		}

		return true;
	}

	@Override
	public boolean moveFiles(ConfigInfoVO ciVO, String sourceDirPath, String targetDirPath) throws Exception {
		try {
			/*
			 * 先從source目錄下載檔案，再上傳至target目錄
			 */
			ConfigInfoVO tmpVO = (ConfigInfoVO)ciVO.clone();
			tmpVO.setConfigFileDirPath(sourceDirPath);

			log.info("[moveFiles] >> downloadFilesString");
			final String fileString = downloadFilesString(tmpVO);
			log.info("[moveFiles] >> uploadFiles");
			uploadFiles(targetDirPath, IOUtils.toInputStream(fileString));

		} catch (Exception e) {
			throw e;
		}

		return true;
	}
}
