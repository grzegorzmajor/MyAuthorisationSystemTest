package ovh.major.myauthorisationsystemtest;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ovh.major.myauthorisationsystemtest.login.dto.SingleUserDTO;
import ovh.major.myauthorisationsystemtest.swager.SwaggerSignInFacade;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
class SwagerSignInUserDetailsService implements UserDetailsService {

    private final SwaggerSignInFacade swaggerSignInFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        SingleUserDTO singleUserDTO = swaggerSignInFacade.findByName(username);
        return getUser(singleUserDTO);
    }

    private org.springframework.security.core.userdetails.User getUser(SingleUserDTO user) {
        List<String> rolesList = Collections.singletonList(user.role().name());
        List<GrantedAuthority> authorities = rolesList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.name(),
                user.password(),
                authorities);
    }
}
