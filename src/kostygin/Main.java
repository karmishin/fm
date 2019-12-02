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

public class Main extends JFrame implements Runnable{

    private JTree leftTree = null;
    private JTree rightTree = null;
    private JLabel label;

    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;

    @Override
    public void run() {
        JScrollPane leftPane = new JScrollPane(createFileManagerTree(leftTree));
        JScrollPane rightPane = new JScrollPane(createFileManagerTree(rightTree));

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
        this.setResizable(false);
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


    private JPanel createFileManagerTree(JTree fileManagerTree) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());

        File fileRoot = new File("/home");
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        fileManagerTree = new JTree(treeModel);
        fileManagerTree.setEditable(true);
        fileManagerTree.setShowsRootHandles(true);

        ChildNodesAdder childNodesAdder = new ChildNodesAdder(fileRoot, root);
        new Thread(childNodesAdder).start();


        JTree finalFileManagerTree = fileManagerTree;
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = finalFileManagerTree.getRowForLocation(e.getX(), e.getY());
                if (selectedRow != -1) {
                    TreePath selectedPath = finalFileManagerTree.getPathForLocation(e.getX(), e.getY());
                    String fileName = selectedPath.getLastPathComponent().toString();
                    String filePath = getFullPath(selectedPath);
                    File selectedFile = new File(filePath);

                    if (e.getClickCount() == 1) {
                        FileEditDialog fileEditDialog = new FileEditDialog(selectedFile);

                        int result = JOptionPane.showConfirmDialog(panel, fileEditDialog,
                                "Изменение параметров файла", JOptionPane.DEFAULT_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            String newFilePath = selectedFile.getParentFile().getAbsolutePath() + "/" + fileEditDialog.getFileName();
                            File newFile = new File(newFilePath);
                            boolean success = selectedFile.renameTo(newFile);
                            if (success) {
                                treeModel.valueForPathChanged(selectedPath, fileEditDialog.getFileName());
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }
}
