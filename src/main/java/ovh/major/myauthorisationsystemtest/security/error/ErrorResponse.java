package ovh.major.myauthorisationsystemtest.security.error;

import org.springframework.http.HttpStatus;

record ErrorResponse(String message, HttpStatus status) {
}
