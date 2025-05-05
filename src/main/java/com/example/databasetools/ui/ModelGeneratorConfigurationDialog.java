package com.example.databasetools.ui;

import com.example.databasetools.config.ModelConfiguration;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class ModelGeneratorConfigurationDialog extends DialogWrapper {

    private final Project project;

    private JCheckBox useLombokGetterCheckBox;
    private JCheckBox useMappingTableCommentCheckBox;
    private JCheckBox useNullable;
    private TextFieldWithBrowseButton textFieldWithBrowseButton;

    public ModelGeneratorConfigurationDialog(Project project) {
        super(project, true); // use current window as a parent
        this.project = project;
        setTitle("Model Generator");
        initValidation();
        init();
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        if (textFieldWithBrowseButton.getText().isEmpty()) {
            return new ValidationInfo("Choose directory", textFieldWithBrowseButton);
        }
        return null;
    }

    @Override
    public boolean isOKActionEnabled() {
        return textFieldWithBrowseButton.getText().isEmpty();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        var dialogPanel = new JPanel();
        var boxLayout = new BoxLayout(dialogPanel, BoxLayout.Y_AXIS);
        dialogPanel.setLayout(boxLayout);

        useLombokGetterCheckBox = new JCheckBox("use lombok @Getter", true);
        useMappingTableCommentCheckBox = new JCheckBox("use mapping table comment", true);
        useNullable = new JCheckBox("use @Nullable", true);
        textFieldWithBrowseButton = new TextFieldWithBrowseButton(new JTextField());

        dialogPanel.add(textFieldWithBrowseButton, BorderLayout.CENTER);
        dialogPanel.add(useLombokGetterCheckBox, BorderLayout.CENTER);
        dialogPanel.add(useNullable, BorderLayout.CENTER);
        dialogPanel.add(useMappingTableCommentCheckBox, BorderLayout.CENTER);

        var folderDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        var browseFolderListener = new TextBrowseFolderListener(folderDescriptor, project);
        textFieldWithBrowseButton.addBrowseFolderListener(browseFolderListener);

        return dialogPanel;
    }

    public ModelConfiguration getModelConfiguration() {
        return new ModelConfiguration(
            useLombokGetterCheckBox.isSelected(),
            useMappingTableCommentCheckBox.isSelected(),
            useNullable.isSelected(),
            textFieldWithBrowseButton.getText()
        );
    }
}
