package com.ftp.ui;

import javax.imageio.IIOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

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
    private JMenu editMenu=null;
    private JMenuItem settingItem=null;

    /**查看菜单栏*/
    private JMenu viewItem=null;
    /**传输菜单栏*/
    private JMenu transmissionMenu=null;
    /**服务器菜单栏*/
    private JMenu serverMenu=null;
    /**帮助菜单栏*/
    private JMenu helpMenu=null;
    private JMenuItem readme=null;

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

        editMenu=new JMenu("设置(E)");
        editMenu.setMnemonic(KeyEvent.VK_E);

        settingItem=new JMenuItem("更改默认下载路径");
        settingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file==null){
                    return;
                }
                String localPath=file.getAbsolutePath();
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
                            p.setProperty(propertiesName,localPath);
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

        editMenu.add(settingItem);
        this.add(editMenu);

        helpMenu=new JMenu("帮助(H)");

        readme=new JMenuItem("关于");

        readme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //TODO 帮助的一个面板，不做了，写一个readme吧
            }
        });

        this.helpMenu.add(readme);
        this.add(helpMenu);

    }
}
