package ovh.major.myauthorisationsystemtest.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ovh.major.myauthorisationsystemtest.login.dto.SingleUserDTO;
import ovh.major.myauthorisationsystemtest.login.forswager.SwaggerSignInFacade;

@AllArgsConstructor
class SwagerUserDetailsService implements UserDetailsService {

    private final SwaggerSignInFacade swaggerSignInFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        SingleUserDTO singleUserDTO = swaggerSignInFacade.findByName(username);
        if(singleUserDTO == null) {
            throw new BadCredentialsException("User not found");
        }

        return User.withUsername(singleUserDTO.name())
                .password(singleUserDTO.password())
                .build();
    }
}
