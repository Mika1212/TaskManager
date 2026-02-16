package taskmanager.user.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import taskmanager.exception.UnauthorizedException;
import taskmanager.security.AuthService;
import taskmanager.user.dto.CreateUserRequest;
import taskmanager.user.dto.LoginRequest;

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

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        if (authentication == null) {
            throw new UnauthorizedException();
        }

        return Map.of(
                "userId", authentication.getPrincipal()
        );
    }
}
