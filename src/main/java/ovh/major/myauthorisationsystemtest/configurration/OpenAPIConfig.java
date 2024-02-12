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
                .title("My Authorisation System Application - test")
                .version("1.0")
                .contact(contact)
                .description("This API is my little project for testing solutions for authorisation system.");

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