package com.r2s.structure_sample.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "Bearer")

public class AdminController {
    @GetMapping
    public String getAdmin() {
        return "Hello Admin";
    }
}
