package org.tax.mitra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {
    private boolean success;
    private String code;
    private String message;
    private Object data;
    private Instant timestamp = Instant.now();
}