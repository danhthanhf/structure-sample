package com.r2s.structure_sample.controller;

import com.r2s.structure_sample.common.response.ResponseObject;
import com.r2s.structure_sample.dto.AuthRequest;
import com.r2s.structure_sample.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody AuthRequest authRequest) {
        var res = authService.register(authRequest);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody AuthRequest authRequest) {
        var res = authService.login(authRequest);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
