package com.example.databasetools.config;

public record ModelConfiguration(
    boolean useLombokGetter,
    boolean useMappingTableComment,
    boolean useNullable,
    String path
) {

}
