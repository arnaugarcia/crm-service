package com.theagilemonkeys.crmservice.web.rest;

import com.theagilemonkeys.crmservice.service.customer.CustomerService;
import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerResource {

    private static final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private final CustomerService costumerService;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers(Pageable pageable) {
        log.info("REST request to get all customers");
        return ResponseEntity.ok(costumerService.findCustomers(pageable));
    }
}
