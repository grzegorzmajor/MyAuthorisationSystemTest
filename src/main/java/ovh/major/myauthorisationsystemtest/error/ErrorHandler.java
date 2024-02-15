package ovh.major.myauthorisationsystemtest.error;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ErrorHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ErrorResponse handleBadCredentials() {
        return new ErrorResponse("Bad credentials!", HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ErrorResponse handleAccessDenied() {
        return new ErrorResponse("Access denied!", HttpStatus.FORBIDDEN);
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseBody
    public ErrorResponse handleTokenExpired() {
        return new ErrorResponse("Token expired!", HttpStatus.UNAUTHORIZED);
    }


//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(RuntimeException.class)
//    @ResponseBody
//    public ErrorResponse handleTokenExpiration() {
//        return new ErrorResponse("Token was Expired", HttpStatus.UNAUTHORIZED);
//    }
}
