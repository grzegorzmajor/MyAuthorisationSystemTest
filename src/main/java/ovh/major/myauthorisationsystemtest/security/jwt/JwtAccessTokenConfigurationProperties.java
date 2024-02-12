package ovh.major.myauthorisationsystemtest.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "auth.jwt.access")
@SuppressWarnings("all") //to suppress the scope warning
record JwtAccessTokenConfigurationProperties(
        String secret,
        long expirationMinutes,
        boolean requireNotExpired
) {
}
