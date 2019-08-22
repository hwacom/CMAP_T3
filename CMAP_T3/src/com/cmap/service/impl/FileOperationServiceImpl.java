package com.cmap.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Constants;
import com.cmap.annotation.Log;
import com.cmap.dao.JobFileOperationSettingDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.JobFileOperationSetting;
import com.cmap.service.FileOperationService;
import com.cmap.service.impl.jobs.BaseJobImpl.Result;
import com.cmap.service.vo.FileOperationServiceVO;
import com.cmap.utils.impl.FtpFileUtils;
import com.cmap.utils.impl.SFtpFileUtils;

@Service("fileOperationService")
@Transactional
public class FileOperationServiceImpl implements FileOperationService {
	@Log
	private static Logger log;

	@Autowired
	private JobFileOperationSettingDAO jobFileOperationSettingDAO;

	private boolean checkFile(String fileName, long fileSizeInByte, String fileNameRegex, String fileSizeRegex) {
		boolean matched = false;

		// Step 1. 先篩選檔案名稱格式
		if (fileName.matches(fileNameRegex)) {

			// Step 2. 若有設定篩選檔案大小，則進行比對
			if (StringUtils.isNotBlank(fileSizeRegex)) {
				String[] regex = fileSizeRegex.split(" ");
				String operator = regex[0];
				String size = regex[1];
				String unit = regex[2];

				long sizeInByte = 0;

				//轉換SIZE單位成BYTE
				switch (unit) {
					case "GB":
						sizeInByte = Integer.valueOf(size)*1024*1024*1024;
						break;

					case "MB":
						sizeInByte = Integer.valueOf(size)*1024*1024;
						break;

					case "KB":
						sizeInByte = Integer.valueOf(size)*1024;
						break;
				}

				boolean isMatched = false;
				switch (operator) {
					case ">=":
						isMatched = fileSizeInByte >= sizeInByte;
						break;

					case ">":
						isMatched = fileSizeInByte > sizeInByte;
						break;

					case "=":
						isMatched = fileSizeInByte == sizeInByte;
						break;

					case "<":
						isMatched = fileSizeInByte < sizeInByte;
						break;

					case "<=":
						isMatched = fileSizeInByte <= sizeInByte;
						break;
				}

				if (isMatched) {
					matched = true;
				}

			} else {
				//沒有設定檔案大小篩選情況
				matched = true;
			}
		}

		return matched;
	}

	private FileOperationServiceVO executeLocalFile(FileOperationServiceVO retVO, JobFileOperationSetting setting) throws IOException {
		Stream<Path> filePaths = Files.list(Paths.get(setting.getSourceDir()));
		final String fileNameRegex = setting.getSourceFileNameRegex();
		final String fileSizeRegex = setting.getSourceFileSizeRegex();

		List<Path> matchedFile = new ArrayList<>();
		for (Iterator<Path> it = filePaths.iterator(); it.hasNext();) {
			Path path = it.next();

			if (path.toFile().isDirectory()) {
				//如果是資料夾的話直接跳過
				continue;
			}

			String fileName = path.getFileName().toString();
			long fileSizeInByte = FileUtils.sizeOf(path.toFile());

			boolean matched = checkFile(fileName, fileSizeInByte, fileNameRegex, fileSizeRegex);

			if (matched) {
				matchedFile.add(path);
			}
		}

		if (matchedFile != null && !matchedFile.isEmpty()) {
			final String action = setting.getDoAction();

			switch (action) {
				case Constants.JOB_FILE_OPERATE_ACTION_CUT:
					retVO = doFileCut(matchedFile, setting);
					break;

				case Constants.JOB_FILE_OPERATE_ACTION_COPY:
					retVO = doFileCopy(matchedFile, setting);
					break;

				case Constants.JOB_FILE_OPERATE_ACTION_DELETE:
					retVO = doFileDelete(matchedFile, setting);
					break;

				case Constants.JOB_FILE_OPERATE_ACTION_RENAME:
					break;
			}

		} else {
			retVO.setJobExcuteResult(Result.SUCCESS);
			retVO.setJobExcuteResultRecords("0");
			retVO.setJobExcuteRemark("無符合檔案需搬移");
		}

		return retVO;
	}

