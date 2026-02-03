package taskmanager.user.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import taskmanager.security.AuthService;
import taskmanager.user.dto.LoginRequest;
import taskmanager.user.dto.CreateUserRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return Map.of("token", token);
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid CreateUserRequest request) {
        String token = authService.register(request);
        return Map.of("token", token);
    }
}
