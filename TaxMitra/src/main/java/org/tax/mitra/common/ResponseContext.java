package org.tax.mitra.common;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.tax.mitra.model.GenericResponse;

@Component
@Data
@RequestScope
public class ResponseContext {

    private boolean success = false;
    private String code;
    private String message;
    private Object data;
    private int httpStatus = 200;

    public void success(String code, String message, Object data) {
        this.success = true;
        this.code = code;
        this.message = message;
        this.data = data;
        this.httpStatus = 200;
    }

    public void failure(String code, String message, Object data, int status) {
        this.success = false;
        this.code = code;
        this.message = message;
        this.data = data;
        this.httpStatus = status;
    }

    public GenericResponse toResponse() {
        GenericResponse response = new GenericResponse();
        response.setSuccess(this.success);
        response.setCode(this.code);
        response.setMessage(this.message);
        response.setData(this.data);
        return response;
    }
}