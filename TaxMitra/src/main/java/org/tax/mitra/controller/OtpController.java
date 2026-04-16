package org.tax.mitra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tax.mitra.common.ResponseContext;
import org.tax.mitra.model.GenericResponse;
import org.tax.mitra.model.TriggerOtpRequestModel;
import org.tax.mitra.model.ValidateOtpRequest;
import org.tax.mitra.service.otpService.OtpServiceListener;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/v1/auth/otp")
@RequiredArgsConstructor
public class OtpController {
    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);
    private final OtpServiceListener service;
    @Autowired
    ResponseContext context;

    @PostMapping("/generate")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody TriggerOtpRequestModel requestModel) throws InvocationTargetException {
        logger.info("Received OTP generation request :: {}", requestModel.toString());
        service.generateOtp(requestModel);
        return ResponseEntity
                .status(context.getHttpStatus())
                .body(createResponse(context));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOtp(@Valid @RequestBody ValidateOtpRequest request) {
        logger.info("Received OTP validation request :: {}", request.toString());
        service.validateOtp(request);
        return ResponseEntity
                .status(context.getHttpStatus())
                .body(createResponse(context));
    }
    private Object createResponse(ResponseContext context) {
        GenericResponse response = new GenericResponse();
        response.setSuccess(context.isSuccess());
        response.setCode(context.getCode());
        response.setData(context.getData());
        response.setMessage(context.getMessage());
        return  response;
    }
}