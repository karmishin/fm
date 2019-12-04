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

    private JScrollPane leftPane, rightPane;
    private JLabel label;
    private JTree tree1, tree2;
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
        tree1 = new JTree(treeModel);
        tree2 = new JTree(treeModel);

        deselectTree(tree1, tree2);
        deselectTree(tree2, tree1);

        leftPane = new JScrollPane(createFileManagerTree(tree1));
        rightPane = new JScrollPane(createFileManagerTree(tree2));

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu editMenu = new JMenu("Изменить");
        menuBar.add(editMenu);
        JMenu viewMenu = new JMenu("Вид");
        menuBar.add(viewMenu);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        pane.add(menuBar, c);

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setLeftComponent(leftPane);
        jSplitPane.setRightComponent(rightPane);
        jSplitPane.setDividerLocation(500);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        pane.add(jSplitPane, c);

        label = new JLabel("Чтобы изменить параметры файла, нажмите на него");
        label.setHorizontalAlignment(JLabel.CENTER);

        c.gridx = 0;
        c.gridy = 2;
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

    public void deselectTree(JTree tree, JTree selectedTree) {
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tree.clearSelection();
            }
        };

        selectedTree.addMouseListener(ml);
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

        fileManagerTree.setModel(treeModel);
        fileManagerTree.setEditable(false);
        fileManagerTree.setShowsRootHandles(true);

        JTree finalFileManagerTree = fileManagerTree;
        /*MouseListener ml = new MouseAdapter() {
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


        fileManagerTree.addMouseListener(ml);*/

        panel.add(fileManagerTree);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }
}
