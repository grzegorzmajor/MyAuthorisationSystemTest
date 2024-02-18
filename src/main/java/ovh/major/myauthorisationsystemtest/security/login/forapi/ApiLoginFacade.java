package ovh.major.myauthorisationsystemtest.security.login.forapi;


import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ovh.major.myauthorisationsystemtest.security.login.enums.Role;
import ovh.major.myauthorisationsystemtest.security.login.dto.SingleUserDTO;


@Component
@AllArgsConstructor
@EnableConfigurationProperties(value = {FirstApiUser.class, SecondApiUser.class})
public class ApiLoginFacade {

    private final FirstApiUser firstUser;
    private final SecondApiUser secondUser;

    public SingleUserDTO findByName(String username) {
        if (firstUser.name().equals(username)) {
            return SingleUserDTO.builder()
                    .name(firstUser.name())
                    .password(firstUser.hashedPassword())
                    .role(Role.USER)
                    .build();
        }  else if (secondUser.name().equals(username)) {
            return SingleUserDTO.builder()
                    .name(firstUser.name())
                    .password(firstUser.hashedPassword())
                    .role(Role.ADMIN)
                    .build();
        } else {
            throw new BadCredentialsException("User not found");
        }
    }
}
