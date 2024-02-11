package ovh.major.myauthorisationsystemtest.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ovh.major.myauthorisationsystemtest.login.dto.UserRequestDTO;
import ovh.major.myauthorisationsystemtest.login.dto.UserResponseDTO;

import java.time.*;

@Component
@EnableConfigurationProperties(value = {JwtConfigurationProperties.class})
public class JwtAuthenticatorFacade {
    public JwtAuthenticatorFacade(
            @Qualifier("authenticationManagerForEndpoints")
            AuthenticationManager authenticationManager,
            Clock clock,
            JwtConfigurationProperties properties) {
        this.authenticationManager = authenticationManager;
        this.clock = clock;
        this.properties = properties;
    }

    private final AuthenticationManager authenticationManager;
    private final Clock clock;
    private final JwtConfigurationProperties properties;

    public UserResponseDTO authenticateAndGenerateToken(UserRequestDTO userRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.name(), userRequestDto.password()));
        User user = (User) authenticate.getPrincipal();
        String token = createToken(user);
        String name = user.getUsername();
        return UserResponseDTO.builder()
                .token(token)
                .name(name)
                .build();
    }

    public String createToken(User user) {
        String secretKey = properties.secret();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Instant now = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant expiresAt = now.plus(Duration.ofDays(properties.expirationDays()));
        String issuer = properties.issuer();
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
