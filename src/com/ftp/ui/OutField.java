package com.ftp.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author 86187
 */
public class OutField extends JPanel {
    private JTextArea out=null;
    public OutField(){
        this.out=new JTextArea();
        this.out.setBounds(0,0,UIStatic.width,UIStatic.height/7);
        this.out.setEditable(false);
        this.out.setAlignmentX(0);
        this.out.setBorder(BorderFactory.createLineBorder(Color.gray,5));
        this.out.setText("sdasdsadasdsadasd");
        this.add(out);
    }
}
