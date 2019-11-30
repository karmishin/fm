package kostygin;


import com.sun.source.tree.Tree;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileExplorer extends JFrame {

    private JTree fileManagerTree = null;
    private JLabel label;

    public FileExplorer() {
        initComponents();
    }

    private void initComponents() {
        JScrollPane leftTree = new JScrollPane(createFileManagerTree());
        JScrollPane rightTree = new JScrollPane(createFileManagerTree());

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTree, rightTree);
        jSplitPane.setDividerLocation(500);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        pane.add(jSplitPane, c);

        label = new JLabel("Чтобы изменить параметры файла, нажмите на него");
        label.setHorizontalAlignment(JLabel.CENTER);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        pane.add(label, c);

        this.getContentPane().add(pane);
        this.setSize(1000, 500);
        this.setResizable(false);
        this.setTitle("Файловый менеджер");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JPanel createFileManagerTree() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());

        fileManagerTree = new JTree();

        if (System.getProperty("os.name").contains("Win")) {
            fileManagerTree.setModel( new FilesContentProvider("C:\\"));
        } else {
            fileManagerTree.setModel(new FilesContentProvider("/"));
        }

        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = fileManagerTree.getRowForLocation(e.getX(), e.getY());
                TreePath selectedPath = fileManagerTree.getPathForLocation(e.getX(), e.getY());
                String filePath = selectedPath.getLastPathComponent().toString();

                if(selectedRow != -1) {
                    if(e.getClickCount() == 2) {
                        FileEditDialog fileEditDialog = new FileEditDialog(filePath);

                        int result = JOptionPane.showConfirmDialog(panel, fileEditDialog,
                                "Изменение параметров файла", JOptionPane.DEFAULT_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            File oldFile = new File(filePath);
                            File newFile = new File(fileEditDialog.getFileName());
                            boolean success = oldFile.renameTo(newFile);
                            if (success) {
                                fileManagerTree.startEditingAtPath(selectedPath);
                            } else {
                                JOptionPane.showMessageDialog(fileEditDialog, "Произошла ошибка!");
                            }
                        }
                    }
                }
            }
        };
        fileManagerTree.addMouseListener(ml);
        panel.add(fileManagerTree);

        return panel;
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException,
            ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        FileExplorer explorerUI = new FileExplorer();
        SwingUtilities.invokeAndWait(() -> explorerUI.setVisible(true));
    }
}
