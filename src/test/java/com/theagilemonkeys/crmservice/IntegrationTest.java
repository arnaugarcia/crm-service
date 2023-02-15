package com.theagilemonkeys.crmservice;

import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Base composite annotation for integration tests.
 */
@Target(TYPE)
@Retention(RUNTIME)
@SpringBootTest(classes = CrmServiceApplication.class)
public @interface IntegrationTest {
}
