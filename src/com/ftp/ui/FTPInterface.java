package com.ftp.ui;

import com.ftp.file.tree.FileTree;
import com.ftp.web.file.WebTree;


import javax.swing.*;
import java.awt.*;

/**
 * @author 86187
 */
public class FTPInterface extends JFrame {
    private UserPanel userPanel=null;
    private FMenuBar menuBar=null;

    private JScrollPane outPanel=null;
    //public static JTextArea outField=null;
    public static JTextPane outField=null;


    private FileTreeM fileTreeM =null;

    private JLabel fileTreeHead=null;
    private FileTree fileTree = null;
    private JScrollPane panelFileTree=null;

    private JLabel fileTreeHeadWeb=null;
    public static WebTree webTree=null;
    private JScrollPane panelWebFile=null;

    public FTPInterface(){
        super("FTP");
        this.setLayout(null);
        this.init();
        this.setSize(UIStatic.width,UIStatic.height);
        this.setLocation(0,0);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    private void init() {
        int relativeHeight = 0;
        this.menuBar = new FMenuBar();
        this.setJMenuBar(menuBar);
        this.userPanel = new UserPanel();
        this.add(userPanel);
        this.userPanel.setBounds(0, 0, UIStatic.width, UIStatic.height / 30);
        relativeHeight = this.userPanel.getHeight();
        /**输出文本域*/
        //outField = new JTextArea(5, 127);
        outField=new JTextPane();
        this.outPanel = new JScrollPane(outField);
        this.outPanel.setBounds(0, relativeHeight, UIStatic.width, UIStatic.height / 7);
        outField.setBounds(0, 0, UIStatic.width, UIStatic.height / 7);
        outField.setSize(UIStatic.width, UIStatic.height / 7);
        outField.setEditable(false);
        outField.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        this.outPanel.add(outField);
        this.add(outPanel);
        //outField.setLineWrap(true);
        relativeHeight = relativeHeight + outPanel.getHeight();
        /**文件树*/
//        fileTreeM = new FileTreeM(UIStatic.width, UIStatic.height / 2, "本地站点");
//        this.add(fileTreeM);
//        fileTreeM.setBounds(0, relativeHeight, UIStatic.width, UIStatic.height / 2);
        fileTreeHead=new JLabel("本地目录");
        this.add(fileTreeHead);
        fileTreeHead.setBackground(Color.gray);
        fileTreeHead.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        fileTreeHead.setBounds(3,relativeHeight,UIStatic.width/2-1,800/30);

        fileTreeHeadWeb=new JLabel("远程站点");
        this.add(fileTreeHeadWeb);
        fileTreeHeadWeb.setBackground(Color.gray);
        fileTreeHeadWeb.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        fileTreeHeadWeb.setBounds(3+fileTreeHead.getWidth(),relativeHeight,UIStatic.width-fileTreeHead.getWidth()-3,800/30);


        relativeHeight+=this.fileTreeHead.getHeight();


        fileTree=new FileTree();
        fileTree.setShowHiddenFiles(false);
        fileTree.setDeleteEnabled(false);
        panelFileTree=new JScrollPane(fileTree);
        this.add(panelFileTree);
        this.panelFileTree.setBounds(3,relativeHeight,UIStatic.width/2,UIStatic.height-relativeHeight);

        webTree=new WebTree(WebTree.root);
        webTree.setShowHiddenFiles(false);
        panelWebFile=new JScrollPane(webTree);
        this.add(panelWebFile);
        this.panelWebFile.setBounds(3+fileTreeHead.getWidth(),relativeHeight,UIStatic.width-fileTreeHead.getWidth()-3,UIStatic.height-relativeHeight);



    }




}
