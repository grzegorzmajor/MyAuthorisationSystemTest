package ovh.major.myauthorisationsystemtest.login.dto;

import lombok.Builder;
import ovh.major.myauthorisationsystemtest.security.Role;

@Builder
public record SingleUserDTO(
        String name,
        String password,
        Role role
) {
}
