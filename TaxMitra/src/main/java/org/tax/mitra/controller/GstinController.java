package org.tax.mitra.controller;

import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tax.mitra.common.ResponseContext;
import org.tax.mitra.exception.BaseException;
import org.tax.mitra.model.GenericResponse;
import org.tax.mitra.model.GstinRequestModel;
import org.tax.mitra.service.gstService.GstinServiceListener;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class GstinController {

    private final GstinServiceListener gstinService;
    private final ResponseContext context;
    @GetMapping(value = "/fetchTaxPayer", produces = "application/json")
    public ResponseEntity<?> fetchTaxPayer(@RequestParam("gstin") String gstin,@RequestParam String msisdn) {
        gstinService.fetchTaxPayer(
                GstinRequestModel.builder()
                        .gstin(gstin)
                        .phoneNumber(msisdn)
                        .build()
        );
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