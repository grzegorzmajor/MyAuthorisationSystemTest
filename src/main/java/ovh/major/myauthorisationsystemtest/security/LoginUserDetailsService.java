package ovh.major.myauthorisationsystemtest.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ovh.major.myauthorisationsystemtest.security.login.forapi.ApiLoginFacade;
import ovh.major.myauthorisationsystemtest.security.login.dto.SingleUserDTO;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
class LoginUserDetailsService implements UserDetailsService {

    private final ApiLoginFacade loginFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        SingleUserDTO singleUserDTO = loginFacade.findByName(username);
        return getUser(singleUserDTO);
    }

    private org.springframework.security.core.userdetails.User getUser(SingleUserDTO user) {
        List<String> rolesList = Collections.singletonList(user.role().name());
        List<SimpleGrantedAuthority> authorities = rolesList.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new org.springframework.security.core.userdetails.User(
                user.name(),
                user.password(),
                authorities);
    }
}
