package com.example.databasetools.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class Model {

    private final String className;
    private final String packageName = "";
    private final List<String> imports = new ArrayList<>();
    private final List<String> comments = new ArrayList<>();
    private final List<String> annotations = new ArrayList<>();
    private final List<Field> fields = new ArrayList<>();

    public Model(String className) {
        this.className = className;
    }

    public void addImport(String importName) {
        if (StringUtils.isBlank(importName)) {
            return;
        }
        this.imports.add(importName);
    }

    public void addComment(String comment) {
        if (StringUtils.isBlank(comment)) {
            return;
        }
        this.comments.add(comment);
    }

    public void addAnnotation(String annotation) {
        if (StringUtils.isBlank(annotation)) {
            return;
        }
        this.annotations.add(annotation);
    }

    public void addField(Field field) {
        fields.add(field);
    }
}
