package com.theagilemonkeys.crmservice.config;

import com.theagilemonkeys.crmservice.domain.Authority;
import com.theagilemonkeys.crmservice.domain.User;
import com.theagilemonkeys.crmservice.repository.AuthorityRepository;
import com.theagilemonkeys.crmservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.theagilemonkeys.crmservice.security.AuthoritiesConstants.*;
import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@AllArgsConstructor
public class DefaultUserLoader {
    private final Logger log = getLogger(DefaultUserLoader.class);
    private final DefaultUserProperties defaultUserProperties;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDefaultData() {
        createDefaultAuthorities();
        createDefaultUser();
    }

    private void createDefaultAuthorities() {
        String[] authorities = {USER, ADMIN, SUPER_ADMIN};
        stream(authorities).map(Authority::new).forEach(authorityRepository::save);
    }

    private void createDefaultUser() {
        User user = new User();
        user.setEmail(defaultUserProperties.email());
        user.setPassword(passwordEncoder.encode(defaultUserProperties.password()));

        authorityRepository.findById(SUPER_ADMIN).ifPresent(user::addAuthority);
        authorityRepository.findById(ADMIN).ifPresent(user::addAuthority);
        authorityRepository.findById(USER).ifPresent(user::addAuthority);

        userRepository.save(user);
        log.info("Default user created: {}", user);
    }
}
