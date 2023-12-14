package com.osipov.server.error;

public enum ExceptionDescriptions {
    USER_NOT_FOUND("User not found"),
    TASK_NOT_FOUND("Task not found"),
    USER_ALREADY_EXISTS("User already exists"),
    PASSWORD_MISMATCH("Password mismatch"),
    NOT_AUTHOR_AND_PERFORMER("You are not the author or performer of this task");

    private final String title;

    ExceptionDescriptions(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}