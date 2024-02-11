package ovh.major.myauthorisationsystemtest.infrastructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import ovh.major.myauthorisationsystemtest.jwt.JwtAuthenticatorFacade;
import ovh.major.myauthorisationsystemtest.login.dto.UserRequestDTO;
import ovh.major.myauthorisationsystemtest.login.dto.UserResponseDTO;

@RestController
@Log4j2
@AllArgsConstructor
class LoginController {

    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;

    @PostMapping("/login")
    @CrossOrigin(
            origins = "*",
            methods = RequestMethod.POST
    )
    public ResponseEntity<UserResponseDTO> authenticateUser(@RequestBody @Valid UserRequestDTO userRequestDTO) throws BadCredentialsException {
        final UserResponseDTO userResponse = jwtAuthenticatorFacade.authenticateAndGenerateToken(userRequestDTO);
        return ResponseEntity.ok(userResponse);
    }
}
