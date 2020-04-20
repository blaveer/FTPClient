package com.ftp.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import static com.ftp.Main.showWarning;

/**
 * ftp工具类
 * @author 86187
 */

public class FtpUtil {

    private final static Log logger = LogFactory.getLog(FtpUtil.class);
    /** 本地字符编码 */
    private static String LOCAL_CHARSET = "GBK";

    /**FTP协议里面，规定文件名编码为iso-8859-1*/
    private static String SERVER_CHARSET = "ISO-8859-1";






    /**
     * 获取FTPClient对象
     *
     * @param ftpHost
     *            FTP主机服务器
     *
     * @param ftpPassword
     *            FTP 登录密码
     *
     * @param ftpUserName
     *            FTP登录用户名
     *
     * @param ftpPort
     *            FTP端口 默认为21
     *
     * @return
     */
    public static FTPClient getFTPClient(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword) {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            // 连接FTP服务器
            ftpClient.connect(ftpHost, ftpPort);
            // 登陆FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.info("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                logger.info("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.info("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }

    /**
     * 从FTP服务器下载文件，测试函数，下面有一个重载的使用函数，参数改为FTPClient对象
     *
     * @param ftpHost FTP IP地址
     *
     * @param ftpUserName FTP 用户名
     *
     * @param ftpPassword FTP用户名密码
     *
     * @param ftpPort FTP端口
     *
     * @param ftpPath FTP服务器中文件所在路径 格式： ftptest/aa，只能是路径
     *
     * @param localPath 下载到本地的位置 格式：H:/download
     *
     * @param fileName 文件名称，这个是指ftp服务端的文件名字，下载到本地名字不变
     */
    public static void downloadFtpFile(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort,
                                       String ftpPath, String localPath, String fileName) {

        FTPClient ftpClient = null;

        try {
            ftpClient = getFTPClient(ftpHost, ftpPort, ftpUserName, ftpPassword);
            // 设置上传文件的类型为二进制类型
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftpClient.setControlEncoding(LOCAL_CHARSET);
            ftpClient.enterLocalPassiveMode();// 设置被动模式
            //设置传输的模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 上传文件
            //对中文文件名进行转码，否则中文名称的文件下载失败
            String fileNameTemp = new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET);

            ftpClient.changeWorkingDirectory(ftpPath);

            InputStream retrieveFileStream = ftpClient.retrieveFileStream(fileNameTemp);

            // 第一种方式下载文件(推荐)
			 /* File localFile = new File(localPath + File.separatorChar + fileName);
			  OutputStream os = new FileOutputStream(localFile);
			  ftpClient.retrieveFile(fileName, os); os.close();*/


            // 第二种方式下载：将输入流转成字节，再生成文件，这种方式方便将字节数组直接返回给前台jsp页面
            byte[] input2byte = input2byte(retrieveFileStream);
            byte2File(input2byte, localPath, fileName);

            if(null != retrieveFileStream){
                retrieveFileStream.close();
            }
        }
        catch (FileNotFoundException e) {
            logger.error("没有找到" + ftpPath + "文件");
            e.printStackTrace();
        }
        catch (SocketException e) {
            logger.error("连接FTP失败.");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("文件读取错误。");
            e.printStackTrace();
        }
        finally {

            if (ftpClient.isConnected()) {
                try {
                    //退出登录
                    ftpClient.logout();
                    //关闭连接
                    ftpClient.disconnect();
                } catch (IOException e) {
                }
            }
        }
    }
    public static void downloadFtpFile(FTPClient ftpClient,String ftpPath, String localPath, String fileName){
        try {
            // 设置上传文件的类型为二进制类型
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftpClient.setControlEncoding(LOCAL_CHARSET);
            ftpClient.enterLocalPassiveMode();// 设置被动模式
            //设置传输的模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 上传文件
            //对中文文件名进行转码，否则中文名称的文件下载失败
            String fileNameTemp = new String(fileName.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
            ftpClient.changeWorkingDirectory(ftpPath);

            InputStream retrieveFileStream = ftpClient.retrieveFileStream(fileNameTemp);

            byte[] input2byte = input2byte(retrieveFileStream);
            byte2File(input2byte, localPath, fileName);

            if(null != retrieveFileStream){
                retrieveFileStream.close();
            }
        } catch (FileNotFoundException e) {
            logger.error("没有找到" + ftpPath + "文件");
            e.printStackTrace();
        } catch (SocketException e) {
            logger.error("连接FTP失败.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件读取错误。");
            e.printStackTrace();
        } finally {
            /**这个函数大部分直接copy的上面的那个函数，finally 部分是析构FTPClient对象的*/
        }
    }


    /**
     * Description: 向FTP服务器上传文件
     *
     * @param ftpHost
     *            FTP服务器hostname
     * @param ftpPort
     *            FTP服务器端口
     * @param ftpUserName
     *            FTP登录账号
     * @param ftpPassword
     *            FTP登录密码
     * @param basePath
     *            FTP服务器基础目录
     * @param filePath
     *            FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename
     *            上传到FTP服务器上的文件名
     * @param input
     *            输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword,
                                     String basePath, String filePath, String filename, InputStream input) {
        boolean result = false;
        FTPClient ftpClient = null;
        try {
            int reply;
            ftpClient = getFTPClient(ftpHost, ftpPort, ftpUserName, ftpPassword);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return result;
            }
            /**切换到上传目录
             * bathPath是基础路径，连接后面的一块是完整路径
             * */
            if (!ftpClient.changeWorkingDirectory(basePath + filePath)) {
                // 如果目录不存在创建目录
                /**这里默认是有基础路径的*/
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {
                        if (!ftpClient.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftpClient.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }

            // 设置上传文件的类型为二进制类型
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftpClient.setControlEncoding(LOCAL_CHARSET);
            ftpClient.enterLocalPassiveMode();// 设置被动模式
            // 设置传输的模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 上传文件
            filename = new String(filename.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
            /**这里没有再写函数，直接调用的storeFile函数使用的*/
            if (!ftpClient.storeFile(filename, input)) {
                //System.out.println("输出"+result);
                return result;
            }
            if(null != input){
                input.close();
            }
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    //退出登录
                    ftpClient.logout();
                    //关闭连接
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public static boolean uploadFile(FTPClient ftpClient,String basePath, String filePath, String filename, InputStream input){
        boolean result = false;
        try {
            int reply;
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return result;
            }
            // 切换到上传目录
            if(!ftpClient.changeWorkingDirectory(basePath)){
                showWarning("请先选择默认的上传基础路径");
                return false;
            }
            if (!ftpClient.changeWorkingDirectory(basePath + filePath)) {
                // 如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {
                        if (!ftpClient.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftpClient.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            // 设置上传文件的类型为二进制类型
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftpClient.setControlEncoding(LOCAL_CHARSET);
            ftpClient.enterLocalPassiveMode();// 设置被动模式
            // 设置传输的模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 上传文件
            filename = new String(filename.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
            if (!ftpClient.storeFile(filename, input)) {
                System.out.println("输出"+result);
                return result;
            }

            if(null != input){
                input.close();
            }

            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!ftpClient.isConnected()) {
                System.out.println("未知原因删除文件后断开连接了");
            }
        }
        return result;
    }



    /**
     * 删除文件 未测试
     *
     * @param ftpHost
     *            FTP服务器地址
     * @param ftpPort
     *            FTP服务器端口号
     * @param ftpUserName
     *            FTP登录帐号
     * @param ftpPassword
     *            FTP登录密码
     * @param pathname
     *            FTP服务器保存目录
     * @param filename
     *            要删除的文件名称
     * @return
     */
    public static boolean deleteFile(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword,
                                     String pathname, String filename) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = getFTPClient(ftpHost, ftpPort, ftpUserName, ftpPassword);
            // 验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            }
            // 切换FTP目录
            /**下面代码都是copy上面的，没改*/
            ftpClient.changeWorkingDirectory(pathname);
            // 设置上传文件的类型为二进制类型
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                LOCAL_CHARSET = "UTF-8";
            }
            ftpClient.setControlEncoding(LOCAL_CHARSET);
            ftpClient.enterLocalPassiveMode();// 设置被动模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);// 设置传输的模式
            //对中文名称进行转码
            filename = new String(filename.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
            ftpClient.dele(filename);
            flag = true;
            System.out.println("成了");
        } catch (Exception e) {
            System.out.println("报错了");
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    //退出登录
                    ftpClient.logout();
                    //关闭连接
                    ftpClient.disconnect();
                } catch (IOException e) {
                }
            }
        }
        return flag;
    }
    public static boolean deleteFile(FTPClient ftpClient,String pathname, String filename){
        boolean flag = false;
        try {
            // 验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                return flag;
            }
            // 切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            // 设置上传文件的类型为二进制类型
            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftpClient.setControlEncoding(LOCAL_CHARSET);
            ftpClient.enterLocalPassiveMode();// 设置被动模式
            /** 设置传输的模式*/
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //对中文名称进行转码
            filename = new String(filename.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
            ftpClient.dele(filename);
            flag = true;
            System.out.println("成了");
        } catch (Exception e) {
            System.out.println("报错了");
            e.printStackTrace();
        }
        return flag;
    }

    // 将字节数组转换为输入流
    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    /**将输入流转为byte[]
     * 将错误抛出，由调用者统一处理
     * */
    public static final byte[] input2byte(InputStream inStream) throws IOException {
        /**测试过程中，发现大部分报错都是在这报的错，*/
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    /**
     * @function 将byte[]转为文件
     * @param buf 从服务器下载的之后的文件，已经转为二进制流的
     * @param filePath 文件存储的本地路径
     * @param fileName
     **/
    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            /**创建文件*/
            file = new File(filePath + File.separator + fileName);
            /**创建输出流*/
            fos = new FileOutputStream(file);
            /**创建二进制流*/
            bos = new BufferedOutputStream(fos);
            /**将二进制写入输出流中*/
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /**上面的两个if语句块中都没有报错的话，就完成了*/
        }
    }

    public static boolean uploadFile2(String ftpHost, String ftpUserName,
                                     String ftpPassword, int ftpPort, String ftpPath,
                                     String fileName,InputStream input) {
        boolean success = false;
        FTPClient ftpClient = null;
        try {
            int reply;
            ftpClient = getFTPClient(ftpHost, ftpPort,ftpUserName, ftpPassword);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return success;
            }
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);

            ftpClient.storeFile(fileName, input);

            input.close();
            ftpClient.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }
}