package com.ftp.web.file;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.ArrayList;

/**
 * @author 86187
 * DefaultMutableTreeNode
 */
public class WebFileNode extends DefaultMutableTreeNode {
    /**完整路径*/
    private String filePath;
    /**文件名字*/
    private String name;
    /**本身是否是文件夹*/
    private boolean isDirectory;
    /**是不是文件*/
    private boolean isFile;
    /**如果是文件夹，子文件及文件夹的数量*/
    private int childrenNum;
    /**子文件的列表*/
    private ArrayList<WebFileNode> children=new ArrayList<>();
    /**父文件夹*/
    private WebFileNode fileParent;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public int getChildrenNum() {
        return childrenNum;
    }

    public void setChildrenNum(int childrenNum) {
        this.childrenNum = childrenNum;
    }

    public ArrayList<WebFileNode> getChildren() {
        return children;
    }


    public WebFileNode getFileParent() {
        return fileParent;
    }

    public void setParent(WebFileNode parent) {
        this.parent = parent;
    }

    public WebFileNode(String name, boolean isDirectory){
        this.userObject=name;
        this.name=name;
        this.isDirectory=isDirectory;
    }

    public WebFileNode(String name,boolean isDirectory,String path){
        this.userObject=name;
        this.name=name;
        this.isDirectory=isDirectory;
        this.filePath=path;
    }
    public WebFileNode(String name){
        super(name);
        this.userObject=name;
        this.filePath="\\";
        this.isDirectory=true;
        this.isFile=false;
        this.fileParent=null;
        /**这个不知如何设置，待定*///TODO
        this.name="";
    }

    public void addChild(WebFileNode newNode){
        this.children.add(newNode);
        newNode.fileParent=this;
        this.childrenNum++;
        if(newNode.filePath!=null){
            if(!newNode.isDirectory){
                newNode.setFilePath(newNode.filePath+newNode.name);
            }
        }else{
            newNode.setFilePath(newNode.name);
        }
        this.add(newNode);
    }

    public static void removeChild(){

    }

    @Override
    public void add(MutableTreeNode newChild) {
        super.add(newChild);
    }
}
