package ovh.major.myauthorisationsystemtest.infrastructure.error;

import org.springframework.http.HttpStatus;

record LoginErrorResponse(String message, HttpStatus status) {
}
