package ovh.major.myauthorisationsystemtest.login.forswager;


import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ovh.major.myauthorisationsystemtest.security.Role;
import ovh.major.myauthorisationsystemtest.login.dto.SingleUserDTO;


@Component
@AllArgsConstructor
@EnableConfigurationProperties(value = {SwaggerUser.class})
public class SwaggerSignInFacade {

    private final SwaggerUser swaggerUser;

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
