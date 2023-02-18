package com.theagilemonkeys.crmservice.service.customer;

import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import com.theagilemonkeys.crmservice.service.customer.request.CustomerRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    /**
     * Find all customers
     * @param pageable the pagination information
     * @return the list of customers
     */
    List<CustomerDTO> findCustomers(Pageable pageable);

    /**
     * Create a new customer
     * @param customerRequest the request to create the customer
     * @return the new customer
     */
    CustomerDTO createCustomer(CustomerRequest customerRequest);

    /**
     * Update an existing customer
     * @param id the id of the customer to update
     * @param customerRequest the request to update the customer
     * @return the updated customer
     */
    CustomerDTO updateCustomer(Long id, CustomerRequest customerRequest);

    /**
     * Delete a customer
     * @param id the id of the customer to delete
     */
    void deleteCustomer(Long id);
}