	private FileOperationServiceVO executeFtpFile(FileOperationServiceVO retVO, JobFileOperationSetting setting) throws Exception {
		final String remoteHostIp = setting.getRemoteHostIp();
		final Integer remoteHostPort = setting.getRemoteHostPort();
		final String remoteLoginAccount = setting.getRemoteLoginAccount();
		final String remoteLoginPassword = setting.getRemoteLoginPassword();
		final String fileNameRegex = setting.getSourceFileNameRegex();
		final String fileSizeRegex = setting.getSourceFileSizeRegex();
		final String sourceDir = setting.getSourceDir();

		FtpFileUtils ftp = new FtpFileUtils();
		ftp.connect(remoteHostIp, remoteHostPort);
		ftp.login(remoteLoginAccount, remoteLoginPassword);
		ftp.changeDir(sourceDir, false);

		List<String> matchedFile = new ArrayList<>();
		FTPFile[] files = ftp.listFiles();
		for (FTPFile file : files) {
			final String fileName = file.getName();
			long fileSizeInByte = file.getSize();

			boolean matched = checkFile(fileName, fileSizeInByte, fileNameRegex, fileSizeRegex);

			if (matched) {
				matchedFile.add(fileName);
			}
		}

		if (matchedFile != null && !matchedFile.isEmpty()) {
			final String action = setting.getDoAction();

			switch (action) {
				case Constants.JOB_FILE_OPERATE_ACTION_CUT:
					retVO = doFtpFileCut(matchedFile, setting, ftp);
					break;

				case Constants.JOB_FILE_OPERATE_ACTION_COPY:
					retVO = doFtpFileCopy(matchedFile, setting, ftp);
					break;

				case Constants.JOB_FILE_OPERATE_ACTION_DELETE:
					retVO = doFtpFileDelete(matchedFile, setting, ftp);
					break;

				case Constants.JOB_FILE_OPERATE_ACTION_RENAME:
					break;
			}

		} else {
			retVO.setJobExcuteResult(Result.SUCCESS);
			retVO.setJobExcuteResultRecords("0");
			retVO.setJobExcuteRemark("無符合檔案需搬移");
		}

		return retVO;
	}

	private FileOperationServiceVO executeSFtpFile(FileOperationServiceVO retVO, JobFileOperationSetting setting) throws Exception {
        final String remoteHostIp = setting.getRemoteHostIp();
        final Integer remoteHostPort = setting.getRemoteHostPort();
        final String remoteLoginAccount = setting.getRemoteLoginAccount();
        final String remoteLoginPassword = setting.getRemoteLoginPassword();
        final String fileNameRegex = setting.getSourceFileNameRegex();
        final String fileSizeRegex = setting.getSourceFileSizeRegex();
        final String sourceDir = setting.getSourceDir();

        SFtpFileUtils sftp = new SFtpFileUtils();
        sftp.connectAndLogin(remoteHostIp, remoteHostPort, remoteLoginAccount, remoteLoginPassword);
        sftp.changeDir(sourceDir, false);

        List<String> matchedFile = new ArrayList<>();
        FTPFile[] files = sftp.listFiles();
        for (FTPFile file : files) {
            final String fileName = file.getName();
            long fileSizeInByte = file.getSize();

            boolean matched = checkFile(fileName, fileSizeInByte, fileNameRegex, fileSizeRegex);

            if (matched) {
                matchedFile.add(fileName);
            }
        }

        if (matchedFile != null && !matchedFile.isEmpty()) {
            final String action = setting.getDoAction();

            switch (action) {
                case Constants.JOB_FILE_OPERATE_ACTION_CUT:
                    retVO = doSFtpFileCut(matchedFile, setting, sftp);
                    break;
            }

        } else {
            retVO.setJobExcuteResult(Result.SUCCESS);
            retVO.setJobExcuteResultRecords("0");
            retVO.setJobExcuteRemark("無符合檔案需搬移");
        }

        return retVO;
    }

