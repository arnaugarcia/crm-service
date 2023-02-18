package com.theagilemonkeys.crmservice.service.customer;

import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> findCustomers(Pageable pageable);
}
