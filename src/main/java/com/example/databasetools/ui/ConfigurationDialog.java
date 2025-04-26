package com.example.databasetools.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jetbrains.annotations.Nullable;

public class ConfigurationDialog extends DialogWrapper {

    public ConfigurationDialog() {
        super(true);
        setTitle("Model Generator Settings");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        var panel = new JPanel(new BorderLayout());

        var useLombokGetterCheckBox = new JCheckBox();
        var useLombokSetterCheckBox = new JCheckBox();

        panel.add(useLombokGetterCheckBox);
        panel.add(useLombokSetterCheckBox);

        return panel;
    }
}
