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
import org.tax.mitra.model.ValidateSessionRequest;
import org.tax.mitra.service.otpService.OtpServiceListener;
import org.tax.mitra.service.sessionService.SessionServiceListener;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/v1/session")
@RequiredArgsConstructor
public class SessionController {
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    private final SessionServiceListener service;
    private final ResponseContext context;

    @PostMapping("/validate")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody ValidateSessionRequest requestModel) throws InvocationTargetException {
        logger.info("Received OTP generation request :: {}", requestModel.toString());
        service.validateSession(requestModel);
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
