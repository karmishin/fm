package kostygin;

import javax.swing.*;

public class FileEditDialog extends JPanel {
    private JTextField fileNameField;
    private JButton confirmButton;

    public FileEditDialog(String fileName) {
        fileNameField = new JTextField(14);
        fileNameField.setText(fileName);

        add(new JLabel("Название файла:"));
        add(this.fileNameField);
    }

    public String getFileName() {
        return fileNameField.getText();
    }
}
