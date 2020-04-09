package com.ftp.web.file;

import com.ftp.ui.FTPInterface;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import static com.ftp.web.file.WebStatic.getAllFile;

/**
 * @author 86187
 */
public class WebTree extends JTree {
    public static WebFileNode root=new WebFileNode("FTP服务器");

    private boolean showHiddenFiles;

    public static boolean MyRefresh(){
        if(WebStatic.ftpClient==null){
            return false;
        }
        root.removeAllChildren();
        getAllFile();
        FTPInterface.webTree.updateUI();
        return true;
    }
    public WebTree(DefaultMutableTreeNode root){
        super(root);
        initVariable();
        if(WebStatic.ftpClient!=null){
            initRoot();
        }
        initListeners();
    }

    private void initListeners() {
        addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
            }
            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            }
        });
        WebTreeListener wtl=new WebTreeListener(this);
        addMouseListener(wtl);
    }
    public void setShowHiddenFiles(boolean showHiddenFiles) {
        if (showHiddenFiles != this.showHiddenFiles)
        {
            this.showHiddenFiles = showHiddenFiles;
            //initRoot();
        }
    }
    private void initRoot(){
        if(WebStatic.ftpClient==null){
            return;
        }
        getAllFile();
        //initTree();
    }

    private void initVariable(){
        this.showHiddenFiles=false;
    }
    /*public void initTree(){
        for (int count = 0; count < root.getChildCount(); count++) {
            root
        }
    }*/
}
