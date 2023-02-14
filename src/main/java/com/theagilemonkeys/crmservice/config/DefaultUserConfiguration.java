package com.theagilemonkeys.crmservice.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class DefaultUserConfiguration {
    // private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        // userService.
        System.out.println("hello world, I have just started up");
    }
}
