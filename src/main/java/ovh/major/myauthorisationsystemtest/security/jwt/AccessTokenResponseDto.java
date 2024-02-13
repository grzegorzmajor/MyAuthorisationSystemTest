package ovh.major.myauthorisationsystemtest.security.jwt;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AccessTokenResponseDto(
        String accessToken,
        LocalDate expireDate,
        String userName
) {
}
