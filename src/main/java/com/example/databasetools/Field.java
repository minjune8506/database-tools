package com.example.databasetools;

import com.example.databasetools.util.ConvertUtil;
import com.intellij.database.model.DasColumn;
import org.apache.commons.lang3.StringUtils;

public class Field {

    private final String accessModifier = "private";
    private final Class<?> type;
    private final String name;
    private final String comment;
    private final boolean notNull;

    public Field(DasColumn column) {
        this.type = ConvertUtil.toJavaType(column.getDasType().getTypeClass().getName());
        this.name = ConvertUtil.toLowerCamelCase(column.getName());
        this.notNull = column.isNotNull();
        this.comment = column.getComment();
    }

    @Override
    public String toString() {
        String annotation = StringUtils.EMPTY;
        if (!notNull) {
            annotation = "@Nullable\n";
        }

        String comment = StringUtils.EMPTY;
        if (this.comment != null) {
            comment = "/**\n"
                + " *" + this.comment + '\n'
                + " */\n";
        }
        return comment +
            annotation +
            String.format(    "%s %s %s;\n", accessModifier, type.getSimpleName(), name);
    }
}
