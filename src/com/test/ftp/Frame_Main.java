package com.test.ftp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.Toolkit;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JScrollBar;

import org.apache.commons.net.ftp.FTPFile;

import java.awt.ScrollPane;
import java.awt.Label;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.awt.Scrollbar;

public class Frame_Main implements ActionListener{


    //初始化参数--------------------------------
    static FTPFile[] file;
    static String FTP="192.168.1.5";
    static String username="ftp";
    static String password="ftp";
    //初始化参数--------------------------------


    private JFrame frame;
    private JTable table;
    static Ftp_by_apache ftp;
    public static Ftp_by_apache getFtp() {
        return ftp;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        ftp=new Ftp_by_apache(FTP,username,password);
        file=ftp.getAllFile();



        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Frame_Main window = new Frame_Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Create the application.
     */
    public Frame_Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Frame_Main.class.getResource("/image/UpFolder.gif")));
        frame.setTitle("FTP");
        frame.setBounds(100, 100, 470, 534);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //上传按钮--------------------------------------------------
        JButton upload = new JButton("\u4E0A\u4F20");
        upload.setFont(new Font("宋体", Font.PLAIN, 12));
        upload.setBackground(UIManager.getColor("Button.highlight"));
        upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //上传点击按钮触发------------------------------------
                System.out.println("上传！！！！！");
                int result = 0;
                File file = null;
                String path = null;
                JFileChooser fileChooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();
                System.out.println(fsv.getHomeDirectory());                //得到桌面路径  
                fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                fileChooser.setDialogTitle("请选择要上传的文件...");
                fileChooser.setApproveButtonText("确定");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                result = fileChooser.showOpenDialog(null);
                if (JFileChooser.APPROVE_OPTION == result) {
                    path=fileChooser.getSelectedFile().getPath();
                    System.out.println("path: "+path);
                    try {
                        //下载
                        ftp.upload(path);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    finally{

                        ftp.close_connection();
                    }
                }
                //上传点击按钮触发------------------------------------
            }
        });
        upload.setBounds(195, 15, 82, 23);
        frame.getContentPane().add(upload);
        //上传按钮--------------------------------------------------



        //刷新按钮--------------------------------------------------
        JButton refresh = new JButton("\u5237\u65B0");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        refresh.setFont(new Font("宋体", Font.PLAIN, 12));
        refresh.setBackground(UIManager.getColor("Button.highlight"));
        refresh.setBounds(312, 15, 82, 23);
        frame.getContentPane().add(refresh);
        //刷新按钮--------------------------------------------------



        //显示基本信息(FTP username)-----------------------------------------------
        JLabel lblNewLabel = new JLabel("FTP\u5730\u5740");
        lblNewLabel.setBounds(32, 10, 54, 15);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("\u7528\u6237\u540D");
        lblNewLabel_1.setBounds(32, 35, 54, 15);
        frame.getContentPane().add(lblNewLabel_1);

        JLabel address = new JLabel(FTP);
        address.setBounds(110, 10, 75, 15);
        frame.getContentPane().add(address);

        JLabel name = new JLabel(username);
        name.setBounds(110, 35, 82, 15);
        frame.getContentPane().add(name);
        //显示基本信息-----------------------------------------------


        //table数据初始化  从FTP读取所有文件
        String[][] data1=new String[file.length][4];
        for(int row=0;row<file.length;row++)
        {

            data1[row][0]=file[row].getName();
            if(file[row].isDirectory())
            {
                data1[row][1]="文件夹";
            }
            else if(file[row].isFile()){
                String[] geshi=file[row].getName().split("\\.");
                data1[row][1]=geshi[1];
            }
            data1[row][2]=file[row].getSize()+"";
            data1[row][3]="下载";
        }



        //table列名-----------------------------------------------------
        String[] columnNames = {"文件", "文件类型", "文件大小(字节)", ""  };
        DefaultTableModel model = new DefaultTableModel();
        model.setDataVector(data1, columnNames);

        //加滚动条--------------------------------------------------------
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(32, 73, 362, 384);
        frame.getContentPane().add(scrollPane);
        //加滚动条-----------------------------------------------------

        //table功能------------------------------------------------------
        table = new JTable(model);
        scrollPane.setViewportView(table);
        table.setColumnSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setToolTipText("\u53EF\u4EE5\u70B9\u51FB\u4E0B\u8F7D");

        //table button初始化(最后一列的按键)--------------------
        ButtonColumn buttonsColumn = new ButtonColumn(table, 3);

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }
}