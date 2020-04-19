package com.ftp;

import com.ftp.ui.FTPInterface;
import com.ftp.util.FtpUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;
import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import static com.ftp.ui.UIStatic.getScreen;
import static com.ftp.util.FtpUtil.getFTPClient;
import static com.ftp.web.file.WebStatic.baseDownloadPath;
import static com.ftp.web.file.WebStatic.ftpBasicPath;

/**
 * @author 86187
 */
public class Main {
    public static FTPInterface ftp;
    public static void main(String[] a) {
        frame();
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
        setting();
        getScreen();
    }
    private static void setting(){
        String path=null;
        String ftpPath=null;
        String filePath="src/com/ftp/properties/setting.properties";
        try{
            Properties p=new Properties();
            File fileIn=new File(filePath);
            p.load(new FileInputStream(fileIn));
            Set<String> pSet = p.stringPropertyNames();
            Iterator i = pSet.iterator();
            while (i.hasNext()) {
                String propertiesName = i.next().toString();
                if ("baseDownloadPath".equalsIgnoreCase(propertiesName)){
                    path=p.getProperty(propertiesName);
                    continue;
                }
                if("ftpBasicPath".equalsIgnoreCase(propertiesName)){
                    ftpPath=p.getProperty(propertiesName);
                    continue;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            System.out.println("主函数设置未知错误");
        }
        if(path!=null){
            baseDownloadPath=path;
        }
        if(ftpPath!=null){
            ftpBasicPath=ftpPath;
        }
    }
    public static void showWarning(String warningInfo){
        JOptionPane.showMessageDialog(ftp,warningInfo,"警告",1);
    }
}
