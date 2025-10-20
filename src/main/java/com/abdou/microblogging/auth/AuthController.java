package com.abdou.microblogging.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    AuthController(AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        return authService.login(loginRequest, request, response);
    }

    @GetMapping("/sessionExpired")
    ResponseEntity<?> sessionExpired() {
        return authService.sessionExpired();
    }

    @GetMapping("/logoutSuccess")
    ResponseEntity<?> logout() {
        return authService.logout();
    }
}
