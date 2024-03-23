package ovh.major.myauthorisationsystemtest.security.login.forapi;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "app.api-user")
record FirstApiUser(
        String name,
        String hashedPassword
) {
}

