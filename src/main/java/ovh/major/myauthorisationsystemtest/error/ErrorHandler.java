package ovh.major.myauthorisationsystemtest.error;

import org.springframework.http.HttpStatus;
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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ErrorResponse handleTokenExpiration() {
        return new ErrorResponse("Token was Expired", HttpStatus.UNAUTHORIZED);
    }
}
