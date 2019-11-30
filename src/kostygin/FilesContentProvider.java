package kostygin;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.Arrays;
import java.util.List;

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