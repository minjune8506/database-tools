package com.example.databasetools.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class ConvertUtilTest {

    @Test
    public void testSnakeCaseToCamelCase() {
        assertEquals("", ConvertUtil.toCamelCase("", false));
        assertEquals("", ConvertUtil.toCamelCase("", true));

        assertEquals("snakeCase", ConvertUtil.toCamelCase("snake_case", false));
        assertEquals("snakeCaseTest", ConvertUtil.toCamelCase("snake_case_test", false));

        assertEquals("SnakeCase", ConvertUtil.toCamelCase("snake_case", true));
        assertEquals("SnakeCaseTest", ConvertUtil.toCamelCase("snake_case_test", true));
    }
}
