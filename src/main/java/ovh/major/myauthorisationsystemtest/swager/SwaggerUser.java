package ovh.major.myauthorisationsystemtest.swager;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
record SwaggerUser(
        String name,
        String hashedPassword
) {
    SwaggerUser(@Value("${swagger_user.name}") String name, @Value("${swagger_user.hashedPassword}") String hashedPassword) {
        log.info("User " + name + " pass " + hashedPassword);
        this.name = name;
        this.hashedPassword = hashedPassword;
    }
}