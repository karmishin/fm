package kostygin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class AttributesManager {

    private File file;
    private Set<PosixFilePermission> permissionSet = new HashSet<>();
    private Set<PosixFilePermission> modifiedPermissionSet = new HashSet<>();

    public AttributesManager(File file) throws IOException {
        this.file = file;
        this.permissionSet = Files.getPosixFilePermissions(Paths.get(file.getPath()));
    }

    public boolean checkIfReadable() {
        return permissionSet.contains(PosixFilePermission.OWNER_READ);
    }

    public boolean checkIfWritable() {
        return permissionSet.contains(PosixFilePermission.OWNER_WRITE);
    }

    public boolean checkIfExecutable() {
        return permissionSet.contains(PosixFilePermission.OWNER_EXECUTE);
    }

    public void setPermissions() throws IOException {
        Files.setPosixFilePermissions(Paths.get(file.getPath()), modifiedPermissionSet);
    }

}
