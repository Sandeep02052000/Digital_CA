package org.tax.mitra.service.otpService;

import org.tax.mitra.model.TriggerOtpRequestModel;
import org.tax.mitra.model.ValidateOtpRequest;

import java.lang.reflect.InvocationTargetException;

public interface OtpServiceListener {
    void generateOtp(TriggerOtpRequestModel request) throws InvocationTargetException;

    void validateOtp(ValidateOtpRequest request);
}
