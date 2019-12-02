package kostygin;

import javax.swing.*;
import java.io.File;

public class FileEditDialog extends JPanel {
    private JTextField fileNameField;
    private JCheckBox readCheckBox;
    private JCheckBox writeCheckBox;
    private JCheckBox executeCheckBox;

    public FileEditDialog(File file) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        fileNameField = new JTextField(14);
        fileNameField.setText(file.getName());

        readCheckBox = new JCheckBox("Разрешить чтение");
        writeCheckBox = new JCheckBox("Разрешить запись");
        executeCheckBox = new JCheckBox("Разрешить исполнение");

        // add(new JLabel("Название файла:"));
        add(this.fileNameField);
        add(this.readCheckBox);
        add(this.writeCheckBox);
        add(this.executeCheckBox);
    }

    public String getFileName() {
        return fileNameField.getText();
    }
}
