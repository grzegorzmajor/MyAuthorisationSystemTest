package ovh.major.myauthorisationsystemtest;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> getParagraph(@PathVariable Integer id) {
        String response =  "If you can see this text, You logged in success for rest api.";
        return ResponseEntity.ok(response);
    }
}
