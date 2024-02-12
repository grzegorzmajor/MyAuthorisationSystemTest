package ovh.major.myauthorisationsystemtest.error;

import org.springframework.http.HttpStatus;

record ErrorResponse(String message, HttpStatus status) {
}
