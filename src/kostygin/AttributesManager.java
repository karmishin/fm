package kostygin;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class AttributesManager {

    Set<PosixFilePermission> permissionSet = new HashSet<>();

    public AttributesManager(File file) {
        try {
            permissionSet = Files.getPosixFilePermissions(Paths.get(file.getPath()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }

}
