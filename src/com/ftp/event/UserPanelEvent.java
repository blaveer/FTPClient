package com.ftp.event;

import com.ftp.ui.FTPInterface;
import com.ftp.util.FtpUtil;
import com.ftp.util.UtilStatic;
import com.ftp.web.file.WebStatic;
import com.ftp.web.file.WebTree;

/**
 * @author 86187
 */
public class UserPanelEvent {
    static int count=1;
    public static void connectClick(String host,String username,String password, int port){
        //TODO 连接服务器代码
        /**连接服务器*/
        WebStatic.ftpClient= FtpUtil.getFTPClient(host,port,username,password);

        if(WebStatic.ftpClient!=null){
            WebStatic.host=host;
            WebStatic.password=password;
            WebStatic.port=port;
            WebStatic.username=username;
            WebTree.MyRefresh();
            FTPInterface.outField.append(count+"连接成功\n");
            count++;
        }
        //FTPInterface.webTree.updateUI();
    }
}
