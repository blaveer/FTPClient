package com.ftp.ui;

import com.ftp.file.tree.FileTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.ftp.Main.showWarning;
import static com.ftp.event.UserPanelEvent.connectClick;

/**
 * @author 86187
 *
 */
public class UserPanel extends JPanel {
    private int height;
    private Rectangle size=null;
    private JLabel host=null;


    private JTextField hostIn=null;
    private JLabel userName=null;
    private JTextField userNameIN=null;
    private JLabel password=null;
    private JTextField passwordIn=null;
    private JLabel port=null;
    private JTextField portIn=null;
    private JButton connect=null;

    public Rectangle getMySize() {
        return size;
    }

    public UserPanel(){
        int relativeWidth=3;
        this.height=UIStatic.height/30;
        /**主机*/
        this.host=new JLabel();
        this.host.setText("主机(H):");
        this.host.setBounds(relativeWidth,0,50,height);
        this.add(host);
        relativeWidth = relativeWidth + this.host.getWidth();
        this.hostIn=new JTextField();
        this.hostIn.setBounds(relativeWidth,0,100,height);
        this.add(hostIn);
        this.hostIn.setText("192.168.1.5");
        relativeWidth = relativeWidth + this.hostIn.getWidth()+25;
        /**用户名*/
        this.userName=new JLabel();
        this.userName.setText("用户名(U):");
        this.userName.setBounds(relativeWidth,0,65,height);
        this.add(userName);
        relativeWidth = relativeWidth + this.userName.getWidth();
        this.userNameIN=new JTextField();
        this.userNameIN.setBounds(relativeWidth,0,100,height);
        this.add(userNameIN);
        this.userNameIN.setText("ftp");
        relativeWidth = relativeWidth + (this.userNameIN.getWidth() + 15);
        /**密码*/
        this.password=new JLabel();
        this.password.setText("密码(W):");
        this.password.setBounds(relativeWidth,0,50,height);
        this.add(password);
        relativeWidth = relativeWidth + this.password.getWidth();
        this.passwordIn=new JTextField();
        this.passwordIn.setBounds(relativeWidth,0,100,height);
        this.add(passwordIn);
        this.passwordIn.setText("ftp");
        relativeWidth = relativeWidth + (this.passwordIn.getWidth() + 15);
        /**端口*/
        this.port=new JLabel();
        this.port.setText("端口(P):");
        this.port.setBounds(relativeWidth,0,50,height);
        this.add(port);
        relativeWidth = relativeWidth + this.port.getWidth();
        this.portIn=new JTextField();
        this.portIn.setBounds(relativeWidth,0,50,height);
        this.add(portIn);
        this.portIn.setText("21");
        relativeWidth = relativeWidth + (this.portIn.getWidth() + 15);
        /**连接按钮*/
        this.connect=new JButton("快速链接");
        this.connect.setBounds(relativeWidth,0,100,height);
        this.connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String host=hostIn.getText().trim();
                String username=userNameIN.getText().trim();
                String password=passwordIn.getText().trim();
                String port=portIn.getText().trim();
                int portInt=21;
                if(host==null||host.equals("")){
                    showWarning("无法解析服务器地址\n请输入服务器地址");
                    return;
                }
                if(port==null||port.equals("")){
                    portInt=21;
                }else{
                    try{
                        portInt=Integer.parseInt(port);
                    }catch (Exception ex){
                        //TODO 这里应该加一个弹窗警告，并且默认将不合法端口的设置为21
                        portInt=21;
                    }
                }

                connectClick(host,username,password,portInt);
            }
        });
        this.add(connect);
        this.size=new Rectangle(UIStatic.width,UIStatic.height/15);
        /*this.setSize(UIStatic.width,UIStatic.height/15);*/
        this.setLayout(null);
        this.setVisible(true);
        this.setBackground(getBackground());
    }
}
