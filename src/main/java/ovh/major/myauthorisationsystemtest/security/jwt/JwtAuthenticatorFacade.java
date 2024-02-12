package ovh.major.myauthorisationsystemtest.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ovh.major.myauthorisationsystemtest.security.login.dto.UserRequestDTO;
import ovh.major.myauthorisationsystemtest.security.login.dto.UserResponseDTO;

import java.time.*;

@Component
@EnableConfigurationProperties(value = {JwtRefreshingTokenConfigurationProperties.class, JwtAccessTokenConfigurationProperties.class})
public class JwtAuthenticatorFacade {
    private final AuthenticationManager authenticationManager;
    private final Clock clock;
    private final JwtRefreshingTokenConfigurationProperties refreshingProperties;
    private final JwtAccessTokenConfigurationProperties accessProperties;

    public JwtAuthenticatorFacade(
            @Qualifier("authenticationManagerForEndpoints")
            AuthenticationManager authenticationManager,
            Clock clock,
            JwtRefreshingTokenConfigurationProperties refreshingProperties,
            JwtAccessTokenConfigurationProperties accessProperties) {
        this.authenticationManager = authenticationManager;
        this.clock = clock;
        this.refreshingProperties = refreshingProperties;
        this.accessProperties = accessProperties;
    }

    public UserResponseDTO authenticateAndGenerateToken(UserRequestDTO userRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.name(), userRequestDto.password()));
        User user = (User) authenticate.getPrincipal();
        String name = user.getUsername();
        String token = createToken(name, JwtTokenIssuer.REFRESHING_TOKEN);
        return UserResponseDTO.builder()
                .token(token)
                .name(name)
                .build();
    }

    public String createToken(String userName, JwtTokenIssuer issuer) {
        Algorithm algorithm;
        Instant now = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant expiresAt;
        switch (issuer) {
            case REFRESHING_TOKEN : {
                algorithm = Algorithm.HMAC256(refreshingProperties.secret());
                expiresAt = now.plus(Duration.ofMinutes(refreshingProperties.expirationMinutes()));
                break;
            }
            case ACCESS_TOKEN : {
                algorithm = Algorithm.HMAC256(accessProperties.secret());
                expiresAt = now.plus(Duration.ofMinutes(accessProperties.expirationMinutes()));
                break;
            }
            default : {
                throw new IllegalArgumentException("Invalid token issuer in JwtAuthenticatorFacade");
            }
        }
        return JWT.create()
                .withSubject(userName)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer.getValue())
                .sign(algorithm);
    }

    public String getTokenIssuer(String refreshingToken) {
        return JWT.decode(refreshingToken).getIssuer();
    }
    public String getTokenSubject(String refreshingToken) {
        return JWT.decode(refreshingToken).getSubject();
    }
}