package ovh.major.myauthorisationsystemtest.security.login.forswager;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "app.swagger-user")
record SingleSwaggerUser(
        String name,
        String hashedPassword
) {
}