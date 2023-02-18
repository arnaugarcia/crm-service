package com.theagilemonkeys.crmservice.service.customer;

import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import com.theagilemonkeys.crmservice.service.customer.request.CustomerRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> findCustomers(Pageable pageable);

    CustomerDTO createCustomer(CustomerRequest customerRequest);

    CustomerDTO updateCustomer(Long id, CustomerRequest customerRequest);
}
