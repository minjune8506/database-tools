package com.example.databasetools;

import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;

public class ModelGenerator {

    public String generate(DbTable table) {
        StringBuilder sb = new StringBuilder();

        String tableName = table.getName();

        sb.append("package ;")
            .append("\n\n")
            .append(String.format("public class %s {\n", tableName))
            .append("\n");

        for (var column : DasUtil.getColumns(table)) {
            var col = new Column(column);
            sb.append(generateColumn(col));
        }

        sb.append("}");
        return sb.toString();
    }

    private String generateColumn(Column column) {
        var type = "int";
        var name = column.getName();
        return String.format("private %s %s\n", type, name);
    }
}
