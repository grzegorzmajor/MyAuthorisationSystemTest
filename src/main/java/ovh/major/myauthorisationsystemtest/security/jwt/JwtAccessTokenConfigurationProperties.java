package ovh.major.myauthorisationsystemtest.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "auth.jwt.access")
record JwtAccessTokenConfigurationProperties(
        String secret,
        long expirationMinutes,
        boolean requireNotExpired
) {
}
