package taskmanager.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import taskmanager.user.factory.UserTestFactory;
import taskmanager.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void shouldGenerateAndParseToken() {
        User user = UserTestFactory.withEmail("test@example.com");

        String token = jwtService.generateToken(user);
        assertThat(token).isNotBlank();

        Claims claims = jwtService.parse(token);
        assertThat(claims.getSubject()).isEqualTo(user.getId().toString());
        assertThat(claims.get("email")).isEqualTo("test@example.com");
    }

    @Test
    void shouldFailOnInvalidToken() {
        String invalidToken = "invalid.token.value";

        assertThatThrownBy(() -> jwtService.parse(invalidToken))
                .isInstanceOf(io.jsonwebtoken.JwtException.class);
    }
}
