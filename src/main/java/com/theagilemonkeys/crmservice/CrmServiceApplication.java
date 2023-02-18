package com.theagilemonkeys.crmservice;

import com.theagilemonkeys.crmservice.config.AWSClientProperties;
import com.theagilemonkeys.crmservice.config.DefaultUserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AWSClientProperties.class, DefaultUserProperties.class})
public class CrmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmServiceApplication.class, args);
    }

}
