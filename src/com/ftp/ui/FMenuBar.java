package com.ftp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author 86187
 * @version 0.01
 *
 */
public class FMenuBar extends JMenuBar {
    /**文件菜单栏*/
    private JMenu fileMenu=null;
    private JMenuItem exitFileItem=null;

    /**编辑菜单栏*/
    private JMenu editItem=null;
    /**查看菜单栏*/
    private JMenu viewItem=null;
    /**传输菜单栏*/
    private JMenu transmissionItem=null;
    /**服务器菜单栏*/
    private JMenu serverItem=null;
    /**帮助菜单栏*/
    private JMenu helpItem=null;

    public FMenuBar(){
        fileMenu=new JMenu("文件(F)");
        fileMenu.setMnemonic(KeyEvent.VK_F);


        exitFileItem=new JMenuItem("退出(exit)");
        exitFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                ActionEvent.CTRL_MASK));
        exitFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitFileItem);


        this.add(fileMenu);
    }
}
