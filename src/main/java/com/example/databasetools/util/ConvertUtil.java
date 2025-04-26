package com.example.databasetools.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConvertUtil {

    public static String toUpperCamelCase(String snake) {
        var camelCase = new StringBuilder();
        var tokens = snake.split("_");
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            camelCase.append(Character.toUpperCase(token.charAt(0)))
                .append(token.substring(1).toLowerCase());
        }
        return camelCase.toString();
    }

    public static String toLowerCamelCase(String snake) {
        var camelCase = new StringBuilder();
        var tokens = snake.split("_");
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            camelCase.append(Character.toLowerCase(token.charAt(0)))
                .append(token.substring(1).toLowerCase());
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
            default -> Object.class;
        };
    }
}
