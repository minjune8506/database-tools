package com.example.databasetools.generator;

import com.example.databasetools.config.ModelConfiguration;
import com.example.databasetools.domain.Field;
import com.example.databasetools.domain.Model;
import com.example.databasetools.util.ConvertUtil;
import com.example.databasetools.util.MappingProvider;
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

        if (configuration.useLegacyComment()) {
            var legacyTables = new MappingProvider().getLegacyTables(table.getName());
            for (String legacyTable : legacyTables) {
                model.addComment(legacyTable);
            }
        }

        if (configuration.useNullable()) {
            model.addImport("org.jspecify.annotations.Nullable");
        }

        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(table);

        Map<String, List<String>> legacyColumnMap = new HashMap<>();
        if (configuration.useLegacyComment()) {
            List<String> columnNames = columns.toStream().map(DasNamed::getName).toList();
            legacyColumnMap.putAll(
                new MappingProvider().getLegacyColumns(table.getName(), columnNames));
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
                var legacyColumns = legacyColumnMap.getOrDefault(column.getName(),
                    Collections.emptyList());
                for (String legacyColumn : legacyColumns) {
                    field.addComment(legacyColumn);
                }
            }

            model.addField(field);
        }

        return model;
    }
}
