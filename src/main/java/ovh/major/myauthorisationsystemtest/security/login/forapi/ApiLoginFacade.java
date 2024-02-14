package ovh.major.myauthorisationsystemtest.security.login.forapi;


import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ovh.major.myauthorisationsystemtest.security.login.enums.Role;
import ovh.major.myauthorisationsystemtest.security.login.dto.SingleUserDTO;


@Component
@AllArgsConstructor
@EnableConfigurationProperties(value = {SingleApiUser.class})
public class ApiLoginFacade {

    private final SingleApiUser singleUser;

    public SingleUserDTO findByName(String username) {
        if (singleUser.name().equals(username)) {
            return SingleUserDTO.builder()
                    .name(singleUser.name())
                    .password(singleUser.hashedPassword())
                    .role(Role.USER)
                    .build();
        }  else {
            throw new BadCredentialsException("User not found");
        }
    }
}
