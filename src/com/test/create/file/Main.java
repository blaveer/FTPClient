package com.test.create.file;

import java.io.File;
import java.io.IOException;

/**
 * @author 86187
 */
public class Main {
    public static void main(String[] arg){
        String path="D:\\FTP\\FTP\\upload\\";
        for (int count = 200; count < 210; count++) {
            File file=new File(path+count+".txt");
            if(file.exists()){
                System.out.println(count+"文件已存在");
            }else{
                try {
                    file.createNewFile();

                } catch (IOException e) {
                    System.out.println(count+"创建文件报错");
                }
                System.out.println(count+"创建文件成功");
            }
        }
    }
}
