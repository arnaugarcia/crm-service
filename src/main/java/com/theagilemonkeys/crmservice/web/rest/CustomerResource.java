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
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
public class CustomerResource {

    private static final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private final CustomerService customerService;

    /**
     * {@code GET /customers} : get all customers.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers(@RequestParam(required = false) Pageable pageable) {
        log.info("REST request to get all customers");
        return ResponseEntity.ok(customerService.findCustomers(pageable));
    }

    /**
     * {@code POST /customers} : Create a new customer.
     *
     * @param customerRequest the request to create the customer
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customer
     */
    @PostMapping("/customers")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        log.info("REST request to create a customer");
        return ResponseEntity.status(CREATED).body(customerService.createCustomer(customerRequest));
    }


    /**
     * {@code PUT /customers} : Updates an existing customer.
     *
     * @param id              the id of the customer to update
     * @param customerRequest the request to update the customer
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the updated customer
     */
    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequest customerRequest) {
        log.info("REST request to update a customer");
        return ResponseEntity.ok(customerService.updateCustomer(id, customerRequest));
    }

    /**
     * {@code DELETE /customers} : Deletes an existing customer.
     * @param id the id of the customer to delete
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.info("REST request to delete a customer");
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
