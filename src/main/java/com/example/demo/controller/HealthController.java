package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class HealthController {
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}