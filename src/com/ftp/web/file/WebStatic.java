package com.ftp.web.file;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;

/**
 * @author 86187
 */
public class WebStatic {
    public static String host;
    public static String username;
    public static String password;
    public static int port=21;
    public static FTPClient ftpClient=null;
    /**存放下载的文件的文件夹，这个算是为了简单测试设置的，等后续添加其他更人性化的方法，就是用这个参数传递信息*/
    public static String baseDownloadPath="D:\\FTP\\LocalFTP\\0409";
    public static String ftpBasicPath="\\newForUpload";
    public static String ftpUploadFileName="";
    public static DefaultMutableTreeNode getAllFile(){
        if(ftpClient==null){
            return WebTree.root;
        }
        FTPFile[] files=null;
        try {
            files=ftpClient.listFiles();

        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        for(FTPFile child : files){
            if(child.isDirectory()){
                //todo 需要一个函数来生成
                WebFileNode dir=createFolderNode(child,child.getName());
                WebTree.root.addChild(dir);
            }
            else{
                WebTree.root.addChild(createNode(child));
            }
        }

        //WebTree.root.add(new DefaultMutableTreeNode("好"));
        return WebTree.root;
    }

    public static WebFileNode createFolderNode(FTPFile root,String path){
        WebFileNode ret=new WebFileNode(root.getName(),root.isDirectory(),path);
        try {
            FTPFile[] files=ftpClient.listFiles(path);
            for(FTPFile child : files){
                if(child.isDirectory()){
                    //todo 需要一个函数来生成
                    WebFileNode dir=createFolderNode(child,path+"\\"+child.getName());
                    ret.addChild(dir);
                }
                else{
                    ret.addChild(createNode(child,path+"\\"));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static WebFileNode createNode(FTPFile self,String path){
        return new WebFileNode(self.getName(),self.isDirectory(),path);
    }
    public static WebFileNode createNode(FTPFile self){
        return new WebFileNode(self.getName(),self.isDirectory());
    }
}
