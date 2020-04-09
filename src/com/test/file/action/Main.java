package com.test.file.action;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.ftp.util.FtpUtil.*;

/**
 * @author 86187
 */
public class Main {
    public static void main(String[] arg){
        String host="192.168.1.5";
        String user="ftp";
        String pass="ftp";
        int port=21;
        String baseLocalPath="D:\\FTP\\LocalFTP\\down";
        String ftpPath1="\\1\\upload";
        /**关于下载的参数解释
         * 前四个参数自然没问题，
         * 第五个是服务器的文件目录，比如文件是my\1.txt，那么此参数就是my，不能有最后一个\
         * 第六个参数是文件下载的本地文件夹目录，
         * 第七个参数是文件的名字，比如上面的例子，该参数是1.txt
         * */
        //downloadFtpFile(host,user,pass,port,ftpPath1,baseLocalPath,"4.txt");
        /**关于删除的参数解释
         *前四个参数也是无需解释
         *第五个参数是文件所在目录，同下载的那个解释
         * 第六个目录是文件名字
         * */
        //deleteFile(host,port,user,pass,ftpPath1,"20.txt");
        /**关于上传的参数解释
         *前四个参数不用解释
         * 第五个参数是在服务器端的基础目录，
         * 第六个也是服务器端的目录，但是要跟第五个参数结合起来才能作为上传到服务器的完整文件路径
         * 第七个参数是文件名，是上传到服务器之后的文件名，无所谓本地名字
         * 第八个就是一个输入流，这个输入流是要承载读取本地文件的作用的，所以在
         * */
        InputStream inputStream=null;
        try {
            inputStream=new FileInputStream(new File(baseLocalPath+"\\"+"-1.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean ret=uploadFile(host,port,user,pass,ftpPath1,"","100.txt",inputStream);
        System.out.println(ret);
    }
}
