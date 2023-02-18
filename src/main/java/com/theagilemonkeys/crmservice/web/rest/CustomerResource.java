package com.theagilemonkeys.crmservice.web.rest;

import com.theagilemonkeys.crmservice.service.customer.CustomerService;
import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import com.theagilemonkeys.crmservice.service.customer.request.CustomerRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
public class CustomerResource {

    private static final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private final CustomerService costumerService;

    /**
     * {@code GET /customers} : get all customers.
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers(Pageable pageable) {
        log.info("REST request to get all customers");
        return ResponseEntity.ok(costumerService.findCustomers(pageable));
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid CustomerRequest customerRequest) {
        log.info("REST request to create a customer");
        return ResponseEntity.status(CREATED).body(costumerService.createCustomer(customerRequest));
    }
}
