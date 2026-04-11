package org.tax.mitra.service.otpService.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tax.mitra.model.TriggerOtpRequestModel;
import org.tax.mitra.model.ValidateOtpRequest;
import org.tax.mitra.service.CommonService;
import org.tax.mitra.service.otpService.OtpServiceListener;

import java.util.Map;

@Service
public class OtpServiceListenerImpl implements OtpServiceListener {

    private final CommonService service;
    //private final NotificationService notificationService;
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    public OtpServiceListenerImpl(CommonService service) {
        this.service = service;
        //this.notificationService = notificationService;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<?> generateOtp(TriggerOtpRequestModel request) {
        Map<String, Object> requestMap = mapper.convertValue(request, Map.class);
        Map<String, Object> response = service.execute(requestMap);
        //notificationService.sendNotification(response);
        return ResponseEntity.ok(response);
    }

    /**
     * @param request
     */
    @Override
    public ResponseEntity<?> validateOtp(ValidateOtpRequest request) {
        Map<String, Object> requestMap = mapper.convertValue(request, Map.class);
        Map<String, Object> response = service.execute(requestMap);
        return ResponseEntity.ok(response);
    }
}
