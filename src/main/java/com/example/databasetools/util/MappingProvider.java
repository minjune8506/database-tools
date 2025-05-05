package com.example.databasetools.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MappingProvider {

    public Map<String, List<String>> getLegacyColumns(String tableName, List<String> columnNames) {
        return Collections.emptyMap();
    }

    public List<String> getLegacyTables(String tableName) {
        return Collections.emptyList();
    }
}
