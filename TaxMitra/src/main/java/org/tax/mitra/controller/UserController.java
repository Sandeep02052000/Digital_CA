package org.tax.mitra.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tax.mitra.model.LoginUserRequest;
import org.tax.mitra.service.userProfileService.UserServiceListener;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class UserController {
    private final UserServiceListener service;
    @PostMapping(value = "/login",produces = "application/json",consumes = "application/json")
    public ResponseEntity<?> userLogin(LoginUserRequest request) {
        service.loginUser(request);
    }
}
