package org.tax.mitra.service.otpService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tax.mitra.constants.ServiceType;
import org.tax.mitra.model.TriggerOtpRequestModel;
import org.tax.mitra.model.ValidateOtpRequest;
import org.tax.mitra.service.CommonService;
import org.tax.mitra.service.ServiceRegistry;
import org.tax.mitra.service.otpService.OtpServiceListener;

@Service
public class OtpServiceListenerImpl implements OtpServiceListener {

    private final ServiceRegistry serviceRegistry;

    @Autowired
    public OtpServiceListenerImpl(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Initiates the OTP generation process for a user based on the provided request details.
     *
     * <p>This method delegates OTP generation logic to the appropriate service implementation
     * resolved dynamically from the {@link ServiceRegistry} using {@link ServiceType#OTP_GENERATE}.
     *
     * <p>The underlying service is responsible for:
     * <ul>
     *   <li>Validating the mobile number</li>
     *   <li>Generating a secure OTP</li>
     *   <li>Storing OTP in cache (Redis) or persistent storage</li>
     *   <li>Sending OTP to the user via configured channel (SMS/other)</li>
     * </ul>
     *
     * <p>This method does not return a response directly; execution results are handled
     * within the underlying service layer.
     *
     * @param request the OTP generation request containing user mobile number and related metadata
     *
     * @throws ClassCastException if the resolved service is not of the expected type
     * @throws org.tax.mitra.exception.OtpException if OTP generation fails due to system or business logic errors
     */
    @Override
    public void generateOtp(TriggerOtpRequestModel request) {
        @SuppressWarnings("unchecked")
        CommonService<TriggerOtpRequestModel> service =
                (CommonService<TriggerOtpRequestModel>) serviceRegistry.getService(ServiceType.OTP_GENERATE);
        service.execute(request);
    }

    /**
     * Validates the OTP provided by the user against stored OTP data.
     *
     * <p>This method delegates OTP validation logic to the appropriate service implementation
     * resolved dynamically from the {@link ServiceRegistry} using {@link ServiceType#OTP_VALIDATE}.
     *
     * <p>The underlying service is responsible for:
     * <ul>
     *   <li>Validating input OTP format and mobile number</li>
     *   <li>Checking OTP correctness against Redis or database</li>
     *   <li>Handling retry attempts and lock conditions</li>
     *   <li>Marking OTP as used or invalid upon validation outcome</li>
     * </ul>
     *
     * <p>In case of validation failure, the underlying service will throw a domain-specific exception
     * indicating the reason (e.g., invalid OTP, expired OTP, max attempts reached).
     *
     * @param request the OTP validation request containing mobile number and OTP value
     *
     * @throws ClassCastException if the resolved service is not of the expected type
     * @throws org.tax.mitra.exception.OtpException if OTP validation fails due to system or business rule violations
     */
    @Override
    public void validateOtp(ValidateOtpRequest request) {
        @SuppressWarnings("unchecked")
        CommonService<ValidateOtpRequest> service =
                (CommonService<ValidateOtpRequest>) serviceRegistry.getService(ServiceType.OTP_VALIDATE);
        service.execute(request);
    }
}