package org.tax.mitra.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Notification {
    private boolean emailNotif;
    private String email;
    private String msisdn;
    private String message;
}