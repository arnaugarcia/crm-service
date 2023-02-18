package com.theagilemonkeys.crmservice.web.rest;

import com.theagilemonkeys.crmservice.IntegrationTest;
import com.theagilemonkeys.crmservice.domain.Customer;
import com.theagilemonkeys.crmservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        customer = createDefaultCustomer();
    }

    private Customer createDefaultCustomer() {
        Customer customer = new Customer();
        customer.setName(DEFAULT_NAME);
        customer.setSurname(DEFAULT_SURNAME);
        customer.setPhotoUrl(DEFAULT_PHOTO_URL);
        customer.setCreatedBy("system");
        return customerRepository.save(customer);
    }

    @Test
    @WithMockUser
    void should_return_empty_customer_list() throws Exception {
        restCustomerMockMvc.perform(get("/customers")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(customer.id()))
                .andExpect(jsonPath("$.[0].name").value(customer.name()))
                .andExpect(jsonPath("$.[0].surname").value(customer.surname()))
                .andExpect(jsonPath("$.[0].photoUrl").value(customer.photoUrl()))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_return_empty_customer_list_as_admin() throws Exception {
        restCustomerMockMvc.perform(get("/customers")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(customer.id()))
                .andExpect(jsonPath("$.[0].name").value(customer.name()))
                .andExpect(jsonPath("$.[0].surname").value(customer.surname()))
                .andExpect(jsonPath("$.[0].photoUrl").value(customer.photoUrl()))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void should_not_allow_to_access_customer_list_as_anonymous() throws Exception {
        restCustomerMockMvc.perform(get("/customers")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void should_return_customers() throws Exception {
        restCustomerMockMvc.perform(get("/customers")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(customer.id()))
                .andExpect(jsonPath("$.[0].name").value(customer.name()))
                .andExpect(jsonPath("$.[0].surname").value(customer.surname()))
                .andExpect(jsonPath("$.[0].photoUrl").value(customer.photoUrl()))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

}
