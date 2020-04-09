package com.ftp.web.file;

import com.ftp.event.UserPanelEvent;
import com.ftp.util.FtpUtil;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        JButton download=new JButton("下载");
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WebFileNode clickNode=(WebFileNode) treePath.getLastPathComponent();
                String pathAll=clickNode.getFilePath();
                String name=clickNode.getName();
                String path=pathAll.substring(0,pathAll.length()-name.length()-1);
                System.out.println("路径："+path+"\n"+"文件名："+name);
                //FtpUtil.downloadFtpFile(WebStatic.host,WebStatic.username,WebStatic.password,WebStatic.port,path,"D:\\FTP\\LocalFTP\\0409\\",clickNode.getName());
                FtpUtil.downloadFtpFile(WebStatic.ftpClient,path,baseDownloadPath,name);
            }
        });
        popup.add(download);
        /**删除按钮*/
        JButton delete=new JButton("删除");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WebFileNode clickNode=(WebFileNode)treePath.getLastPathComponent();
                String allPath=clickNode.getFilePath();
                String name=clickNode.getName();
                String path=allPath.substring(0,allPath.length()-name.length()-1);
                System.out.println("要删除的文件目录位置和名字是："+path+"     "+name);
                boolean ret=FtpUtil.deleteFile(WebStatic.ftpClient,path,name);
                if(ret){
                    /**更新目录
                     * 两个思路，一个是从服务器重新获取，一个是直接删除节点*/
                    /*WebTree.MyRefresh();*/
                    UserPanelEvent.connectClick(WebStatic.host,WebStatic.username,WebStatic.password,WebStatic.port);
                }else {
                    //TODO 删除失败的处理，待做
                }
            }
        });
        popup.add(delete);
        popup.show(webTree,x,y);
    }
}
