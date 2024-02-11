package ovh.major.myauthorisationsystemtest.login.forapi;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "app.api-user")
record SingleUser(
        String name,
        String hashedPassword
) {
}

