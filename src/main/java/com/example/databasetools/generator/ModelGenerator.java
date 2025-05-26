package com.example.databasetools.generator;

import com.example.databasetools.config.ModelConfiguration;
import com.example.databasetools.domain.Field;
import com.example.databasetools.domain.Model;
import com.example.databasetools.util.ConvertUtil;
import com.example.databasetools.database.MappingProvider;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasNamed;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import com.intellij.util.containers.JBIterable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelGenerator {

    private final ModelConfiguration configuration;

    public ModelGenerator(ModelConfiguration configuration) {
        this.configuration = configuration;
    }

    public Model generate(DbTable table) {
        var model = new Model(ConvertUtil.toCamelCase(table.getName(), true));
        model.addComment(table.getComment());

        if (configuration.useLombokGetter()) {
            model.addImport("lombok.Getter");
            model.addAnnotation("Getter");
        }

        if (configuration.useNullable()) {
            model.addImport("org.jspecify.annotations.Nullable");
        }

        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(table);
        Map<String, List<String>> legacyColumns = new HashMap<>();
        if (configuration.useLegacyComment()) {
            legacyColumns = new MappingProvider().getLegacyColumns(columns);
        }

        for (DasColumn column : columns) {
            var field = new Field(
                ConvertUtil.toJavaType(column.getDasType().getTypeClass().getName()),
                ConvertUtil.toCamelCase(column.getName(), false));
            field.addComment(column.getComment());

            if (configuration.useNullable()) {
                field.addAnnotation("Nullable");
            }
            if (configuration.useLegacyComment()) {
                var comments = legacyColumns.getOrDefault(column.getName(), Collections.emptyList());
                for (String comment : comments) {
                    field.addComment(comment);
                }
            }

            model.addField(field);
        }

        return model;
    }
}
