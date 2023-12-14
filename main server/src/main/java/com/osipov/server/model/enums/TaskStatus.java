package com.osipov.server.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TaskStatus {
    PENDING("PENDING"),
    IN_PROGRESS("IN PROGRESS"),
    COMPLETED("COMPLETED");

    @Getter
    private final String value;
}