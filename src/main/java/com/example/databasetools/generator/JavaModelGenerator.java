package com.example.databasetools.generator;

import com.example.databasetools.domain.Model;
import java.util.List;

public class JavaModelGenerator {

    public String generate(Model model) {
        var javaFile = new StringBuilder();

        // todo: package

        // import
        List<String> imports = model.getImports();
        for (var target : imports) {
            javaFile.append(String.format("import %s;\n", target));
        }
        javaFile.append('\n');

        // class comments
        List<String> comments = model.getComments();
        if (!comments.isEmpty()) {
            javaFile.append("/**\n");
            for (String comment : comments) {
                javaFile.append(" * ").append(comment).append('\n');
            }
            javaFile.append(" */\n");
        }

        // annotations
        List<String> annotations = model.getAnnotations();
        for (String annotation : annotations) {
            javaFile.append(String.format("@%s\n", annotation));
        }

        // class start
        javaFile.append(String.format("public class %s {\n\n", model.getClassName()));

        // field
        var fields = model.getFields();
        for (var field : fields) {
            // comments
            List<String> fieldComments = field.getComments();
            if (!fieldComments.isEmpty()) {
                javaFile.append("    /**\n");
                for (String comment : fieldComments) {
                    javaFile.append("     * ").append(comment).append('\n');
                }
                javaFile.append("     */\n");
            }

            // annotations
            List<String> fieldAnnotations = field.getAnnotations();
            for (String annotation : fieldAnnotations) {
                javaFile.append(String.format("    @%s\n", annotation));
            }

            // field
            javaFile.append(String.format("    %s %s %s;\n\n",
                field.getAccessModifier(),
                field.getType().getSimpleName(),
                field.getFieldName()));
        }

        // class end
        javaFile.append("}");
        return javaFile.toString();
    }
}
