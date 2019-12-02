package kostygin;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class FileEditDialog extends JPanel {
    AttributesManager attributesManager = null;

    private JTextField fileNameField;
    private JCheckBox readCheckBox;
    private JCheckBox writeCheckBox;
    private JCheckBox executeCheckBox;

    public FileEditDialog(File file) {
        try {
            attributesManager = new AttributesManager(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.toString());
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        fileNameField = new JTextField(14);
        fileNameField.setText(file.getName());

        readCheckBox = new JCheckBox("Разрешить чтение");
        writeCheckBox = new JCheckBox("Разрешить запись");
        executeCheckBox = new JCheckBox("Разрешить исполнение");

        setCheckBoxes();

        add(this.fileNameField);
        add(this.readCheckBox);
        add(this.writeCheckBox);
        add(this.executeCheckBox);
    }

    public String getFileName() {
        return fileNameField.getText();
    }

    private void setCheckBoxes() {
        if (attributesManager.checkIfReadable()) {
            readCheckBox.setSelected(true);
        }

        if (attributesManager.checkIfWritable()) {
            writeCheckBox.setSelected(true);
        }

        if (attributesManager.checkIfExecutable()) {
            executeCheckBox.setSelected(true);
        }

    }
}
