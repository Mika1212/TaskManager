package taskmanager.user.controller;

import org.springframework.web.bind.annotation.*;
import taskmanager.security.AuthService;
import taskmanager.user.dto.LoginRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.email(), request.password());
        return Map.of("token", token);
    }
}
