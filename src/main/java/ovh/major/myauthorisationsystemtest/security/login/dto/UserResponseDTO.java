package ovh.major.myauthorisationsystemtest.security.login.dto;

import lombok.Builder;

@Builder
public record UserResponseDTO(
        String token,
        String name
) {
}
