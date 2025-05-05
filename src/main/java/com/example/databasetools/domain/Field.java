package com.example.databasetools.domain;

import lombok.Getter;

@Getter
public class Field {

    private final String accessModifier = "private";
    private final Class<?> type;
    private final String name;
    private final String comment;
    private final boolean notNull;

    public Field(Class<?> type, String name, String comment, boolean notNull) {
        this.type = type;
        this.name = name;
        this.comment = comment;
        this.notNull = notNull;
    }
}
