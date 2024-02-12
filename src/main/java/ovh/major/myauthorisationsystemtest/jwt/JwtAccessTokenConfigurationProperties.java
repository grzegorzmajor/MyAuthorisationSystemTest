package ovh.major.myauthorisationsystemtest.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "auth.jwt.access")
public record JwtAccessTokenConfigurationProperties(
        String secret,
        long expirationMinutes,
        boolean requireNotExpired
) {
}
