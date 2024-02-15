package ovh.major.myauthorisationsystemtest.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

// This method can be used when an access token is generated during user login - when we generate
// both tokens during login - it is only used for token refreshment and may require unexpired.
//    @GetMapping("/{oldAccessToken}")
//    @SecurityRequirement(name = "RefreshingToken")
//    public ResponseEntity<AccessTokenResponseDto> getRefreshWithAccessToken(
//            HttpServletRequest request,
//            @PathVariable() String oldAccessToken) {
//        String authentication = request.getHeader("Authorization");
//        if (jwtAccessTokenConfigurationProperties.requireNotExpired()){
//            String secretKey = jwtAccessTokenConfigurationProperties.secret();
//            Algorithm algorithm = Algorithm.HMAC256(secretKey);
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .build();
//            verifier.verify(oldAccessToken.substring(7));
//        }
//        return ResponseEntity.ok(getNewToken(authentication));
//    }
public String getAuthorizationHeader(HttpHeaders headers) {
    if (headers.containsKey("Authorization")) {
        return headers.get("Authorization").get(0);
    }
    return null;
}

//    @GetMapping("/{oldAccessToken}")
//    @SecurityRequirement(name = "RefreshingToken")
//    public AccessTokenResponseDto getRefreshWithAccessToken(
//            HttpServletRequest request,
//            @PathVariable() String oldAccessToken) {
//        String authentication = request.getHeader("Authorization");
//        if (jwtAccessTokenConfigurationProperties.requireNotExpired()){
//            String secretKey = jwtAccessTokenConfigurationProperties.secret();
//            Algorithm algorithm = Algorithm.HMAC256(secretKey);
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .build();
//            verifier.verify(oldAccessToken.substring(7));
//        }
//        return getNewToken(authentication);
//    }

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