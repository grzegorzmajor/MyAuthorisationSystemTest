package ovh.major.myauthorisationsystemtest.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
@RequiredArgsConstructor
public class CreateTokenService {

    private final Clock clock;
    private final JwtRefreshingTokenConfigurationProperties refreshingProperties;
    private final JwtAccessTokenConfigurationProperties accessProperties;

    public String createToken(
            String userName,
            JwtTokenIssuer issuer) {
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

    public String getTokenIssuer(String refreshedToken) {
        return JWT.decode(refreshedToken).getIssuer();
    }
    public String getTokenSubject(String refreshedToken) {
        return JWT.decode(refreshedToken).getSubject();
    }
}
