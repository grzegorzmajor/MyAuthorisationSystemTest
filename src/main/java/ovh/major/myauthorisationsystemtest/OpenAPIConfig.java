package ovh.major.myauthorisationsystemtest;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;

import java.util.List;

@Configuration
@Log4j2
public class OpenAPIConfig {

    @Value("https://localhost")
    private String devUrl;

//    @Value("https://api.major.ovh:9666")
//    private String prodUrl;

    @Autowired
    public void printAllAuthenticationManagers(FilterChainProxy filterChainProxy) {
        filterChainProxy.getFilterChains().forEach(filterChain -> {
            log.info("\n" + filterChain.getClass() + " Filters: \n" +filterChain.getFilters().stream().map(filter -> filter.getClass().getName()).reduce((a, b) -> a + "\n" + b).orElse("No filters"));
        });
    }

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

//        Server prodServer = new Server();
//        prodServer.setUrl(prodUrl);
//        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("grzegorz@major.ovh");
        contact.setName("Grzegorz Major");
        contact.setUrl("https://major.ovh");

        //License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("My Authorisation System Application - test")
                .version("1.0")
                .contact(contact)
                .description("This API is my little project for testing solutions for authorisation system.");//.termsOfService("https://www.bezkoder.com/terms");
                //.license(mitLicense);

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .components(
                        new Components()
                                .addSecuritySchemes("Bearer",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(info)
                .servers(List.of(devServer));
    }
}