	@Override
	public FileOperationServiceVO executeFileOperation(String settingId) throws ServiceLayerException {
		FileOperationServiceVO retVO = new FileOperationServiceVO();
		try {
			JobFileOperationSetting setting = jobFileOperationSettingDAO.findJobFileOperationSettingById(settingId);

			if (setting == null) {
				retVO.setJobExcuteResult(Result.FAILED);
				retVO.setJobExcuteResultRecords("0");
				retVO.setJobExcuteRemark("查無 JobFileOperationSetting 設定 >>> settingId: " + settingId);

			} else {
				final String getSourceMethod = setting.getGetSourceMethod();

				switch (getSourceMethod) {
					case Constants.DATA_POLLER_FILE_BY_LOCAL_DIR:
						retVO = executeLocalFile(retVO, setting);
						break;

					case Constants.DATA_POLLER_FILE_BY_FTP:
						retVO = executeFtpFile(retVO, setting);
						break;

					case Constants.DATA_POLLER_FILE_BY_SFTP:
                        retVO = executeSFtpFile(retVO, setting);
                        break;
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retVO;
	}

	private void moveFile(Path sourcePath, Path targetPath, boolean createDir, boolean deleteSourceFile) throws FileSystemException,ServiceLayerException {
		try {
			Path targetFileDirPath = targetPath.getParent();

			if (!Files.exists(targetFileDirPath)) {
				try {
					Files.createDirectories(targetFileDirPath);

				} catch (IOException e) {
					log.error(e.toString(), e);
					throw new ServiceLayerException("建立搬移目標資料夾失敗");
				}
			}

			Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

			if (deleteSourceFile) {
				Files.deleteIfExists(sourcePath);
			}

		} catch (FileSystemException jse) {
			//log.error(jse.toString());
			throw jse;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("moveFile 發生異常 >>> " + e.toString());
		}
	}

	private String composeTargetFileName(final String sourceFileName, final String targetFileNameFormat) throws ServiceLayerException {
		String targetFileName = targetFileNameFormat;

		try {
			//原始檔名
			if (targetFileName.contains(Constants.FILE_FORMAT_ORI_FILE_NAME)) {
				String oriFileName = sourceFileName.substring(0, sourceFileName.lastIndexOf("."));
				targetFileName = targetFileName.replace(
											Constants.FILE_FORMAT_ORI_FILE_NAME, oriFileName);
			}
			//日期戳記
			if (targetFileName.contains(Constants.FILE_FORMAT_YYYYMMDDHH24MISS)) {
				Calendar cal = Calendar.getInstance();
				String dateStr = Constants.FORMAT_YYYYMMDD_HH24MISS_NOSYMBOL.format(cal.getTime());
				targetFileName = targetFileName.replace(
											Constants.FILE_FORMAT_YYYYMMDDHH24MISS, dateStr);
			}

			//副檔名
			if (targetFileName.contains(Constants.FILE_FORMAT_FILE_EXT)) {
				String extName = sourceFileName.substring(sourceFileName.lastIndexOf(".") + 1);
				targetFileName = targetFileName.replace(
											Constants.FILE_FORMAT_FILE_EXT, extName);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("moveFile 發生異常 >>> " + e.toString());
		}
		return targetFileName;
	}

	private FileOperationServiceVO composeResultVO(String action, int totalCount, int successCount, int lockCount) {
		FileOperationServiceVO retVO = new FileOperationServiceVO();

		retVO.setJobExcuteResult(
				successCount == 0 ? Result.FAILED : (totalCount != successCount)
															? Result.PARTIAL_SUCCESS : Result.SUCCESS);
		retVO.setJobExcuteResultRecords(String.valueOf(totalCount));

		int errorCount = totalCount - lockCount - successCount;
		retVO.setJobExcuteRemark(action + ", 總筆數: " + totalCount + ", 成功: " + successCount + " 筆,  Lock: " + lockCount + " 筆,  失敗: " + errorCount + " 筆");

		return retVO;
	}

	private FileOperationServiceVO doFileCut(List<Path> matchedFile, JobFileOperationSetting setting) {
		int totalCount = matchedFile.size();
		int successCount = 0;
		int lockCount = 0;

		for (Path sourcePath : matchedFile) {
			try {
				String sourceFileName = sourcePath.getFileName().toString();
				String targetFileNameFormat = setting.getTargetFileNameFormat();
				String targetDir = setting.getTargetDir();
				boolean targetDirByDay = StringUtils.equals(setting.getTargetDirByDay(), Constants.DATA_Y)
												? true : false;
				String targetDayDirName = targetDirByDay ? Constants.FORMAT_YYYY_MM_DD.format(new Date()) : "";

				String targetFileName = composeTargetFileName(sourceFileName, targetFileNameFormat);
				Path targetPath =
						Paths.get(
								targetDir +
								File.separator +
								(targetDirByDay ? (targetDayDirName + File.separator) : "") +
								targetFileName
						);

				moveFile(sourcePath, targetPath, true, true);
				successCount++;

			} catch (FileSystemException fse) {
				lockCount++;
				continue;

			} catch (ServiceLayerException sle) {
				continue;

			} catch (Exception e) {
				log.error(e.toString(), e);
				continue;
			}
		};

		return composeResultVO(Constants.JOB_FILE_OPERATE_ACTION_CUT, totalCount, successCount, lockCount);
	}

	private FileOperationServiceVO doFileCopy(List<Path> matchedFile, JobFileOperationSetting setting) {
		int totalCount = matchedFile.size();
		int successCount = 0;

		for (Path sourcePath : matchedFile) {
			try {
				String sourceFileName = sourcePath.getFileName().toString();
				String targetFileNameFormat = setting.getTargetFileNameFormat();
				String targetDir = setting.getTargetDir();
				boolean targetDirByDay = StringUtils.equals(setting.getTargetDirByDay(), Constants.DATA_Y)
												? true : false;
				String targetDayDirName = targetDirByDay ? Constants.FORMAT_YYYY_MM_DD.format(new Date()) : "";

				String targetFileName = composeTargetFileName(sourceFileName, targetFileNameFormat);
				Path targetPath =
						Paths.get(
								targetDir +
								File.separator +
								(targetDirByDay ? (targetDayDirName + File.separator) : "") +
								targetFileName
						);

				moveFile(sourcePath, targetPath, true, false);
				successCount++;

			} catch (ServiceLayerException sle) {
				log.error(sle.toString());
				continue;

			} catch (Exception e) {
				log.error(e.toString(), e);
				continue;
			}
		};

		return composeResultVO(Constants.JOB_FILE_OPERATE_ACTION_CUT, totalCount, successCount, 0);
	}

	private FileOperationServiceVO doFileDelete(List<Path> matchedFile, JobFileOperationSetting setting) {
		int totalCount = matchedFile.size();
		int successCount = 0;

		for (Path deletePath : matchedFile) {
			try {
				Files.deleteIfExists(deletePath);
				successCount++;

			} catch (Exception e) {
				log.error(e.toString(), e);
				continue;
			}
		};

		return composeResultVO(Constants.JOB_FILE_OPERATE_ACTION_CUT, totalCount, successCount, 0);
	}

	private FileOperationServiceVO doFtpFileCut(List<String> matchedFile, JobFileOperationSetting setting, FtpFileUtils ftp) {
		int totalCount = matchedFile.size();
		int successCount = 0;
		int lockCount = 0;

		final String targetFileNameFormat = setting.getTargetFileNameFormat();
		final String targetDir = setting.getTargetDir();
		boolean targetDirByDay = StringUtils.equals(setting.getTargetDirByDay(), Constants.DATA_Y)
										? true : false;
		String targetDayDirName = targetDirByDay ? Constants.FORMAT_YYYY_MM_DD.format(new Date()) : "";

		String targetFileName = null;
		String targetFilePath = null;
		FileOutputStream fos = null;
		for (String sourceFileName : matchedFile) {
			try {
				targetFileName = composeTargetFileName(sourceFileName, targetFileNameFormat);
				targetFilePath = targetDir +
										File.separator +
										(targetDirByDay ? (targetDayDirName + File.separator) : "") +
										targetFileName;
				fos = new FileOutputStream(targetFilePath, false);
				boolean success = ftp.retrieveFile(sourceFileName, fos);

				if (success) {
					//下載成功後將FTP上檔案刪除
					success = ftp.deleteFile(sourceFileName);

					if (!success) {
						//若刪除失敗(可能檔案被lock中)，則將先前下載的檔案刪除，避免重複處理
						Path localFilePath = Paths.get(targetFilePath);

						if (Files.exists(localFilePath)) {
							Files.delete(localFilePath);
						}
					}
				}

			} catch (Exception e) {
				log.error(e.toString(), e);
				continue;
			}
		};

		return composeResultVO(Constants.JOB_FILE_OPERATE_ACTION_CUT, totalCount, successCount, lockCount);
	}

	private FileOperationServiceVO doSFtpFileCut(List<String> matchedFile, JobFileOperationSetting setting, SFtpFileUtils sftp) {
        int totalCount = matchedFile.size();
        int successCount = 0;
        int lockCount = 0;

        final String targetFileNameFormat = setting.getTargetFileNameFormat();
        final String targetDir = setting.getTargetDir();
        boolean targetDirByDay = StringUtils.equals(setting.getTargetDirByDay(), Constants.DATA_Y)
                                        ? true : false;
        String targetDayDirName = targetDirByDay ? Constants.FORMAT_YYYY_MM_DD.format(new Date()) : "";

        String targetFileName = null;
        String targetFilePath = null;
        FileOutputStream fos = null;
        for (String sourceFileName : matchedFile) {
            try {
                targetFileName = composeTargetFileName(sourceFileName, targetFileNameFormat);
                targetFilePath = targetDir +
                                        File.separator +
                                        (targetDirByDay ? (targetDayDirName + File.separator) : "") +
                                        targetFileName;
                fos = new FileOutputStream(targetFilePath, false);
                boolean success = sftp.retrieveFile(sourceFileName, fos);

                if (success) {
                    //下載成功後將FTP上檔案刪除
                    success = sftp.deleteFile(sourceFileName);

                    if (!success) {
                        //若刪除失敗(可能檔案被lock中)，則將先前下載的檔案刪除，避免重複處理
                        Path localFilePath = Paths.get(targetFilePath);

                        if (Files.exists(localFilePath)) {
                            Files.delete(localFilePath);
                        }
                    }
                }

            } catch (Exception e) {
                log.error(e.toString(), e);
                continue;
            }
        };

        return composeResultVO(Constants.JOB_FILE_OPERATE_ACTION_CUT, totalCount, successCount, lockCount);
    }

	private FileOperationServiceVO doFtpFileCopy(List<String> matchedFile, JobFileOperationSetting setting, FtpFileUtils ftp) {
		int totalCount = matchedFile.size();
		int successCount = 0;
		int lockCount = 0;

		final String targetFileNameFormat = setting.getTargetFileNameFormat();
		final String targetDir = setting.getTargetDir();
		boolean targetDirByDay = StringUtils.equals(setting.getTargetDirByDay(), Constants.DATA_Y)
										? true : false;
		String targetDayDirName = targetDirByDay ? Constants.FORMAT_YYYY_MM_DD.format(new Date()) : "";

		String targetFileName = null;
		String targetFilePath = null;
		FileOutputStream fos = null;
		for (String sourceFileName : matchedFile) {
			try {
				targetFileName = composeTargetFileName(sourceFileName, targetFileNameFormat);
				targetFilePath = targetDir +
										File.separator +
										(targetDirByDay ? (targetDayDirName + File.separator) : "") +
										targetFileName;
				fos = new FileOutputStream(targetFilePath, false);
				ftp.retrieveFile(sourceFileName, fos);

			} catch (Exception e) {
				log.error(e.toString(), e);
				continue;
			}
		};

		return composeResultVO(Constants.JOB_FILE_OPERATE_ACTION_CUT, totalCount, successCount, lockCount);
	}

	private FileOperationServiceVO doFtpFileDelete(List<String> matchedFile, JobFileOperationSetting setting, FtpFileUtils ftp) {
		int totalCount = matchedFile.size();
		int successCount = 0;
		int lockCount = 0;

		for (String sourceFileName : matchedFile) {
			try {
				ftp.deleteFile(sourceFileName);

			} catch (Exception e) {
				log.error(e.toString(), e);
				continue;
			}
		};

		return composeResultVO(Constants.JOB_FILE_OPERATE_ACTION_CUT, totalCount, successCount, lockCount);
	}
}
