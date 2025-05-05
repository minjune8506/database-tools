package com.example.databasetools.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Model {

    private final String className;
    private final String comment;
    private final List<Field> fields = new ArrayList<>();

    public Model(String className, String comment) {
        this.className = className;
        this.comment = comment;
    }

    public void addField(Field field) {
        fields.add(field);
    }
}
