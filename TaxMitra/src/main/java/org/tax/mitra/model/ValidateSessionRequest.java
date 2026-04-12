package org.tax.mitra.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidateSessionRequest extends RequestContext {
    private String sessionId;
    private String phoneNumber;
}
