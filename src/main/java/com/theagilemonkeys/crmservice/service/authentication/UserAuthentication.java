package com.theagilemonkeys.crmservice.service.authentication;


import com.theagilemonkeys.crmservice.domain.User;
import com.theagilemonkeys.crmservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.security.core.userdetails.User.builder;

@Service
@AllArgsConstructor
public class UserAuthentication implements UserDetailsService {

    private static final Logger LOGGER = getLogger(UserAuthentication.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        LOGGER.info("Found user with email: {} has been authorized to access", user.email());
        return builder()
                .username(user.email())
                .password(user.password())
                .authorities(user.authoritiesAsArray())
                .build();
    }
}
