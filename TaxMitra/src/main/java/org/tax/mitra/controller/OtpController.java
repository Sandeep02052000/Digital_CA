package org.tax.mitra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ResponseContext context;
    private static final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/generate")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody TriggerOtpRequestModel request) throws InvocationTargetException, JsonProcessingException {
        logger.info("Received OTP generation request :: {}", mapper.writeValueAsString(request));
        service.generateOtp(request);
        return ResponseEntity
                .status(context.getHttpStatus())
                .body(context.toResponse());
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOtp(@Valid @RequestBody ValidateOtpRequest request) throws JsonProcessingException {
        logger.info("Received OTP validation request :: {}", mapper.writeValueAsString(request));
        service.validateOtp(request);
        return ResponseEntity
                .status(context.getHttpStatus())
                .body(context.toResponse());
    }
}