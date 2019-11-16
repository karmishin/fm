package kostygin;


import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileExplorer extends JFrame {

    private JTree fileManagerTree = null;

    public FileExplorer() {
        initComponents();
    }

    private void initComponents() {
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(createFileManagerTree()), new JScrollPane(createFileManagerTree()));
        jSplitPane.setDividerLocation(500);
        this.getContentPane().add(jSplitPane);
        this.setSize(1000, 500);
        this.setResizable(true);
        this.setTitle("Файловый менеджер");
    }

    private JPanel createFileManagerTree() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());
        fileManagerTree = new JTree();

        if (System.getProperty("os.name").contains("Win")) {
            fileManagerTree.setModel(new FilesContentProvider("C:\\"));
        } else {
            fileManagerTree.setModel(new FilesContentProvider("/"));
        }

        panel.add(fileManagerTree);

        return panel;
    }

    class FilesContentProvider implements TreeModel {
        private File node;

        public FilesContentProvider(String path) {
            node = new File(path);

        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {

        }

        @Override
        public Object getChild(Object parent, int index) {
            if (parent == null)
                return null;
            return ((File) parent).listFiles()[index];
        }

        @Override
        public int getChildCount(Object parent) {
            if (parent == null)
                return 0;
            return (((File) parent).listFiles() != null) ? ((File) parent).listFiles().length : 0;
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            List<File> list = Arrays.asList(((File) parent).listFiles());
            return list.indexOf(child);
        }

        @Override
        public Object getRoot() {
            return node;
        }

        @Override
        public boolean isLeaf(Object node) {
            return ((File) node).isFile();
        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {

        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {

        }

    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException,
            ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        FileExplorer explorerUI = new FileExplorer();
        SwingUtilities.invokeAndWait(() -> explorerUI.setVisible(true));
    }
}
