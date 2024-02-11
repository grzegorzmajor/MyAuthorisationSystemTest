package ovh.major.myauthorisationsystemtest.login.forswager;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "app.swagger-user")
record SwaggerUser(
        String name,
        String hashedPassword
) {
}