package com.ftp;

import com.ftp.ui.FTPInterface;
import com.ftp.util.FtpUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;
import java.io.*;
import static com.ftp.ui.UIStatic.getScreen;
import static com.ftp.util.FtpUtil.getFTPClient;

/**
 * @author 86187
 */
public class Main {
    public static FTPInterface ftp;
    public static void main(String[] a) throws FileNotFoundException {
        frame();
        //getFile();
        //IP
        /*String ftpHost = "192.168.1.5";
        //端口号，没改过
        int ftpPort = 21;
        //用户名
        String ftpUserName = "ftp";
        //ftp����������
        String ftpPassword = "ftp";
        //ftp������·��
        String ftpPath = "1/";
        //����·��
        String localPath = "D:\\FTP\\LocalFTP\\";
        //�ļ���
        String fileName = "my.txt";
        *//*FtpUtil.deleteFile(ftpHost, ftpPort, ftpUserName, ftpPassword,ftpPath, fileName);*//*
        InputStream inputStream=new FileInputStream(new File(localPath+"my.txt"));
        boolean r=FtpUtil.uploadFile(ftpHost, ftpPort, ftpUserName, ftpPassword, ftpPath, "upload/", "my.txt", inputStream);
        System.out.println(r);*/
    }

    private static void frame() {
        init();
        ftp=new FTPInterface();
    }

    public static void getFile(){
        FTPClient f=getFTPClient("192.168.1.5",21,"ftp","ftp");
        getAllFile(f);
    }

    public static FTPFile[] getAllFile(FTPClient f){
        FTPFile[] files = null;
        try {
            files = f.listFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            for (FTPFile file : files) {

                if (file.isDirectory()) {
                    FTPFile[] f1 = f.listFiles(file.getName());
                    for(FTPFile f2 :f1){
                        if(f2.isDirectory()){
                            FTPFile[] f3=f.listFiles(file.getName()+"\\"+f2.getName());
                            for(FTPFile f4: f3){
                                System.out.println("永远永远"+file.getGroup());
                                System.out.println(f4.getName());
                            }
                        }
                        else{
                            System.out.println(f2.getName());
                        }

                    }
                }else{
                    System.out.println(file.getName());
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            System.out.println("?????");
        }
        return files;

    }






    private static void init(){
        getScreen();
    }
    public static void showWarning(String warningInfo){
        JOptionPane.showMessageDialog(ftp,warningInfo,"警告",1);
    }
}
