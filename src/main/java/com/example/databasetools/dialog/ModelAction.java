package com.example.databasetools.dialog;

import com.esotericsoftware.kryo.kryo5.minlog.Log;
import com.example.databasetools.domain.ModelGenerator;
import com.example.databasetools.ui.ModelGeneratorConfigurationDialog;
import com.intellij.database.psi.DbTable;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public class ModelAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(ModelAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // using the event, implement an action
        final var project = e.getProject(); // Project from the context of this event.
        final var psiElements = e.getData(PlatformCoreDataKeys.PSI_ELEMENT_ARRAY);

        if (project == null || psiElements == null) {
            return;
        }
        LOG.debug(String.format("project: %s", project.getName()));
        LOG.debug(String.format("psi Elements : %s", Arrays.toString(psiElements)));

        var modelConfigurationDialog = new ModelGeneratorConfigurationDialog(project);
        boolean isClose = modelConfigurationDialog.showAndGet();
        if (!isClose) {
            return;
        }

        var configuration = modelConfigurationDialog.getModelConfiguration();
        Log.debug(String.format("chosen directory: %s", configuration.path()));
        if (configuration.path() == null) {
            return;
        }
        var virtualDir = LocalFileSystem.getInstance().findFileByPath(configuration.path());
        if (virtualDir == null) {
            return;
        }

        for (var element : psiElements) {
            if (element instanceof DbTable table) {
                Log.debug(String.format("table: %s", table.getName()));

                var modelGenerator = new ModelGenerator(configuration);
                var model = modelGenerator.generate(table);

                var javaFile = modelGenerator.generateModelJavaFile(model);
                Log.debug(javaFile);

                var modelPsiFile = PsiFileFactory.getInstance(project)
                    .createFileFromText(
                        model.getClassName() + ".java",
                        JavaClassFileType.INSTANCE,
                        javaFile);

                var psiDirectory = PsiDirectoryFactory.getInstance(project)
                    .createDirectory(virtualDir);

                if (psiDirectory.findFile(modelPsiFile.getName()) == null) {
                    Runnable runnable = () -> {
                        psiDirectory.add(modelPsiFile);
                        Log.debug("model file created");
                    };
                    WriteCommandAction.runWriteCommandAction(project, runnable);
                }
            }
        }
    }
}