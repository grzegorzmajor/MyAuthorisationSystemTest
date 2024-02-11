package ovh.major.myauthorisationsystemtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ovh.major.myauthorisationsystemtest.jwt.JwtConfigurationProperties;

@SpringBootApplication
public class MyAuthorisationSystemTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyAuthorisationSystemTestApplication.class, args);
    }

}
