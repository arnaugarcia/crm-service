package com.theagilemonkeys.crmservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theagilemonkeys.crmservice.IntegrationTest;
import com.theagilemonkeys.crmservice.domain.Customer;
import com.theagilemonkeys.crmservice.repository.CustomerRepository;
import com.theagilemonkeys.crmservice.service.customer.request.CustomerRequest;
import com.theagilemonkeys.crmservice.service.customer.request.CustomerRequest.CustomerPhotoRequest;
import com.theagilemonkeys.crmservice.service.storage.CloudStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
class CustomerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO_URL = "https://arnaugarcia.com/picture.jpg";
    private static final String UPDATED_PHOTO_URL = "https://arnaugarcia.com/picture2.jpg";

    private static final byte[] DEFAULT_DATA = "AAAAAAAAAA".getBytes();

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc restCustomerMockMvc;

    @MockBean
    private CloudStorageService cloudStorageService;

    private CustomerRequest customerRequest;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        customer = createDefaultCustomer();
        customerRequest = createDefaultCustomerRequest();
    }

    private static CustomerRequest createDefaultCustomerRequest() {
        return CustomerRequest.builder()
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .photo(new CustomerPhotoRequest(DEFAULT_DATA, "image/png"))
                .build();
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

    @Test
    @WithMockUser
    void should_return_two_customers() throws Exception {
        Customer anotherCustomer = createDefaultCustomer();

        restCustomerMockMvc.perform(get("/customers")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void should_return_customers_with_pagination() throws Exception {
        Customer anotherCustomer = createDefaultCustomer();

        restCustomerMockMvc.perform(get("/customers?page=0&size=1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(customer.id()))
                .andExpect(jsonPath("$.[0].name").value(customer.name()))
                .andExpect(jsonPath("$.[0].surname").value(customer.surname()))
                .andExpect(jsonPath("$.[0].photoUrl").value(customer.photoUrl()))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    @WithMockUser("user@localhost")
    void should_create_a_customer() throws Exception {
        when(cloudStorageService.uploadObject(any())).thenReturn(new URL(DEFAULT_PHOTO_URL));

        restCustomerMockMvc.perform(post("/customers")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(customer.name()))
                .andExpect(jsonPath("$.surname").value(customer.surname()))
                .andExpect(jsonPath("$.photoUrl").value(customer.photoUrl()))
                .andExpect(jsonPath("$.lastModifiedBy").value("user@localhost"))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void should_not_create_a_customer_with_invalid_name() throws Exception {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .surname(DEFAULT_SURNAME)
                .build();

        restCustomerMockMvc.perform(post("/customers")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(customerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void should_not_create_a_customer_with_invalid_surname() throws Exception {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name(DEFAULT_NAME)
                .build();

        restCustomerMockMvc.perform(post("/customers")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(customerRequest)))
                .andExpect(status().isBadRequest());
    }

}
