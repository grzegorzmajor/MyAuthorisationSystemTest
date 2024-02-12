package ovh.major.myauthorisationsystemtest.security.login.forswager;


import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ovh.major.myauthorisationsystemtest.security.login.dto.SingleUserDTO;
import ovh.major.myauthorisationsystemtest.security.login.enums.Role;


@Component
@AllArgsConstructor
@EnableConfigurationProperties(value = {SingleSwaggerUser.class})
public class SwaggerSignInFacade {

    private final SingleSwaggerUser swaggerUser;

    public SingleUserDTO findByName(String username) {
         if (swaggerUser.name().equals(username)) {
            return SingleUserDTO.builder()
                    .name(swaggerUser.name())
                    .password(swaggerUser.hashedPassword())
                    .role(Role.SWAGGER_VIEWER)
                    .build();
        } else {
            throw new BadCredentialsException("User not found");
        }
    }
}
