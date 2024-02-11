package ovh.major.myauthorisationsystemtest.security;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import ovh.major.myauthorisationsystemtest.jwt.JwtAuthTokenFilter;
import ovh.major.myauthorisationsystemtest.login.forapi.LoginFacade;
import ovh.major.myauthorisationsystemtest.login.forswager.SwaggerSignInFacade;

@Configuration
@Log4j2
@AllArgsConstructor
class SecurityConfig  {

    private final JwtAuthTokenFilter jwtAuthTokenFilter;

    private final SwaggerSignInFacade swaggerSignInFacade;
    private final LoginFacade loginFacade;

    @Autowired
    public void printAllAuthenticationManagers(ApplicationContext applicationContext) {
        String[] authenticationManagers = applicationContext.getBeanNamesForType(AuthenticationManager.class);
        for (String name : authenticationManagers) {
            System.out.println(name);
        }
    }

    @Bean("authenticationManagerForSwagger")
    @Primary
    @Qualifier("authenticationManagerForSwagger")
    public AuthenticationManager authenticationManagerForSwagger() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceForSwagger(swaggerSignInFacade));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean("authenticationManagerForEndpoints")
    @Qualifier("authenticationManagerForEndpoints")
    public AuthenticationManager authenticationManagerForEndpoints() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceForEndpoints(loginFacade));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
//    @Qualifier("UserDetailsServiceForSecurity")
    public UserDetailsService userDetailsServiceForEndpoints(LoginFacade loginFacade) {
        return new LoginUserDetailsService(loginFacade);
    }

    @Bean
//    @Qualifier("UserDetailsServiceForSecurity")
    public UserDetailsService userDetailsServiceForSwagger(SwaggerSignInFacade swaggerSignInFacade) {
        return new SwagerSignInUserDetailsService(swaggerSignInFacade);
    }

//    @Order(3)
//    @Bean
//    @Qualifier("authenticationManagerForSwagger")
//    public SecurityFilterChain basicSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        log.info("Basic Security Filter Chain LOADED");
//        httpSecurity
////                .authenticationManager(authenticationManagerForSwagger())
//                .authenticationManager(authenticationManagerForEndpoints())
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((authorize) -> authorize
//                    .requestMatchers(
//                            "/v3/**",
//                            "/v3/api-docs",
//                            "/v3/api-docs/**",
//                            "/swagger-ui/index.html",
//                            "/swagger-ui/**",
//                            "/swagger-ui.html",
//                            "/api/auth/**",
//                            "/webjars/**"
//                    ).authenticated()
//                    .requestMatchers(
//                            "/login/**",
//                            "/signin").permitAll()
//                    .requestMatchers(HttpMethod.GET,
//                            "/posts").permitAll()//)
////                .httpBasic(AbstractHttpConfigurer::disable)
////                .formLogin( httpSecurityFormLoginConfigurer ->
////                        httpSecurityFormLoginConfigurer
////                                .loginPage("/signin")
////                                .successForwardUrl("/swagger-ui/index.html")
////                                .failureForwardUrl("/signin?error=true")
////                                .permitAll())
////                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .exceptionHandling(Customizer.withDefaults())
////                .addFilterAfter(jwtAuthTokenFilter, SecurityContextHolderFilter.class);
//                    .anyRequest().authenticated())
//                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(Customizer.withDefaults())
//                .addFilterAfter(jwtAuthTokenFilter, SecurityContextHolderFilter.class);
//        return httpSecurity.build();
//    }

    @Order(2)
    @Bean
    @Qualifier("authenticationManagerForEndpoints")
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("JWT Security Filter Chain LOADED");
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/test")
                .authenticationManager(authenticationManagerForEndpoints())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(Customizer.withDefaults())
                .addFilterAfter(jwtAuthTokenFilter, SecurityContextHolderFilter.class);
        return httpSecurity.build();
    }
    @Order(1)
    @Bean
    public SecurityFilterChain openedEndpoitnsSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Opened Security Filter Chain LOADED");
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(
                        "/login",
                        "/login/**")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(Customizer.withDefaults());
        return httpSecurity.build();
    }
}

