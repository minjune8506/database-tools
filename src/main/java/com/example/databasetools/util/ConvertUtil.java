package com.example.databasetools.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConvertUtil {

    private ConvertUtil() {
    }

    public static String toCamelCase(String snake, boolean upperCase) {
        var camelCase = new StringBuilder();
        var tokens = snake.split("_");

        for (int i = 0; i < tokens.length; i++) {
            var token = tokens[i].toLowerCase();
            if (token.isEmpty()) {
                continue;
            }
            char firstCharacter = Character.toUpperCase(token.charAt(0));

            if (i == 0 && !upperCase) {
                firstCharacter = Character.toLowerCase(firstCharacter);
            }

            camelCase.append(firstCharacter)
                .append(token.substring(1));
        }

        return camelCase.toString();
    }

    public static Class<?> toJavaType(String type) {
        var typeUpperCase = type.toUpperCase();

        return switch (typeUpperCase) {
            case "TEXT", "VARCHAR", "CHAR" -> String.class;
            case "SMALLINT", "INTEGER", "BIGINT" -> Long.class;
            case "NUMERIC", "DECIMAL" -> BigDecimal.class;
            case "REAL" -> Float.class;
            case "DOUBLE PRECISION" -> Double.class;
            case "DATE" -> LocalDate.class;
            case "TIME" -> LocalTime.class;
            case "TIMESTAMP", "TIMESTAMPTZ" -> LocalDateTime.class;
            case "BOOLEAN" -> Boolean.class;
            default -> String.class;
        };
    }
}
