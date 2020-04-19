package com.ftp.ui;

import com.ftp.util.JFileTree;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.SpringLayout.NORTH;

/**
 * @author 86187
 * @function 显示本地和服务器的文件结构
 * 这个类也没应用到
 */
public class FileTreeM extends JPanel{
    //TODO 鉴于本地和服务器的pop菜单不同，可能还要再继承一下
    /*private JPopupMenu popupMenu=new JPopupMenu();*/

    /**头结构*/
    private JPanel head=null;
    private JLabel headLabel=null;
    private JLabel filePath=null;
    private final static JFileTree FILETREE = new JFileTree(new JFileTree.ExtensionFilter("lnk"));

    public FileTreeM(int width, int height, String headText){
        this.setLayout(new BorderLayout());
        int headHeight=0;
        headHeight=UIStatic.height/40;
        /**头结构*/
        this.head=new JPanel(null);
        this.head.setBounds(0,0,width,headHeight);
        System.out.println(headHeight);
        this.headLabel=new JLabel(headText);
        this.headLabel.setBounds(0,0,width/7,headHeight);
        //TODO 关于这个背景等属性的介绍
        this.head.add(headLabel);

        this.filePath=new JLabel();

        this.add(head,NORTH);
        /**下面就是文件部分了*/
        this.add(new JScrollPane(FILETREE),CENTER);



    }

    public String getFilePath() {
        return filePath.getText();
    }

    public void setFilePath(String filePath) {
        this.filePath.setText(filePath);
    }
}
