package ovh.major.myauthorisationsystemtest.security.login.dto;

import lombok.Builder;
import ovh.major.myauthorisationsystemtest.security.login.enums.Role;

@Builder
public record SingleUserDTO(
        String name,
        String password,
        Role role
) {
}
