package com.ddokdoghotdog.gowalk.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HealthController {

    @Value("${server.env}")
    private String env;

    @GetMapping("/hc")
    public ResponseEntity<String> HealthCheck() {
        return ResponseEntity.ok("하이용");
    }

    @GetMapping("/env")
    public ResponseEntity<String> checkEnv() {
        return ResponseEntity.ok(env);
    }
}
