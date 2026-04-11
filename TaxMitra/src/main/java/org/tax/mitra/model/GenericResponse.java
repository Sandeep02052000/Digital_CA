package org.tax.mitra.model;

import java.time.Instant;

public class GenericResponse<T> {

    private String status;
    private String message;
    private String errorCode;
    private T data;
    private Instant timestamp;

    public GenericResponse() {
        this.timestamp = Instant.now();
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public static <T> GenericResponse<T> success(T data, String message) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setStatus("SUCCESS");
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> GenericResponse<T> failure(String message, String errorCode) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setStatus("FAILURE");
        response.setMessage(message);
        response.setErrorCode(errorCode);
        return response;
    }
}