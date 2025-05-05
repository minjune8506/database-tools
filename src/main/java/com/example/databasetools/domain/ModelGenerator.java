package com.example.databasetools.domain;

import com.example.databasetools.config.ModelConfiguration;
import com.example.databasetools.util.ConvertUtil;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;

public class ModelGenerator {

    private final ModelConfiguration configuration;

    public ModelGenerator(ModelConfiguration configuration) {
        this.configuration = configuration;
    }

    public Model generate(DbTable table) {
        var model = new Model(
            ConvertUtil.toCamelCase(table.getName(), true),
            table.getComment());

        for (DasColumn column : DasUtil.getColumns(table)) {
            var field = new Field(
                ConvertUtil.toJavaType(column.getDasType().getTypeClass().getName()),
                ConvertUtil.toCamelCase(column.getName(), false),
                column.getComment(),
                column.isNotNull());
            model.addField(field);
        }

        return model;
    }

    public String generateModelJavaFile(Model model) {
        var javaFile = new StringBuilder();

        // todo: package

        // import
        if (configuration.useLombokGetter()) {
            javaFile.append("import lombok.Getter;\n");
        }
        if (configuration.useNullable()) {
            javaFile.append("import org.jspecify.annotations.Nullable;\n");
        }
        javaFile.append("\n");

        // class javadoc
        if (model.getComment() != null) {
            javaFile.append(generateJavaDoc(model.getComment()));
        }

        // todo: legacy table name comment

        // annotations
        if (configuration.useLombokGetter()) {
            javaFile.append("@Getter\n");
        }

        // class start
        javaFile.append(String.format("public class %s {\n\n", model.getClassName()));

        // field
        var fields = model.getFields();
        for (var field : fields) {
            // field javadoc
            if (field.getComment() != null) {
                javaFile.append(generateJavaDocWithIndent((field.getComment())));
            }

            // todo: legacy column name comment

            // annotation
            if (configuration.useNullable()) {
                javaFile.append("    @Nullable\n");
            }

            // field
            javaFile.append(String.format("    %s %s %s;\n\n",
                field.getAccessModifier(),
                field.getType().getSimpleName(),
                field.getName()));
        }

        // class end
        javaFile.append("}");
        return javaFile.toString();
    }

    private String generateJavaDocWithIndent(String contents) {
        return "    /**\n"
            + "     * " + contents + "\n"
            + "     */\n";
    }

    private String generateJavaDoc(String contents) {
        return "/**\n"
            + " * " + contents + "\n"
            + " */\n";
    }
}
