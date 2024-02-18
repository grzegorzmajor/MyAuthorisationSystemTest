package ovh.major.myauthorisationsystemtest.domain;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
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

    @GetMapping("/opened")
    public ResponseEntity<TestRecord> getSomethingOpened() {
        String response =  "That everybody can see..";
        return ResponseEntity.ok(TestRecord.builder()
                //.id(15L)
                .description(response)
                .somethingElse("You welcome!")
                .build());
    }

    @GetMapping(path ="/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "AccessToken")
    public ResponseEntity<TestRecord> getSomething() {
        String response =  "If you can see this text, You logged in success for rest api.";
        return ResponseEntity.ok(TestRecord.builder()
                //.id(15L)
                .description(response)
                .somethingElse("You welcome!")
                .build());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "AccessToken")
    public ResponseEntity<TestRecord> getSomethingAsAdmin() {
        String response =  "If you can see this text, You logged in success for rest api as admin.";
        return ResponseEntity.ok(TestRecord.builder()
                //.id(15L)
                .description(response)
                .somethingElse("You are Admin?")
                .build());
    }


}
