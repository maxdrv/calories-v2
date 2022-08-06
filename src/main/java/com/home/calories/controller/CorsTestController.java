package com.home.calories.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// temp just to test cors policies
@Controller
public class CorsTestController {

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello world!");
    }

    @GetMapping("/greet")
    public ResponseEntity<String> greet() {
        return ResponseEntity.ok("Hey!");
    }

    @PostMapping("/greet")
    public ResponseEntity<String> greet(@RequestBody String dto) {
        return ResponseEntity.ok("Hello, " + dto + "!");
    }

}
