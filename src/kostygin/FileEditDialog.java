package kostygin;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class FileEditDialog extends JPanel {
    DosPermissionManager dosPermissionManager = null;
    PosixPermissionManager posixPermissionManager = null;

    boolean isWindows = false;

    private JTextField fileNameField;
    private JCheckBox firstCheckbox;
    private JCheckBox secondCheckbox;
    private JCheckBox thirdCheckbox;

    public FileEditDialog(File file) {

        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                isWindows = true;
                dosPermissionManager = new DosPermissionManager(file);
                firstCheckbox = new JCheckBox("Только для чтения");
                secondCheckbox = new JCheckBox("Скрытый файл");
                thirdCheckbox = new JCheckBox("Системный файл");
            } else {
                posixPermissionManager = new PosixPermissionManager(file);
                firstCheckbox = new JCheckBox("Разрешить чтение");
                secondCheckbox = new JCheckBox("Разрешить запись");
                thirdCheckbox = new JCheckBox("Разрешить исполнение");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.toString());
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        fileNameField = new JTextField(14);
        fileNameField.setText(file.getName());

        setCheckBoxes();

        add(this.fileNameField);
        add(this.firstCheckbox);
        add(this.secondCheckbox);
        add(this.thirdCheckbox);
    }

    public String getFileName() {
        return fileNameField.getText();
    }

    private void setCheckBoxes() {
        if (isWindows) {
            if (dosPermissionManager.checkIfReadOnly()) {
                firstCheckbox.setSelected(true);
            }

            if (dosPermissionManager.checkIfHidden()) {
                secondCheckbox.setSelected(true);
            }

            if (dosPermissionManager.checkIfSystem()) {
                thirdCheckbox.setSelected(true);
            }

        } else {
            if (posixPermissionManager.checkIfReadable()) {
                firstCheckbox.setSelected(true);
            }

            if (posixPermissionManager.checkIfWritable()) {
                secondCheckbox.setSelected(true);
            }

            if (posixPermissionManager.checkIfExecutable()) {
                thirdCheckbox.setSelected(true);
            }
        }
    }

    public void checkUserPermissions() throws IOException {
        if (isWindows) {

            if (firstCheckbox.isSelected()) {
                dosPermissionManager.setReadOnly(true);
            } else {
                dosPermissionManager.setReadOnly(false);
            }

            if (secondCheckbox.isSelected()) {
                dosPermissionManager.setHidden(true);
            } else {
                dosPermissionManager.setHidden(false);
            }

            if (thirdCheckbox.isSelected()) {
                dosPermissionManager.setSystem(true);
            } else {
                dosPermissionManager.setSystem(false);
            }

        } else {

            if (firstCheckbox.isSelected()) {
                posixPermissionManager.setReadable(true);
            } else {
                posixPermissionManager.setReadable(false);
            }

            if (secondCheckbox.isSelected()) {
                posixPermissionManager.setWritable(true);
            } else {
                posixPermissionManager.setReadable(false);
            }

            if (thirdCheckbox.isSelected()) {
                posixPermissionManager.setExecutable(true);
            } else {
                posixPermissionManager.setExecutable(false);
            }

            posixPermissionManager.setPermissions();

        }
    }
}
