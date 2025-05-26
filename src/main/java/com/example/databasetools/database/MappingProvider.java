package com.example.databasetools.database;

import com.intellij.database.model.DasColumn;
import com.intellij.util.containers.JBIterable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingProvider {

    private final String url = "";
    private final String user = "";
    private final String password = "";

    public Map<String, List<String>> getLegacyColumns(JBIterable<? extends DasColumn> columns) {
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            inClause.append("(?, ?)");
            if (i < columns.size() - 1) {
                inClause.append(", ");
            }
        }

        var sql = "SELECT * FROM  WHERE () IN (" + inClause + ")";
        System.out.println("generated sql: " + sql);

        Map<String, List<String>> legacyComments = new HashMap<>();

        try (
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            int paramIndex = 1;
            for (DasColumn column : columns) {
                pstmt.setString(paramIndex++, column.getTableName());
                pstmt.setString(paramIndex++, column.getName());
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                var asIsTable = rs.getString("");
                var asIsColumn = rs.getString("");
                var toBeTable = rs.getString("");
                var toBeColumn = rs.getString("");

                String key = toBeTable + "." + toBeColumn;
                String value = asIsTable + "." + asIsColumn;
                if (legacyComments.get(key) == null) {
                    var newValue = new ArrayList<String>();
                    newValue.add(value);
                    legacyComments.put(key, newValue);
                    continue;
                }
                legacyComments.get(key).add(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return legacyComments;
    }
}
