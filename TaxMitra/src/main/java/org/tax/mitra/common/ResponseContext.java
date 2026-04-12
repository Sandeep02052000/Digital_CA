package org.tax.mitra.common;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@Data
@RequestScope
public class ResponseContext {
    private boolean success = false;
    private String code;
    private String message;
    private Object data;
    private int httpStatus = 200;
}