package com.example.databasetools.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class Field {

    private final String accessModifier = "private";
    private final Class<?> type;
    private final String fieldName;

    private final List<String> annotations = new ArrayList<>();
    private final List<String> comments = new ArrayList<>();

    public Field(Class<?> type, String fieldName) {
        this.type = type;
        this.fieldName = fieldName;
    }

    public void addAnnotation(String annotation) {
        if (StringUtils.isBlank(annotation)) {
            return;
        }
        this.annotations.add(annotation);
    }

    public void addComment(String comment) {
        if (StringUtils.isBlank(comment)) {
            return;
        }
        this.comments.add(comment);
    }
}
