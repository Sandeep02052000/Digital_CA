package org.tax.mitra.service.otpService;

import org.springframework.http.ResponseEntity;
import org.tax.mitra.model.TriggerOtpRequestModel;
import org.tax.mitra.model.ValidateOtpRequest;

import java.lang.reflect.InvocationTargetException;

public interface OtpServiceListener {
    ResponseEntity<?> generateOtp(TriggerOtpRequestModel request) throws InvocationTargetException;

    ResponseEntity<?> validateOtp(ValidateOtpRequest request);
}
