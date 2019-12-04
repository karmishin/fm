package kostygin;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame implements Runnable{

    private JScrollPane leftPane, rightPane;
    private JLabel label;

    private DefaultMutableTreeNode root;
    private File fileRoot;
    private DefaultTreeModel treeModel;

    @Override
    public void run() {
        fileRoot = new File(System.getProperty("user.home"));
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        ChildNodesAdder childNodesAdder = new ChildNodesAdder(fileRoot, root);
        new Thread(childNodesAdder).start();

        leftPane = new JScrollPane(createFileManagerTree());
        rightPane = new JScrollPane(createFileManagerTree());

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setLeftComponent(leftPane);
        jSplitPane.setRightComponent(rightPane);

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
        this.setResizable(true);
        this.setTitle("Файловый менеджер");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public String getFullPath(TreePath selectedPath) {
        StringBuilder sb = new StringBuilder();
        Object[] nodes = selectedPath.getPath();
        for (int i = 0; i < nodes.length; i++) {
            sb.append(File.separatorChar).append(nodes[i].toString());
        }

        return sb.toString();
    }


    private JPanel createFileManagerTree() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());

        JTree fileManagerTree = new JTree(treeModel);

        fileManagerTree.setEditable(false);
        fileManagerTree.setShowsRootHandles(true);

        JTree finalFileManagerTree = fileManagerTree;
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = finalFileManagerTree.getRowForLocation(e.getX(), e.getY());
                if (selectedRow != -1) {
                    TreePath selectedPath = finalFileManagerTree.getPathForLocation(e.getX(), e.getY());
                    String fileName = selectedPath.getLastPathComponent().toString();
                    String filePath = fileRoot.getParent() + getFullPath(selectedPath);
                    File selectedFile = new File(filePath);

                    if (e.getClickCount() == 1) {
                        FileEditDialog fileEditDialog = new FileEditDialog(selectedFile);

                        int result = JOptionPane.showConfirmDialog(panel, fileEditDialog,
                                "Изменение параметров файла", JOptionPane.DEFAULT_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            String newFilePath = null;
                            try {
                                fileEditDialog.checkUserPermissions();
                                newFilePath = selectedFile.getParentFile().getPath() + "/" + fileEditDialog.getFileName();

                            } catch (IOException ioException) {
                                JOptionPane.showMessageDialog(fileEditDialog, ioException.toString());
                            }
                            File newFile = new File(newFilePath);
                            boolean success = selectedFile.renameTo(newFile);

                            if (success) {
                                treeModel.valueForPathChanged(selectedPath, fileEditDialog.getFileName());
                            } else {
                                JOptionPane.showMessageDialog(fileEditDialog, "Произошла неизвестная ошибка!");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }
}
