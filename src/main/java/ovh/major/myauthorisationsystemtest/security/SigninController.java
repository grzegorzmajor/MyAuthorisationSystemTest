package ovh.major.myauthorisationsystemtest.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ovh.major.myauthorisationsystemtest.jwt.JwtAuthenticatorFacade;

@Controller
@RequiredArgsConstructor
public class SigninController {

    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/signin")
    public String showLoginPage() {
        return "signin";
    }

    @PostMapping("/signin")

    public String handleLogin(@ModelAttribute LoginForm loginForm) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.username, loginForm.password));
        User user = (User) authenticate.getPrincipal();
        String token = jwtAuthenticatorFacade.createToken(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        Model model = null;
        model.addAttribute("token", token);

        return "redirect:/swagger-ui/index.html";
    }

    @Getter
    @AllArgsConstructor
    public static class LoginForm {
        private String username;
        private String password;

    }

}
