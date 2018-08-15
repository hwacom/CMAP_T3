package com.cmap.utils;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;

import com.cmap.service.vo.ConfigInfoVO;

public interface FileUtils {

	/**
	 * 開啟連線
	 * @param hostIp
	 * @param hostPort
	 * @return
	 * @throws Exception
	 */
	public boolean connect(final String hostIp, final Integer hostPort) throws Exception;

	/**
	 * 登入
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean login(final String account, final String password) throws Exception;

	/**
	 * 移動作業目錄
	 * @param targetDirPath
	 * @return
	 * @throws Exception
	 */
	public boolean changeDir(String targetDirPath, boolean createFolder) throws Exception;

	/**
	 * 列出當下所在目錄下的所有檔案清單
	 * @return
	 * @throws Exception
	 */
	public FTPFile[] listFiles() throws Exception;

	/**
	 * 上傳檔案
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFiles(String fileName, InputStream inputStream) throws Exception;

	/**
	 * 下載檔案，回傳結果以行列儲存於List內
	 * @return
	 * @throws Exception
	 */
	public List<String> downloadFiles(ConfigInfoVO ciVO) throws Exception;

	/**
	 * 下載檔案，回傳結果串接為單一字串
	 * @param ciVO
	 * @return
	 * @throws Exception
	 */
	public String downloadFilesString(ConfigInfoVO ciVO) throws Exception;

	/**
	 * 刪除檔案
	 * @param targetDirPath
	 * @param fileName
	 * @return
	 */
	public boolean deleteFiles(String targetDirPath, String fileName) throws Exception;

	/**
	 * 移動檔案
	 * @param sourceDirPath
	 * @param targetDirPath
	 * @return
	 * @throws Exception
	 */
	public boolean moveFiles(ConfigInfoVO ciVO, String sourceDirPath, String targetDirPath) throws Exception;

	/**
	 * 中斷連線
	 * @return
	 * @throws Exception
	 */
	public boolean disconnect() throws Exception;
}
