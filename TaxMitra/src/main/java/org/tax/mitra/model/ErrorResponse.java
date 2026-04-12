package org.tax.mitra.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private Status status;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    private String path;
    public enum Status {
        SUCCESS,
        FAILED,
        PENDING
    }
}