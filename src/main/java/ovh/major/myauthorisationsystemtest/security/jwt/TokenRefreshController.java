package ovh.major.myauthorisationsystemtest.security.jwt;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.MethodNotAllowedException;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/access_token")
class TokenRefreshController {

    private final CreateTokenService createTokenService;
    private final JwtAccessTokenConfigurationProperties jwtAccessTokenConfigurationProperties;

    @GetMapping
    @SecurityRequirement(name = "RefreshingToken")
    public String getRefresh(HttpServletRequest request) {
        String authentication = request.getHeader("Authorization");
        if (jwtAccessTokenConfigurationProperties.requireNotExpired()){
            throw new MethodNotAllowedException("Method not allowed: application required not expired AccessToken - use endpoint with accessToken value.", null);
        }
        return getNewToken(authentication);
    }

    @NotNull
    private String getNewToken(String authentication) {
        String token = authentication.substring(7);
        String issuer = createTokenService.getTokenIssuer(token);
        String userName = createTokenService.getTokenSubject(token);
        if (issuer.equals(JwtTokenIssuer.REFRESHING_TOKEN.getValue())) {
            return createTokenService.createToken(userName,JwtTokenIssuer.ACCESS_TOKEN);
        } else {
            throw new BadCredentialsException("Refreshing token error.");
        }
    }
}