package com.revrecon.backend.controller;

import com.revrecon.backend.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {
    @GetMapping("/health")
    public HealthResponse healthCheck() {
        return new HealthResponse("UP");
    }
}
