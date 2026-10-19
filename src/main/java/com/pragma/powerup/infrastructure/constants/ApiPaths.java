package com.pragma.powerup.infrastructure.constants;

public final class ApiPaths {
    public static final String BOOTCAMP_REPORTS = "/internal/bootcamp-reports";
    public static final String BOOTCAMP_REPORT_PERSON_COUNT = BOOTCAMP_REPORTS + "/{id}/person-count";
    public static final String OPENAPI = "/openapi.yaml";
    public static final String OPENAPI_FILE = "openapi.yaml";

    private ApiPaths() {
    }
}
