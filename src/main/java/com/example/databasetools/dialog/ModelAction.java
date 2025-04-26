package com.example.databasetools.dialog;

import com.esotericsoftware.kryo.kryo5.minlog.Log;
import com.example.databasetools.Model;
import com.example.databasetools.ui.ConfigurationDialog;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.database.psi.DbTable;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public class ModelAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(ModelAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        ConfigurationDialog dialog = new ConfigurationDialog();
        dialog.show();

        final Project project = e.getProject();
        if (project == null) {
            return;
        }
        LOG.debug(String.format("project: %s", project.getName()));

        final var psiElements = e.getData(PlatformCoreDataKeys.PSI_ELEMENT_ARRAY);
        if (psiElements == null || psiElements.length == 0) {
            return;
        }
        LOG.debug(String.format("PSI Elements : %s", Arrays.toString(psiElements)));

        final var basePath = project.getBasePath();
        if (basePath != null) {
            LOG.debug(String.format("proejct base path : %s", basePath));

            // choose directory
            var baseDir = LocalFileSystem.getInstance().findFileByPath(basePath);
            var fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
            var chosenDir = FileChooser.chooseFile(fileChooserDescriptor, project, baseDir);
            if (chosenDir == null) {
                return;
            }
            Log.debug(String.format("chosen dir : %s", chosenDir.getPath()));
            for (PsiElement psiElement : psiElements) {

                if (psiElement instanceof DbTable table) {
                    Log.debug(String.format("table: %s", table.getName()));

                    var model = new Model(table);
                    var modelFile = PsiFileFactory.getInstance(project)
                        .createFileFromText(
                            model.getClassName() + ".java",
                            JavaClassFileType.INSTANCE,
                            model.toString());
                    var psiDirectory = PsiDirectoryFactory.getInstance(project)
                        .createDirectory(chosenDir);

                    if (psiDirectory.findFile(modelFile.getName()) == null) {
                        Runnable runnable = () -> psiDirectory.add(modelFile);
                        WriteCommandAction.runWriteCommandAction(project, runnable);
                        new ReformatCodeProcessor(project, modelFile, null, false).run();
                    }
                }
            }
        }
    }
}
