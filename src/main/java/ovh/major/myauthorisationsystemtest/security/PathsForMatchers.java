package ovh.major.myauthorisationsystemtest.security;

import lombok.Getter;

@Getter
public enum PathsForMatchers {
    SWAGGER_INDEX(new String[] {
            "/swagger-ui/index.html"}),
    SWAGGER_ALL(new String[]{
            "/v3/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/auth/**",
            "/webjars/**"}),
    ACCESS_TOKEN(new String[]{"/access_token"}),
    OPENED_ENDPOINTS(new String[]{
            "/login",
            "/login/**"}),
    AUTHENTICATED_ENDPOINTS_WITHOUT_ACCESS_TOKEN(new String[]{
            "/test",
            "/test/admin"}),
    AUTHENTICATED_ENDPOINTS(new String[]{
            "/test",
            "/test/admin",
            "/access_token"});

    private final String[] values;

    PathsForMatchers(String[] values) {
        this.values = values;
    }
}
