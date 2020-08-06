package fm;


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
import java.lang.reflect.InvocationTargetException;

public class Main extends JFrame implements Runnable {

    private JScrollPane leftPane, rightPane;
    private JLabel label;
    private JTree tree1, tree2;
    private DefaultMutableTreeNode root;
    private File fileRoot;
    private DefaultTreeModel treeModel;
    private JPanel pane;
    private GridBagConstraints c;

    @Override
    public void run() {
        fileRoot = new File(System.getProperty("user.home"));
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);
        ChildNodesAdder childNodesAdder = new ChildNodesAdder(fileRoot, root, false);
        childNodesAdder.createChildren(fileRoot, root);
        tree1 = new JTree(treeModel);
        tree2 = new JTree(treeModel);

        deselectTree(tree1, tree2);
        deselectTree(tree2, tree1);

        leftPane = new JScrollPane(createFileManagerTree(tree1));
        rightPane = new JScrollPane(createFileManagerTree(tree2));

        pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        createMenu();

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

        label = new JLabel(":)");
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

    public void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem createFile = new JMenuItem("Создать файл");
        createFile.addActionListener(e -> {
            try {
                createFile(false);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
            }
        });
        fileMenu.add(createFile);
        JMenuItem createDirectory = new JMenuItem("Создать папку");
        createDirectory.addActionListener(e -> {
            try {
                createFile(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
            }
        });
        fileMenu.add(createDirectory);
        JMenuItem deleteFile = new JMenuItem("Удалить файл");
        deleteFile.addActionListener(e -> deleteFile());
        fileMenu.add(deleteFile);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Изменить");
        JMenuItem editFile = new JMenuItem("Изменить выбранный файл");
        editFile.addActionListener(e -> rename());
        editMenu.add(editFile);
        menuBar.add(editMenu);

        JMenu viewMenu = new JMenu("Вид");
        JCheckBoxMenuItem showHiddenFiles = new JCheckBoxMenuItem("Показать скрытые файлы");
        showHiddenFiles.setEnabled(false); // TODO
        showHiddenFiles.addActionListener(e -> toggleShowHidden(showHiddenFiles.isSelected()));
        viewMenu.add(showHiddenFiles);
        menuBar.add(viewMenu);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        pane.add(menuBar, c);
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

    public void toggleShowHidden(boolean showHidden) {
        treeModel.setRoot(null);
        treeModel.reload();
        fileRoot = new File(System.getProperty("user.home"));
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel.setRoot(root);
        ChildNodesAdder childNodesAdder = new ChildNodesAdder(fileRoot, root, showHidden);
        childNodesAdder.createChildren(fileRoot, root);
        treeModel.reload();
    }

    public TreePath getSelectedNode() {
        if (tree1.getSelectionPath() != null) {
            return tree1.getSelectionPath();
        } else if (tree2.getSelectionPath() != null) {
            return tree2.getSelectionPath();
        }

        return null;
    }

    public File getCreationPath() {
        TreePath selectedNodePath = getSelectedNode();

        if (selectedNodePath != null) {
            File selectedFile = new File(fileRoot.getParent() + getFullPath(selectedNodePath));

            if (selectedFile.isDirectory()) {
                return selectedFile;
            } else {
                return selectedFile.getParentFile();
            }
        }

        return null;
    }

    public void createFile(boolean isDirectory) throws IOException {
        File creationPath = getCreationPath();

        if (creationPath != null) {
            String newFileName = JOptionPane.showInputDialog("Введите имя нового файла:");
            File newFile = new File(creationPath + File.separator + newFileName);

            boolean success;
            if (isDirectory) {
                success = newFile.mkdirs();
            } else {
                success = newFile.createNewFile();
            }

            treeModel.insertNodeInto(new DefaultMutableTreeNode(newFile.getName()), (DefaultMutableTreeNode) getSelectedNode().getLastPathComponent(), 0);

            if (!success) {
                JOptionPane.showMessageDialog(null, "Произошла ошибка!");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Для начала выберите папку, в которой будет создан файл");
        }
    }

    public void deleteFile() {
        TreePath selectedNodePath = getSelectedNode();

        if (selectedNodePath != null) {
            File selectedFile = new File(fileRoot.getParent() + getFullPath(selectedNodePath));
            boolean success = selectedFile.delete();

            if (success) {
                treeModel.removeNodeFromParent((DefaultMutableTreeNode) selectedNodePath.getLastPathComponent());
            } else {
                JOptionPane.showMessageDialog(null, "Произошла ошибка!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Для начала выберите удаляемый файл");
        }
    }

    public void rename() {
        TreePath selectedNodePath = getSelectedNode();

        if (selectedNodePath != null) {
            String fileName = selectedNodePath.getLastPathComponent().toString();
            String filePath = fileRoot.getParent() + getFullPath(selectedNodePath);
            File selectedFile = new File(filePath);

            FileEditDialog fileEditDialog = new FileEditDialog(selectedFile);

            int result = JOptionPane.showConfirmDialog(null, fileEditDialog,
                    "Изменение параметров файла", JOptionPane.DEFAULT_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String newFilePath = null;
                try {
                    fileEditDialog.checkUserPermissions();
                    newFilePath = selectedFile.getParentFile().getPath() + File.separator + fileEditDialog.getFileName();
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(fileEditDialog, ioException.toString());
                }
                File newFile = new File(newFilePath);
                boolean success = selectedFile.renameTo(newFile);

                if (success) {
                    treeModel.valueForPathChanged(selectedNodePath, fileEditDialog.getFileName());
                } else {
                    JOptionPane.showMessageDialog(fileEditDialog, "Произошла неизвестная ошибка!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Для начала выберите переименовываемый файл");
        }
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

        panel.add(fileManagerTree);

        return panel;
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Main());
    }
}
