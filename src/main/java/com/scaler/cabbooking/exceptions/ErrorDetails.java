package com.scaler.cabbooking.exceptions;

import java.time.LocalDateTime;

public class ErrorDetails {
    private String error;
    private String details;
    private LocalDateTime timestamp;

    public ErrorDetails(String error, String details, LocalDateTime timestamp) {
        super();
        this.error = error;
        this.details = details;
        this.timestamp = timestamp;
    }
}
