package ovh.major.myauthorisationsystemtest.infrastructure.error;

import org.springframework.http.HttpStatus;

record ErrorResponse(String message, HttpStatus status) {
}
