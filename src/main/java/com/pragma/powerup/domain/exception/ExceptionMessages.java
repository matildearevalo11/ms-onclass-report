package com.pragma.powerup.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {
    INVALID_REQUEST("The request body is invalid."),
    BOOTCAMP_ID_INVALID("Bootcamp ID must be positive."),
    BOOTCAMP_NAME_REQUIRED("Bootcamp name is required."),
    BOOTCAMP_DESCRIPTION_REQUIRED("Bootcamp description is required."),
    LAUNCH_DATE_REQUIRED("Launch date is required."),
    DURATION_INVALID("Duration must be greater than zero."),
    CAPACITIES_REQUIRED("Capacities are required."),
    CAPACITY_INVALID("Each capacity requires a positive ID, a name and its technologies."),
    TECHNOLOGY_INVALID("Each technology requires a positive ID and a name."),
    COUNTS_INVALID("Report counts must be zero or greater."),
    AUTHENTICATION_REQUIRED("A valid token is required to access this resource."),
    DATABASE_UNAVAILABLE("The persistence operation could not be completed."),
    INTERNAL_ERROR("The request could not be completed.");

    private final String message;
}
