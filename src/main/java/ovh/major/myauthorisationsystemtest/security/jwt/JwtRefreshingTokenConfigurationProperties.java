package ovh.major.myauthorisationsystemtest.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "auth.jwt.refreshing")
@SuppressWarnings("all") //to suppress the scope warning
record JwtRefreshingTokenConfigurationProperties(
        String secret,
        long expirationMinutes
) {
}
