package ovh.major.myauthorisationsystemtest.domain;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/test")
class TestController {

    @GetMapping
    @SecurityRequirement(name = "AccessToken")
    public ResponseEntity<String> getSomething() {
        String response =  "If you can see this text, You logged in success for rest api.";
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "AccessToken")
    public ResponseEntity<String> getSomethingAsAdmin() {
        String response =  "If you can see this text, You logged in success for rest api as admin.";
        return ResponseEntity.ok(response);
    }


}
