package com.mmall.util;


import ch.qos.logback.classic.selector.servlet.LoggerContextFilter;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by wangshufu on 2017/8/3.
 */
public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String FTP_IP = PropertiesUtil.getProperty("ftp.server.ip");
    private static String FTP_user = PropertiesUtil.getProperty("ftp.user");
    private static String FTP_password = PropertiesUtil.getProperty("ftp.pass");
    private static String REMOTE_PATH = "img";

    private String ip;
    private int port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    private FTPUtil(String ip, int port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * 批量上传文件
     * @param fileList
     * @return
     * @throws IOException
     */
    public static boolean uploadFiles(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(FTP_IP, 21, FTP_user, FTP_password);
        logger.info("开始上传文件");
        boolean result = ftpUtil.uploadFiles(REMOTE_PATH, fileList);
        logger.info("上传文件结束,上传结果为{}",result);
        return result;
    }

    private boolean uploadFiles(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream inputStream = null;
        if (connectServer(this.ip, this.port, this.user, this.password)) {
            try {
                //需不需要切换文件夹,比如本来我们是直接在ftp下的,现在我们想上传到在ftp的img下,就调用这个;当我们传nul时,他就默认在ftp根目录下了
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                //将文件设置为字节,防止乱码
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //因在FTP服务器上配置的是被动模式,并且也对外开放了一个服务的被动端口范围
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    inputStream = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),inputStream);
                }
            } catch (IOException e) {
                logger.error("上传文件失败",e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                inputStream.close();
                //注意要断开连接,如果不断开连接,一直让其连接的话,会有问题的
                ftpClient.disconnect();
            }
        } else {
            uploaded = false;
        }
        return uploaded;
    }

    /**
     * 连接并登录FTP服务器
     *
     * @param ip
     * @param port
     * @param user
     * @param password
     * @return
     */
    private boolean connectServer(String ip, int port, String user, String password) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            //连接FTP
            ftpClient.connect(ip);
            //登录FTP
            isSuccess = ftpClient.login(user, password);
        } catch (IOException e) {
            logger.error("ftp连接或登录失败", e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
