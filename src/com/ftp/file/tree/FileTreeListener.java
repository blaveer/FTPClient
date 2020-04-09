package com.ftp.file.tree;


import com.ftp.event.UserPanelEvent;
import com.ftp.web.file.WebStatic;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.ftp.util.FtpUtil.uploadFile;
import static com.ftp.web.file.WebStatic.ftpUploadFileName;

/**
 * the listener for the <code>FileTree</code>
 * @author 李鑫超
 */
public class FileTreeListener extends MouseAdapter {

    /**
     * Creates a new instance of FileTreeListener
     * @param fileTree the <code>FileTree</code> to listen for
     */
    public FileTreeListener(FileTree fileTree) {
        if (fileTree == null) {
            throw new IllegalArgumentException("Null argument not allowed");
        }

        this.fileTree = fileTree;
    }

    /**
     * Listens for right-clicks on the tree.
     * @param e contains information about the mouse click event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightClick(e.getX(), e.getY());
        }
    }

    /**
     *
     * @param x the x coordinate of the mouse when it was pressed
     * @param y the y coordinate of the mouse when it was pressed
     */
    private void rightClick(int x, int y) {
        TreePath treePath = fileTree.getPathForLocation(x, y);
        if (treePath == null) {
            return;
        }

       /* if (!fileTree.isDeleteEnabled()) {
            return;
        }*/

        JPopupMenu popup = new JPopupMenu();

        /**popup.add(new DeleteFileAction(treePath));*/
        JButton upload=new JButton("上传");
        upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DefaultMutableTreeNode clickNode=(DefaultMutableTreeNode)treePath.getLastPathComponent();
                if(clickNode.getChildCount()!=0){
                    System.out.println("点击了文件夹");
                    return;
                }
                String localPath=getPath(treePath.toString());
                if(localPath.equals("")){
                    System.out.println("选择了不合法的文件");
                    return;
                }
                /**localPath这是文件在本地的完整路径
                 * 接下来就该上传了
                 * */
                InputStream inputStream=null;
                try{
                    inputStream=new FileInputStream(
                            new File(localPath)
                    );
                }catch (FileNotFoundException e){
                    System.out.println("上传的文件找不到");
                    return;
                }catch (Exception e){
                    System.out.println("上传文件时创建输入流发生储物");
                    return;
                }
                boolean ret=uploadFile(WebStatic.ftpClient,WebStatic.ftpBasicPath,"",ftpUploadFileName,inputStream);
                if(ret){
                    UserPanelEvent.connectClick(WebStatic.host,WebStatic.username,WebStatic.password,WebStatic.port);
                }else{
                    //TODO 上传失败的处理
                }
            }
        });
        popup.add(upload);
        popup.show(fileTree, x, y);
    }

    private String getPath(String node){
        String ret="";
        String[] split=node.split(",");
        for (int count = 0; count < split.length; count++) {
            split[count]=split[count].trim();
        }
        if(split.length<2){
            return ret;
        }else if(!split[1].equals("My Computer")){
            return ret;
        }

        split[split.length-1]=split[split.length-1].substring(0,split[split.length-1].length()-1);
        ret=split[2];
        /**这么写的前提是点击的是个文件，而不是一个文件夹*/
        for (int count = 3; count < split.length-1; count++) {
            ret=ret+split[count]+"\\";
        }
        ftpUploadFileName=split[split.length-1];
        ret=ret+split[split.length-1];

        return ret;
    }

    /**
     * the <code>FileTree</code> to listen on
     */
    private FileTree fileTree;

    /**
     * feature not implemented
     */
    private class RenameAction extends AbstractAction {

        public RenameAction(TreePath treePath) {
            this.treePath = treePath;

            putValue(Action.NAME, "Rename");

            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            fileTreeNode = (FileTreeNode)treeNode.getUserObject();
            if (!fileTreeNode.file.canWrite())
                setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fileTree.startEditingAtPath(treePath);
        }

        private TreePath treePath;
        private FileTreeNode fileTreeNode;
    }

    private class DeleteFileAction extends AbstractAction {
        /**
         * constructor for the action to delete a file or directory
         * @param treePath the treepath of the node to act on
         */
        public DeleteFileAction(TreePath treePath) {
            this.treePath = treePath;

            if (Constants.isOSX) {
                putValue(Action.NAME, "Move to Trash");
            } else
            {
                putValue(Action.NAME, "Delete");
                putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
            }

            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            fileTreeNode = (FileTreeNode)treeNode.getUserObject();
            if (!fileTreeNode.file.canWrite()) {
                setEnabled(false);
            }
        }

        /**
         * the action called when the user wants to delete a file or directory
         * @param e information about the event that caused this method to be called
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int choice = JOptionPane.showConfirmDialog(fileTree.getRootPane(),
                    "Are you sure you want to delete '" + fileTreeNode.file.getName()+"'?",
                    "Confirm delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == 1) {
                return; // they selected no
            }

            boolean success = false;
            if (fileTreeNode.file.isDirectory()) {
                success = deleteDirectory(fileTreeNode.file);
            } else {
                success = fileTreeNode.file.delete();
            }

            if (success)
            {
                fileTree.getFileTreeModel().removeNodeFromParent(
                        (DefaultMutableTreeNode)treePath.getLastPathComponent());
            }

        }

        /**
         * deletes a directory and its content
         * @param dir The directory to delete
         * @return true on success, false otherwise
         */
        private boolean deleteDirectory(File dir) {
            if (dir == null || !dir.exists() || !dir.isDirectory()) {
                return false;
            }

            boolean success = true;
            File [] list = dir.listFiles();
            for (File file:list)
            {
                if (file.isDirectory())
                {
                    if (!deleteDirectory(file)) {
                        success = false;
                    }
                }
                else
                {
                    if (!file.delete()) {
                        success = false;
                    }
                }
            }
            if (!dir.delete())  // finally delete the actual directory
            {
                success = false;
            }

            return success;
        }

        /**
         * The <code>TreePath</code> to the node that will be deleted.
         */
        private TreePath treePath;
        /**
         * The <code>FileTreeNode</code> stored inside the <code>DefaultMutableTreeNode</code>'s
         * user object
         */
        private FileTreeNode fileTreeNode;
    }
}