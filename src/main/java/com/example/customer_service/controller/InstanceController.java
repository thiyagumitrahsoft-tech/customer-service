package com.example.customer_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InstanceController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/api/customers/instance-info")
    public Map<String, String> instanceInfo() {
        return Map.of(
                "application", applicationName,
                "port", serverPort,
                "hostname", System.getenv().getOrDefault("HOSTNAME", "local")
        );
    }
}