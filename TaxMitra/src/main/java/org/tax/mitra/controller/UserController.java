package org.tax.mitra.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tax.mitra.common.ResponseContext;
import org.tax.mitra.model.LoginUserRequest;
import org.tax.mitra.model.RegisterUserRequest;
import org.tax.mitra.model.UniquenessCheckRequest;
import org.tax.mitra.service.userProfileService.UserServiceListener;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class UserController {
    private final UserServiceListener service;
    private final ResponseContext context;
    @PostMapping(value = "user/login",produces = "application/json",consumes = "application/json")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginUserRequest request) {
        service.loginUser(request);
        return ResponseEntity
                .status(context.getHttpStatus())
                .body(context.toResponse());
    }

    @PostMapping(value = "/user/register",produces = "application/json",consumes = "application/json")
    public ResponseEntity<?> regUser(@Valid @RequestBody RegisterUserRequest request) {
        service.registerUser(request);
        return ResponseEntity
                .status(context.getHttpStatus())
                .body(context.toResponse());
    }
    @PostMapping(value = "/user/uniqueness/check",produces = "application/json",consumes = "application/json")
    public ResponseEntity<?> uniquenessCheck(@Valid @RequestBody UniquenessCheckRequest request) {
        service.checkIfUnique(request);
        return ResponseEntity
                .status(context.getHttpStatus())
                .body(context.toResponse());
    }
}
