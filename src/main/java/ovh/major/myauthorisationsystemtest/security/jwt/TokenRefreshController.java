package ovh.major.myauthorisationsystemtest.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.MethodNotAllowedException;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/ref")
class TokenRefreshController {

    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;
    private final JwtAccessTokenConfigurationProperties jwtAccessTokenConfigurationProperties;

    @GetMapping("/{accessToken}")
    @SecurityRequirement(name = "RefreshingToken")
    public ResponseEntity<String> getRefreshWithAccessToken(
            @RequestHeader("Authorization") String authentication,
            @PathVariable() String accessToken) {
        if (jwtAccessTokenConfigurationProperties.requireNotExpired()){
            String secretKey = jwtAccessTokenConfigurationProperties.secret();
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(accessToken.substring(7));
        }
        return getNewToken(authentication);
    }

    @GetMapping
    @SecurityRequirement(name = "RefreshingToken")
    public ResponseEntity<String> getRefresh(
            @RequestHeader("Authorization") String authentication) {
        if (jwtAccessTokenConfigurationProperties.requireNotExpired()){
            throw new MethodNotAllowedException("Method not allowed: application required not expired AccessToken - use endpoint with accessToken value.", null);
        }
        return getNewToken(authentication);
    }

    @NotNull
    private ResponseEntity<String> getNewToken(String authentication) {
        String token = authentication.substring(7);
        String issuer = jwtAuthenticatorFacade.getTokenIssuer(token);
        String userName = jwtAuthenticatorFacade.getTokenSubject(token);
        if (issuer.equals(JwtTokenIssuer.REFRESHING_TOKEN.getValue())) {
            return ResponseEntity.ok(jwtAuthenticatorFacade.createToken(userName,JwtTokenIssuer.ACCESS_TOKEN));
        } else {
            throw new BadCredentialsException("Refreshing token error.");
        }
    }
}