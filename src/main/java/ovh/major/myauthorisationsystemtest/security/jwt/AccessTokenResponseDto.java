package ovh.major.myauthorisationsystemtest.security.jwt;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record AccessTokenResponseDto(
        String accessToken,
        Timestamp expireDate,
        String userName
) {
}
