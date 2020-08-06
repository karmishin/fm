package fm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

public class PosixPermissionManager {

    private File file;
    private Path filePath;
    private Set<PosixFilePermission> permissionSet;

    public PosixPermissionManager(File file) throws IOException {
        this.file = file;
        this.filePath = Paths.get(file.getPath());
        this.permissionSet = Files.getPosixFilePermissions(filePath);
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

    public void setReadable(boolean readable) {
        if (readable) {
            if (!(this.checkIfReadable())) {
                permissionSet.add(PosixFilePermission.OWNER_READ);
            }
        } else {
            if (this.checkIfReadable()) {
                permissionSet.remove(PosixFilePermission.OWNER_READ);
            }
        }
    }

    public void setWritable(boolean writable) {
        if (writable) {
            if (!(this.checkIfWritable())) {
                permissionSet.add(PosixFilePermission.OWNER_WRITE);
            }
        } else {
            if (this.checkIfWritable()) {
                permissionSet.remove(PosixFilePermission.OWNER_WRITE);
            }
        }
    }

    public void setExecutable(boolean executable) {
        if (executable) {
            if (!(this.checkIfExecutable())) {
                permissionSet.add(PosixFilePermission.OWNER_EXECUTE);
            }
        } else {
            if (this.checkIfExecutable()) {
                permissionSet.remove(PosixFilePermission.OWNER_EXECUTE);
            }
        }
    }

    public void setPermissions() throws IOException {
        Files.setPosixFilePermissions(filePath, permissionSet);
    }

}
