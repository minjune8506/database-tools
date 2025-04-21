package com.example.databasetools.dialog;

import com.example.databasetools.ModelGenerator;
import com.intellij.database.psi.DbTable;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Model extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) {
            return;
        }
        System.out.println(project);

        PsiElement[] psiElements = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (psiElements == null || psiElements.length == 0) {
            return;
        }
        System.out.println(Arrays.toString(psiElements));

        final String basePath = project.getBasePath();
        if (basePath != null) {
            System.out.println(basePath);

            Path projectPath = Paths.get(project.getBasePath());
            System.out.println(projectPath);

            VirtualFile chooseFile = null;
            try {
                chooseFile = VfsUtil.findFileByURL(projectPath.toUri().toURL());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            System.out.println(chooseFile);
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
            var chosenFolder = FileChooser.chooseFile(descriptor, project, chooseFile);

            System.out.println(chosenFolder);
            if (chosenFolder == null) {
                return;
            }

            for (PsiElement psiElement : psiElements) {

                if (psiElement instanceof DbTable table) {
                    System.out.println(table);

                    String model = new ModelGenerator().generate(table);

                    PsiFile file = PsiFileFactory.getInstance(project)
                        .createFileFromText("test.java", JavaClassFileType.INSTANCE, model);
                    PsiDirectory psiDirectory = PsiDirectoryFactory.getInstance(project)
                        .createDirectory(chosenFolder);

                    if (null == psiDirectory.findFile(file.getName())) {
                        Runnable r = () -> psiDirectory.add(file);

                        WriteCommandAction.runWriteCommandAction(project, r);
                    }
                }
            }
        }
    }
}
