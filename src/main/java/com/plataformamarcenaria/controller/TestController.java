package com.plataformamarcenaria.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String test() {
        return "API funcionando!";
    }
    
    @PostMapping("/login")
    public Map<String, String> testLogin(@RequestBody Map<String, String> loginData) {
        return Map.of(
            "message", "Login endpoint funcionando!",
            "email", loginData.get("email"),
            "password", loginData.get("password")
        );
    }
}

