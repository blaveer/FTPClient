package com.ftp.web.file;

import com.ftp.event.UserPanelEvent;
import com.ftp.ui.FTPInterface;
import com.ftp.util.FtpUtil;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import static com.ftp.Main.showWarning;
import static com.ftp.web.file.WebStatic.baseDownloadPath;
import static com.ftp.web.file.WebStatic.host;

/**
 * @author 86187
 */
public class WebTreeListener extends MouseAdapter {
    private WebTree webTree;
    public WebTreeListener(WebTree webTree){
        if(webTree==null){
            throw new IllegalArgumentException("Null argument");
        }
        this.webTree=webTree;
    }

    @Override
    public void mousePressed(MouseEvent e){
        if(e.getButton()==MouseEvent.BUTTON3){
            rightClick(e.getX(),e.getY());
        }
    }

    private void rightClick(int x,int y){
        TreePath treePath=webTree.getPathForLocation(x,y);
        if(webTree==null){
            return;
        }
        JPopupMenu popup=new JPopupMenu();
        /**下载按钮*/
        JButton download=new JButton("          下      载          ");
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WebFileNode clickNode=(WebFileNode) treePath.getLastPathComponent();
                if(clickNode.isDirectory()){
                    showWarning("不可直接下载文件夹");
                    System.out.println("不可直接下载文件夹");
                    return;
                }
                String pathAll=clickNode.getFilePath();
                String name=clickNode.getName();
                String path=null;
                try{
                    path=pathAll.substring(0,pathAll.length()-name.length()-1);
                }catch (Exception e){
                    path=pathAll;
                }
                System.out.println("路径："+path+"\n"+"文件名："+name);
                //FtpUtil.downloadFtpFile(WebStatic.host,WebStatic.username,WebStatic.password,WebStatic.port,path,"D:\\FTP\\LocalFTP\\0409\\",clickNode.getName());
                FtpUtil.downloadFtpFile(WebStatic.ftpClient,path,baseDownloadPath,name);

                String out=FTPInterface.outField.getText();
                FTPInterface.outField.setText(out+"下载服务器文件"+pathAll+"成功"+"\n");

            }
        });
        popup.add(download);
        /**另存为按钮*/
        JButton saveAs=new JButton("        另   存   为        ");
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WebFileNode clickNode=(WebFileNode) treePath.getLastPathComponent();
                if(clickNode.isDirectory()){
                    showWarning("不可直接下载文件夹");
                    System.out.println("不可直接下载文件夹");
                    return;
                }
                /**另存为事件*/
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file==null){
                    return;
                }
                String localPath=file.getAbsolutePath();


                String pathAll=clickNode.getFilePath();
                System.out.println(pathAll);
                String name=clickNode.getName();
                String path=null;
                try{
                    path=pathAll.substring(0,pathAll.length()-name.length()-1);
                }catch (Exception e){
                    path=pathAll;
                }

                System.out.println("路径："+path+"\n"+"文件名："+name);
                //FtpUtil.downloadFtpFile(WebStatic.host,WebStatic.username,WebStatic.password,WebStatic.port,path,"D:\\FTP\\LocalFTP\\0409\\",clickNode.getName());
                FtpUtil.downloadFtpFile(WebStatic.ftpClient,path,localPath,name);


                String out=FTPInterface.outField.getText();
                FTPInterface.outField.setText(out+"下载服务器文件"+pathAll+"成功"+"\n");
            }
        });
        popup.add(saveAs);
        /**删除按钮*/
        JButton delete=new JButton("          删      除          ");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WebFileNode clickNode=(WebFileNode)treePath.getLastPathComponent();
                if(clickNode.isDirectory()){
                    showWarning("不可直接删除文件夹");
                    System.out.println("不可直接删除文件夹");
                    return;
                }
                String allPath=clickNode.getFilePath();
                String name=clickNode.getName();
                String path=null;
                try{
                    path=allPath.substring(0,allPath.length()-name.length()-1);
                }catch (Exception e){
                    path=allPath;
                }
                System.out.println("要删除的文件目录位置和名字是："+path+"     "+name);
                boolean ret=FtpUtil.deleteFile(WebStatic.ftpClient,path,name);
                if(ret){
                    /**更新目录
                     * 两个思路，一个是从服务器重新获取，一个是直接删除节点*/
                    /*WebTree.MyRefresh();*/
                    UserPanelEvent.connectClick(WebStatic.host,WebStatic.username,WebStatic.password,WebStatic.port);
                    String out=FTPInterface.outField.getText();
                    FTPInterface.outField.setText(out+"删除服务器文件"+allPath+"成功"+"\n");
                }else {
                    String out=FTPInterface.outField.getText();
                    FTPInterface.outField.setText(out+"下载服务器文件"+allPath+"失败"+"\n");
                }
            }
        });
        popup.add(delete);
        JButton setting=new JButton("置为默认上传根路径");
        setting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WebFileNode clickNode=(WebFileNode) treePath.getLastPathComponent();
                if(!clickNode.isDirectory()){
                    showWarning("请选择文件夹默认文件夹");
                    System.out.println("不可直接下载文件夹");
                    return;
                }
                String path=clickNode.getFilePath();
                //System.out.println(path);
                path="\\"+path;
                WebStatic.ftpBasicPath=path;
                String filePath="src/com/ftp/properties/setting.properties";
                try{
                    Properties p=new Properties();
                    File fileIn=new File(filePath);
                    p.load(new FileInputStream(fileIn));
                    Set<String> pSet = p.stringPropertyNames();
                    Iterator i = pSet.iterator();
                    while (i.hasNext()) {
                        String propertiesName = i.next().toString();
                        if ("ftpBasicPath".equalsIgnoreCase(propertiesName)){
                            p.setProperty(propertiesName,path);
                            break;
                        }
                    }
                    p.store(new FileOutputStream(filePath), p.toString());
                }catch (IOException e){
                    e.printStackTrace();
                }catch (Exception e){
                    System.out.println("更改默认路径未知错误");
                }
            }
        });
        popup.add(setting);
        popup.show(webTree,x,y);
    }
}
