package taskmanager.user.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @PostMapping
    public Map<String, Object> loginUser(@RequestBody Map<String, Object> body) {
        return Map.of(
                "status", "ok",
                "email", body.get("email"),
                "message", "Backend received request"
        );
    }
}
