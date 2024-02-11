package ovh.major.myauthorisationsystemtest.login.dto;

import lombok.Builder;

@Builder
public record UserResponseDTO(
        String token,
        String name
) {
}
