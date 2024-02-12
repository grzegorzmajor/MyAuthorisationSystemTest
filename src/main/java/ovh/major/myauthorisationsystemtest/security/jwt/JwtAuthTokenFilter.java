package ovh.major.myauthorisationsystemtest.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

@Component
@Log4j2
@AllArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final JwtRefreshingTokenConfigurationProperties refreshingProperties;
    private final JwtAccessTokenConfigurationProperties accessProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/test")) {
            String authorization = null;
            Enumeration<String> authorizations = request.getHeaders("Authorization");
            while (authorizations.hasMoreElements()) {
                String authHeader = authorizations.nextElement();
                log.info("Request has authorization header: " + authHeader);
                if (authHeader.startsWith("Bearer")) {
                    authorization = authHeader;
                }
            }
            logger.info("Authorization header is: " + authorization);
            if (authorization == null) {
                filterChain.doFilter(request, response);
                return;
            }
            if (authorization.startsWith("Bearer ")) {
                if (!getTokenIssuer(authorization.substring(7))
                        .equals(JwtTokenIssuer.ACCESS_TOKEN.getValue()) ) {
                    filterChain.doFilter(request, response);
                }
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(authorization, JwtTokenIssuer.ACCESS_TOKEN);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request, response);
            }
        }

        if (path.startsWith("/ref")) {
            String authorization = null;
            Enumeration<String> authorizations = request.getHeaders("Authorization");
            while (authorizations.hasMoreElements()) {
                String authHeader = authorizations.nextElement();
                log.info("Request has authorization header: " + authHeader);
                if (authHeader.startsWith("Bearer")) {
                    authorization = authHeader;
                }
            }
            logger.info("Authorization header is: " + authorization);
            if (authorization == null) {
                filterChain.doFilter(request, response);
                return;
            }
            if (authorization.startsWith("Bearer ")) {
                if (!getTokenIssuer(authorization.substring(7))
                        .equals(JwtTokenIssuer.REFRESHING_TOKEN.getValue()) ) {
                    filterChain.doFilter(request, response);
                }
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(authorization, JwtTokenIssuer.REFRESHING_TOKEN);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request, response);
            }
        }
        filterChain.doFilter(request, response);
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
