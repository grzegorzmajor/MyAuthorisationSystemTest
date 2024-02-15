package ovh.major.myauthorisationsystemtest.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ovh.major.myauthorisationsystemtest.security.PathsForMatchers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

@Component
@Log4j2
@AllArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {


    private final JwtRefreshingTokenConfigurationProperties refreshingProperties;
    private final JwtAccessTokenConfigurationProperties accessProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("JWT AUTHORISATION TOKEN FILTER: Processing request started.");
        String path = request.getRequestURI();
        if (Arrays.stream(PathsForMatchers.AUTHENTICATED_ENDPOINTS_WITHOUT_ACCESS_TOKEN
                        .getValues())
                .anyMatch(path::startsWith)) {
            log.info("JWT AUTHORISATION TOKEN FILTER: Start processing request without access token.");
            String authorization = getAuthorisationBearerHeader(request, "JWT AUTHORISATION TOKEN FILTER: Authorisation header is: ");
            log.info("Authorization header is: " + authorization);
            if (authorization == null) {
                log.warn("Authorisation not possible because no Authorisation Header exist in request.");
                filterChain.doFilter(request, response);
                return;
            }
            if (authorization.startsWith("Bearer ")) {
                if (!getTokenIssuer(authorization.substring(7)).equals(JwtTokenIssuer.ACCESS_TOKEN.getValue()) ) {
                    log.warn("Authorisation not possible because token has invalid issuer.");
                    filterChain.doFilter(request, response);
                    return;
                }
                try {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(authorization, JwtTokenIssuer.ACCESS_TOKEN);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    filterChain.doFilter(request, response);
                } catch (TokenExpiredException e) {
                    log.info("JWT AUTHORISATION TOKEN FILTER: Token expired!.");
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getOutputStream().println("{ \"error\": \"Token was expired!\"}");
                } catch (Exception e) {
                    log.info("JWT AUTHORISATION TOKEN FILTER: Something went wrong: " + e.getLocalizedMessage());
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getOutputStream().println("{ \"error\": \"Something went wrong during authorisation: " + e.getLocalizedMessage() + " \"}");
                }
            }
        }

        if (Arrays.stream(PathsForMatchers.ACCESS_TOKEN
                        .getValues())
                .anyMatch(path::startsWith)) {
            log.info("JWT AUTHORISATION TOKEN FILTER: Start processing request with access token.");
            String authorization = getAuthorisationBearerHeader(request, "JWT AUTHORISATION TOKEN FILTER: Authorisation header is: ");
            logger.info("Authorization header is: " + authorization);
            if (authorization == null) {
                log.warn("Authorisation not possible because no Authorisation Header exist in request.");
                filterChain.doFilter(request, response);
                return;
            }
            if (authorization.startsWith("Bearer ")) {
                if (!getTokenIssuer(authorization.substring(7)).equals(JwtTokenIssuer.REFRESHING_TOKEN.getValue()) ) {
                    log.warn("Authorisation not possible because token has invalid issuer.");
                    filterChain.doFilter(request, response);
                    return;
                }
                try {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(authorization, JwtTokenIssuer.REFRESHING_TOKEN);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    filterChain.doFilter(request, response);
                    log.info("JWT AUTHORISATION TOKEN FILTER: Processing request finished successfully.");
                } catch (TokenExpiredException e) {
                    log.info("JWT AUTHORISATION TOKEN FILTER: Token expired!.");
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getOutputStream().println("{ \"error\": \"Token was expired!\"}");
                } catch (Exception e) {
                    log.info("JWT AUTHORISATION TOKEN FILTER: Something went wrong: " + e.getLocalizedMessage());
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getOutputStream().println("{ \"error\": \"Something went wrong during authorisation: " + e.getLocalizedMessage() + " \"}");
                }
            }
        }
        log.info("JWT AUTHORISATION TOKEN FILTER: Processing request finished.");
    }

    private static String getAuthorisationBearerHeader(HttpServletRequest request, String message) {
        String authorization = null;
        Enumeration<String> authorizations = request.getHeaders("Authorization");
        while (authorizations.hasMoreElements()) {
            String authHeader = authorizations.nextElement();
            log.info(message + authHeader);
            if (authHeader.startsWith("Bearer")) {
                authorization = authHeader;
            }
        }
        return authorization;
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token, JwtTokenIssuer issuer) {
        String secretKey = switch (issuer) {
            case ACCESS_TOKEN -> accessProperties.secret();
            case REFRESHING_TOKEN -> refreshingProperties.secret();
        };
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT jwt = verifier.verify(token.substring(7));
        log.info("Decoded JWT subject is: " + jwt.getSubject());
        return new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, Collections.emptyList());
    }

    private String getTokenIssuer(String refreshingToken) {
        return JWT.decode(refreshingToken).getIssuer();
    }
}
