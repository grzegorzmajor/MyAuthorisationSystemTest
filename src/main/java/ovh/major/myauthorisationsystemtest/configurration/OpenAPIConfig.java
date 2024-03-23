package ovh.major.myauthorisationsystemtest.configurration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Log4j2
public class OpenAPIConfig {

    @Value("${app.url}")
    private String devUrl;
    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("grzegorz@major.ovh");
        contact.setName("Grzegorz Major");
        contact.setUrl("https://major.ovh");

        Info info = new Info()
                .title("My Authorisation System - test")
                .version("1.0")
                .contact(contact)
                .description("""
                        This API is my small, simple project for testing authorization solutions.<br><br>
                        This is just 1 version on which I tested the authorization solution for Spring Boot 3.2.x.<br>
                        <ul>Assumed use:
                        <li>several filter chains providing multiple levels of authorization (open endpoints, Swagger base, tokens for endpoints requiring authorization),</li>
                        <li>different user roles,</li>
                        <li>dual tokens - RefreshingToken, AccessToken</li></ul><br>
                        The first RefreshingToken, long-term (here 120 minutes) - is used only to renew access.<br>
                        The second AccessToken, short-term (5 minutes) - is used to authorize access to the API (here only three test endpoints).<br><br>
                        If you want to test, two usernames are available: MasterOfInterface - with the admin role, RegularClicker - with the regular user role. The password for both users is the same: pass<br><br>
                        Happy clicking!<br><br>
                        If you are a recruiter and you have made it this far, please contact me! Feedback is extremely important to me! Have a nice day and many successful recruitments!
                        """);

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer", ""))
                .components(
                        new Components()
                                .addSecuritySchemes("RefreshingToken",
                                        new SecurityScheme()
                                                .name("RefreshingToken")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                                .addSecuritySchemes("AccessToken",
                                        new SecurityScheme()
                                                .name("AccessToken")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                ))
                .info(info)
                .servers(List.of(devServer));
    }
}