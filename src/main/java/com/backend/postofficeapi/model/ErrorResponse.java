package com.backend.postofficeapi.model;

public class ErrorResponse {
    private String message;
    private String description;

    public ErrorResponse(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}