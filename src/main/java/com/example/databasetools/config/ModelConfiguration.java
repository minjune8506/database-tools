package com.example.databasetools.config;

public record ModelConfiguration(
    boolean useLombokGetter,
    boolean useLegacyComment,
    boolean useNullable,
    String path
) {

}
