package org.tax.mitra.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TriggerOtpResponse extends GenericResponse{
    private boolean success;
    private String message;
}
