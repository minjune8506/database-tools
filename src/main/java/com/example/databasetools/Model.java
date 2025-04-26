package com.example.databasetools;

import com.example.databasetools.util.ConvertUtil;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Model {

    private final String className;
    private final String comment;
    private final List<Field> fields = new ArrayList<>();

    public Model(DbTable table) {
        this.className = ConvertUtil.toUpperCamelCase(table.getName());
        this.comment = table.getComment();

        for (DasColumn column : DasUtil.getColumns(table)) {
            fields.add(new Field(column));
        }
    }

    @Override
    public String toString() {
        var model = new StringBuilder();
        if (comment != null) {
            model.append("/**\n")
                .append(" *").append(comment).append('\n')
                .append(" */\n");
        }
        model.append("public class ").append(className).append(" {\n\n");
        for (var field : fields) {
            model.append(field.toString()).append("\n");
        }
        model.append("}");
        return model.toString();
    }
}
