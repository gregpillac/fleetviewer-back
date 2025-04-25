package com.eni.fleetviewer.back.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    @GetMapping("/public/hello")
    public String publicEndpoint() {
        return "Hello, this is a public endpoint!";
    }

    @GetMapping("/private/hello")
    public String privateEndpoint() {
        return "Hello, this is a private endpoint!";
    }
}
