package fm;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class ChildNodesAdder {
    private DefaultMutableTreeNode root;
    private File fileRoot;
    private boolean showHidden;
    private File[] files;

    public ChildNodesAdder(File fileRoot, DefaultMutableTreeNode root, boolean showHidden) {
        this.fileRoot = fileRoot;
        this.root = root;
        this.showHidden = showHidden;
    }

    public void createChildren(File fileRoot, DefaultMutableTreeNode node) {
        if (showHidden) {
            files = fileRoot.listFiles();
        } else {
            files = fileRoot.listFiles(file -> !file.isHidden());
        }

        if (files == null ) {
            return;
        }

        for (File file : files) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(file));
            node.add(childNode);

            if (file.isDirectory()) {
                createChildren(file, childNode);
            }

        }
    }
}
