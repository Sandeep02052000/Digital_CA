package org.tax.mitra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class GstinController {
    @GetMapping(value = "/fetchTaxPayer",produces = "application/json")
    public ResponseEntity<?> fetchTaxPayer(@RequestParam String gstNumber) {
        return null;
    }
}
