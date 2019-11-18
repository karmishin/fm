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
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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

    public static void main(String[] args) throws InvocationTargetException, InterruptedException,
            ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        FileExplorer explorerUI = new FileExplorer();
        SwingUtilities.invokeAndWait(() -> explorerUI.setVisible(true));
    }
}
