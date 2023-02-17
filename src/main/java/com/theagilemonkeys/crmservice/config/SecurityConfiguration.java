package com.theagilemonkeys.crmservice.config;

import com.theagilemonkeys.crmservice.service.authentication.UserAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.theagilemonkeys.crmservice.security.AuthoritiesConstants.ADMIN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final UserAuthentication userDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().disable()// disable CORS (Cross-Origin Resource Sharing)
                .csrf(CsrfConfigurer::disable) // disable CSRF (Cross-Site Request Forgery)
                .sessionManagement()
                    .sessionCreationPolicy(STATELESS) // no session (JSESSION cookie) will be created or used by spring security
                .and()
                    .authorizeRequests(auth -> auth.requestMatchers("/users").hasAuthority(ADMIN)) // Deny access to users endpoints for non-admin users
                    .authorizeRequests(auth -> auth.requestMatchers("/users/**").hasAuthority(ADMIN)) // Deny access to users endpoints for non-admin users
                .httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return new ProviderManager(authProvider);
    }
}
