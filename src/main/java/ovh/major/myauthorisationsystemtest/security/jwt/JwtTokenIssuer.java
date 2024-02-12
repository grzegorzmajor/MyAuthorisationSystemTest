package ovh.major.myauthorisationsystemtest.security.jwt;

import lombok.Getter;

@Getter
enum JwtTokenIssuer {
    ACCESS_TOKEN("LogToLife:AccessToken"),
    REFRESHING_TOKEN("LogToLife:RefreshingToken");

    private final String value;

    JwtTokenIssuer(String value) {
        this.value = value;
    }

}
