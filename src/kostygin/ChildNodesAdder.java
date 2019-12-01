package kostygin;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class ChildNodesAdder implements Runnable{
    private DefaultMutableTreeNode root;
    private File fileRoot;

    public ChildNodesAdder(File fileRoot, DefaultMutableTreeNode root) {
        this.fileRoot = fileRoot;
        this.root = root;
    }

    private void createChildren(File fileRoot, DefaultMutableTreeNode node) {
        File[] files = fileRoot.listFiles();
        if (files == null ) {
            return;
        }

        for (File file : files) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(file));
            node.add(childNode);

            if (file.getName().startsWith(".")) {
                continue;
            }

            if (file.isDirectory()) {
                createChildren(file, childNode);
            }

        }
    }

    @Override
    public void run() {
        createChildren(fileRoot, root);
    }
}
