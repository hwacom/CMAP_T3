package com.cmap.utils.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.sftp.SftpClientFactory;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cmap.Env;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.utils.FileUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFtpFileUtils implements FileUtils {
	private static Logger log = LoggerFactory.getLogger(SFtpFileUtils.class);

	private ChannelSftp command;
    private Session session;

	public SFtpFileUtils() throws Exception {
		try {

		} catch (Exception e) {
			log.error(e.toString(), e);

			throw new Exception("[SFTP initial failed] >> " + e.toString());
		}
	}

	private void checkSFtpStatus() throws Exception {
//		boolean conError = false;
//
//		if (sftp == null) {
//			conError = true;
//
//		} else {
//			if (!sftp.isConnected() || !sftp.isAvailable()) {
//				conError = true;
//			}
//		}
//
//		if (conError) {
//			disconnect();
//			throw new Exception("SFTP connect interrupted!");
//		}
	}

	@Override
    public boolean connectAndLogin(String hostIp, Integer hostPort, String account, String password)
            throws Exception {
	    try {
            //If the client is already connected, disconnect
            if (command != null) {
                disconnect();
            }
            FileSystemOptions fso = new FileSystemOptions();
            try {
                SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(fso, "no");
                session = SftpClientFactory.createConnection(
                        hostIp, hostPort, account.toCharArray(), password.toCharArray(), fso);
                Channel channel = session.openChannel("sftp");
                channel.connect();
                command = (ChannelSftp) channel;

            } catch (FileSystemException e) {
                e.printStackTrace();
                return false;
            }
            return command.isConnected();

        } catch (Exception e) {
            throw new Exception("[SFTP connect failed] >> " + e.toString());
        }
    }

	@Override
	public boolean connect(final String hostIp, final Integer hostPort) throws Exception {
		try {

		} catch (Exception e) {
			throw new Exception("[SFTP connect failed] >> " + e.toString());
		}
		return true;
	}

	@Override
	public boolean login(String account, String password) throws Exception {
		try {

		} catch (Exception e) {
			throw new Exception("[FTP connect failed] >> " + e.toString());
		}
		return true;
	}

	@Override
	public boolean changeDir(String targetDirPath, boolean createFolder) throws Exception {
		try {
		    try {
	            command.cd(targetDirPath);

	        } catch (SftpException e) {
	            return false;
	        }

		} catch (Exception e) {
			throw new Exception("[SFTP change direction failed] >> " + e.toString());
		}

		return true;
	}


	@Override
	public FTPFile[] listFiles() throws Exception {
	    FTPFile[] fileList = null;
        try {
            String currentDir = command.pwd();
            Vector<?> vector = command.ls(currentDir);

            if (vector != null && vector.size() > 0) {
                fileList = new FTPFile[vector.size() - 2];  // 去除掉「.」跟「..」
            }

            FTPFile fFile = null;
            int idx = 0;
            for (Object item : vector) {
                LsEntry entry = (LsEntry) item;
                String fileName = entry.getFilename();

                if (fileName.equals(".") || fileName.equals("..")) { // 跳過「.」跟「..」
                    continue;
                }

                long fileSize = entry.getAttrs().getSize();

                fFile = new FTPFile();
                fFile.setType(FTPFile.FILE_TYPE);
                fFile.setName(fileName);
                fFile.setSize(fileSize);

                fileList[idx] = fFile;
                idx++;
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }
        return fileList;
	}

	@Override
	public boolean uploadFiles(String fileName, InputStream inputStream) throws Exception {
		try {
			checkSFtpStatus();
			command.put(inputStream, fileName);
			return true;

		} catch (Exception e) {
			throw new Exception("[SFTP upload file failed] >> " + e.toString());

		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	@Override
	public List<String> downloadFiles(ConfigInfoVO ciVO) throws Exception {
		List<String> fileContentList = null;

		String sevePath = "/tmp/111";
		FileOutputStream fos = null;
		try {
			checkSFtpStatus();

			fos = new FileOutputStream(sevePath);

			//切換工作目錄
			command.cd(ciVO.getRemoteFileDirPath());
			command.get(ciVO.getFileFullName(), fos);

		} catch (FileNotFoundException e) {
		    log.error( "get : " + "下載失敗，儲存目錄不存在，儲存目錄：[" + sevePath + "]");

	    } catch (SftpException e) {
	        log.error( "get : " + "下載失敗，錯誤訊息：[" + e.getMessage() + "]");

		} finally {
		    try {
		        fos.close();

		    } catch (IOException e) {
		        log.error( "get : "+"close FileOutputStream fail，錯誤訊息：["+e.getMessage()+"]");
		    }
		}

		return fileContentList;
	}

	@Override
	public boolean disconnect() throws Exception {
		try {
		    if (command != null) {
	            command.exit();
	        }
	        if (session != null) {
	            session.disconnect();
	        }
	        command = null;

		} catch (Exception e) {
			throw new Exception("[FTP disconnect failed] >> " + e.toString());
		}
		return true;
	}

	@Override
	public String downloadFilesString(ConfigInfoVO ciVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteFile(String filePath) throws Exception {
		try {
		    command.rm(filePath);

		} catch (Exception e) {
		    log.error(e.toString(), e);
		}
		return true;
	}

	@Override
	public boolean moveFiles(ConfigInfoVO ciVO, String sourceDirPath, String targetDirPath) throws Exception {
		try {
			targetDirPath = targetDirPath.substring(0, targetDirPath.lastIndexOf(Env.FTP_DIR_SEPARATE_SYMBOL));
			String sourceFilePath = sourceDirPath.concat(Env.FTP_DIR_SEPARATE_SYMBOL).concat(ciVO.getFileFullName());
			String targetFilePath = targetDirPath.concat(Env.FTP_DIR_SEPARATE_SYMBOL).concat(ciVO.getFileFullName());

			if (Env.ENABLE_LOCAL_BACKUP_USE_TODAY_ROOT_DIR) {
				SimpleDateFormat sdf = new SimpleDateFormat(Env.DIR_PATH_OF_CURRENT_DATE_FORMAT);

				targetDirPath = Env.FTP_DIR_SEPARATE_SYMBOL.concat(sdf.format(new Date())).concat(Env.FTP_DIR_SEPARATE_SYMBOL).concat(targetDirPath);
				targetFilePath = Env.FTP_DIR_SEPARATE_SYMBOL.concat(sdf.format(new Date())).concat(Env.FTP_DIR_SEPARATE_SYMBOL).concat(targetFilePath);

				targetDirPath = targetDirPath.replace("//", "/");
				targetFilePath = targetFilePath.replace("//", "/");
			}

			command.cd(targetDirPath);
			command.rename(sourceFilePath, targetFilePath);

		} catch (Exception e) {
		    log.error( "rename : " + "重新命名或移動失敗，錯誤訊息：[" + e.getMessage() + "]");
			throw e;
		}
		return false;
	}

	@Override
	public boolean retrieveFile(String fileName, FileOutputStream fos) throws Exception {
		try {
		    command.get(fileName, fos);

		} catch (SftpException e) {
            log.error( "get : " + "下載失敗，錯誤訊息：[" + e.getMessage() + "]");

        } finally {
            try {
                fos.close();

            } catch (IOException e) {
                log.error( "get : "+"close FileOutputStream fail，錯誤訊息：["+e.getMessage()+"]");
            }
        }
		return true;
	}
}
