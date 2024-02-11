package ovh.major.myauthorisationsystemtest.login.forapi;


import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ovh.major.myauthorisationsystemtest.security.Role;
import ovh.major.myauthorisationsystemtest.login.dto.SingleUserDTO;


@Component
@AllArgsConstructor
@EnableConfigurationProperties(value = {SingleUser.class})
public class LoginFacade {

    private final SingleUser singleUser;

    public SingleUserDTO findByName(String username) {
        if (singleUser.name().equals(username)) {
            return SingleUserDTO.builder()
                    .name(singleUser.name())
                    .password(singleUser.hashedPassword())
                    .role(Role.ADMIN)
                    .build();
        }  else {
            throw new BadCredentialsException("User not found");
        }
    }
}
