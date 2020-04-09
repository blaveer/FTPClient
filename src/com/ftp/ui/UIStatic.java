package com.ftp.ui;

import java.awt.*;

/**
 * @author 86187
 */
public class UIStatic {
    /**存储当前的宽度*/
    public static int width=0;
    /**获取当前的高度*/
    public static int height=0;
    public static void getScreen(){
        /**得到屏幕的尺寸*/
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        UIStatic.width=screenSize.width;
        UIStatic.height=screenSize.height;
    }

}
