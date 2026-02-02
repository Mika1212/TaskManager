package taskmanager.user.controller;

import org.springframework.web.bind.annotation.*;
import taskmanager.security.AuthService;
import taskmanager.user.dto.LoginRequest;
import taskmanager.user.dto.RegisterRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.email(), request.password());
        return Map.of("token", token);
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest request) {
        String token = authService.register(request.email(), request.password());
        return Map.of("token", token);
    }
}
