package kostygin;


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Main extends JFrame implements Runnable{

    private JTree fileManagerTree = null;
    private JLabel label;

    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;

    @Override
    public void run() {
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

        File fileRoot = new File("/home");
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        fileManagerTree = new JTree(treeModel);
        fileManagerTree.setEditable(true);
        fileManagerTree.setShowsRootHandles(true);

        ChildNodesAdder childNodesAdder = new ChildNodesAdder(fileRoot, root);
        new Thread(childNodesAdder).start();


        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = fileManagerTree.getRowForLocation(e.getX(), e.getY());
                if (selectedRow != -1) {
                TreePath selectedPath = fileManagerTree.getPathForLocation(e.getX(), e.getY());
                String fileName = selectedPath.getLastPathComponent().toString();

                String filePath = getFullPath(selectedPath);


                    if (e.getClickCount() == 1) {
                        FileEditDialog fileEditDialog = new FileEditDialog(fileName);

                        int result = JOptionPane.showConfirmDialog(panel, fileEditDialog,
                                "Изменение параметров файла", JOptionPane.DEFAULT_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            File oldFile = new File(filePath);
                            String newFilePath = oldFile.getParentFile().getAbsolutePath() + "/" + fileEditDialog.getFileName();
                            File newFile = new File(newFilePath);
                            boolean success = oldFile.renameTo(newFile);
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
