package org.tax.mitra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tax.mitra.model.TriggerOtpRequestModel;
import org.tax.mitra.model.TriggerOtpResponse;
import org.tax.mitra.model.ValidateOtpRequest;
import org.tax.mitra.service.otpService.OtpServiceListener;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class OtpController {

    private final OtpServiceListener service;

    @PostMapping("/generate-otp")
    public ResponseEntity<TriggerOtpResponse> sendOtp(@Valid @RequestBody TriggerOtpRequestModel model) throws InvocationTargetException {
        log.info("Received OTP request :: {}", model.toString());
        service.generateOtp(model);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new TriggerOtpResponse(true, "OTP sent successfully"));
    }
    @PostMapping("/validate-otp")
    public ResponseEntity<TriggerOtpResponse> validateOtp(@Valid @RequestBody ValidateOtpRequest model) throws InvocationTargetException {
        log.info("Received OTP request :: {}", model.toString());
        service.validateOtp(model);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new TriggerOtpResponse(true, "OTP sent successfully"));
    }
}