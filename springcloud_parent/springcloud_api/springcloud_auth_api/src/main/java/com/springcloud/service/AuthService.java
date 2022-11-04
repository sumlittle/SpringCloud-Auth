package com.springcloud.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface AuthService {
    @GetMapping("/auth/getToken")
    Map<String, Object> getToken(@RequestParam("userName") String userName, @RequestParam("password") String password);

    @GetMapping("/auth/verifyToken")
    Map<String, Object> verifyToken(@RequestParam(value = "token") String token);

    @GetMapping("/auth/refreshToken")
    Map<String, Object> refreshToken(@RequestParam(value = "refreshToken") String refreshToken);
}
