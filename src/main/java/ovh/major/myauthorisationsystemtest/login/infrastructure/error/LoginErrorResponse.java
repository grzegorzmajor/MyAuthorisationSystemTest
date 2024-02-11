package ovh.major.myauthorisationsystemtest.login.infrastructure.error;

import org.springframework.http.HttpStatus;

record LoginErrorResponse(String message, HttpStatus status) {
}
