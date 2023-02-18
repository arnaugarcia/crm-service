package com.theagilemonkeys.crmservice.web.rest;

import com.theagilemonkeys.crmservice.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
class CustomerResourceIT {

    @Autowired
    private MockMvc restConsumerMockMvc;

    @Test
    @WithMockUser
    void should_return_empty_consumer_list() throws Exception {
        restConsumerMockMvc.perform(get("/customers")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_return_empty_consumer_list_as_admin() throws Exception {
        restConsumerMockMvc.perform(get("/customers")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void should_not_allow_to_access_consumer_list_as_anonymous() throws Exception {
        restConsumerMockMvc.perform(get("/customers")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}
