package com.test.JTree;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * the listener for the <code>FileTree</code>
 * @author Arash Payan (http://www.arashpayan.com)
 */
public class FileTreeListener extends MouseAdapter {

    /**
     * Creates a new instance of FileTreeListener
     * @param fileTree the <code>FileTree</code> to listen for
     */
    public FileTreeListener(FileTree fileTree) {
        if (fileTree == null)
            throw new IllegalArgumentException("Null argument not allowed");

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
        if (treePath == null)
            return;

        if (!fileTree.isDeleteEnabled())
            return;

        JPopupMenu popup = new JPopupMenu();

        popup.add(new DeleteFileAction(treePath));
        popup.show(fileTree, x, y);
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

            if (Constants.isOSX)
                putValue(Action.NAME, "Move to Trash");
            else
            {
                putValue(Action.NAME, "Delete");
                putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
            }

            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            fileTreeNode = (FileTreeNode)treeNode.getUserObject();
            if (!fileTreeNode.file.canWrite())
                setEnabled(false);
        }

        /**
         * the action called when the user wants to delete a file or directory
         * @param e information about the event that caused this method to be called
         */
        public void actionPerformed(ActionEvent e) {
            int choice = JOptionPane.showConfirmDialog(fileTree.getRootPane(),
                    "Are you sure you want to delete '" + fileTreeNode.file.getName()+"'?",
                    "Confirm delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == 1)
                return; // they selected no

            boolean success = false;
            if (fileTreeNode.file.isDirectory())
                success = deleteDirectory(fileTreeNode.file);
            else
                success = fileTreeNode.file.delete();

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
            if (dir == null || !dir.exists() || !dir.isDirectory())
                return false;

            boolean success = true;
            File [] list = dir.listFiles();
            for (File file:list)
            {
                if (file.isDirectory())
                {
                    if (!deleteDirectory(file))
                        success = false;
                }
                else
                {
                    if (!file.delete())
                        success = false;
                }
            }
            if (!dir.delete())  // finally delete the actual directory
                success = false;

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