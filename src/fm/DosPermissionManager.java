package fm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;

public class DosPermissionManager {
    private File file;
    private Path filePath;
    private DosFileAttributes dosFileAttributes;

    DosPermissionManager(File file) throws IOException {
        this.file = file;
        this.filePath = Paths.get(file.getPath());
        dosFileAttributes = Files.readAttributes(filePath, DosFileAttributes.class);
    }

    public boolean checkIfReadOnly() {
        return dosFileAttributes.isReadOnly();
    }

    public boolean checkIfHidden() {
        return dosFileAttributes.isHidden();
    }

    public boolean checkIfSystem() {
        return dosFileAttributes.isSystem();
    }

    public void setReadOnly(boolean value) throws IOException {
        Files.setAttribute(filePath, "dos:readonly", value);
    }

    public void setHidden(boolean value) throws IOException {
        Files.setAttribute(filePath, "dos:hidden", value);
    }

    public void setSystem(boolean value) throws IOException {
        Files.setAttribute(filePath, "dos:system", value);
    }
}
