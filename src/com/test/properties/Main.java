package com.test.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * @author 86187
 */
public class Main {
    public static void main(String[] s) throws Exception{
        String pa="src/com/ftp/properties/setting.properties";
        File file1=new File(pa);
        String PROPERTIES_FILE_NAME="D:\\IIDEA\\project\\FTP\\My\\01\\src\\com\\test\\properties\\test.properties";
        System.out.println(System.getProperty("user.dir"));
        Properties p=new Properties();
        File file=new File(PROPERTIES_FILE_NAME);
        p.load(new FileInputStream(file));
        Set<String> pSet = p.stringPropertyNames();
        Iterator i = pSet.iterator();
        while (i.hasNext()) {
            String propertiesName = i.next().toString();
            /**查看*/
            if("my".equalsIgnoreCase(propertiesName)){
                System.out.println(p.getProperty(propertiesName));
                continue;
            }
            if ("you".equalsIgnoreCase(propertiesName)){
                System.out.println("修改前："+p.getProperty(propertiesName));
                p.setProperty(propertiesName,"12345");
                System.out.println("修改后："+p.getProperty(propertiesName));
                continue;
            }
            if("why".equalsIgnoreCase(propertiesName)){
                p.remove(propertiesName);
            }
        }
        p.store(new FileOutputStream(file), p.toString());
    }
    public void saveProperties() {
        try {
            Properties properties = new Properties();

            Properties p = new Properties();
            File file = new File("F://test.properties");
            p.load(new FileInputStream(file));
            Set<String> pSet = p.stringPropertyNames();
            Iterator i = pSet.iterator();
            while (i.hasNext()) {
                String propertiesName = i.next().toString();
                //删除一个当获取的名称hk相同时，就返回到下一步;break;是退出循环
                if ("hk".equalsIgnoreCase(propertiesName)) {
                    continue;
                }
                properties.setProperty(propertiesName, p.getProperty(propertiesName));
                //修改
                if ("japan".equalsIgnoreCase(propertiesName)) {
                    properties.setProperty(propertiesName, "123456789");
                }
            }
            properties.setProperty("usa", "美国");
            properties.setProperty("hk", "香港");
            properties.setProperty("japan", "日本");
            properties.setProperty("china", "中国");
            //添加
            properties.store(new FileOutputStream(file), properties.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
