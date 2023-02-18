package com.theagilemonkeys.crmservice.service.customer.mapper;

import com.theagilemonkeys.crmservice.domain.Customer;
import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .name(customer.name())
                .surname(customer.surname())
                .photoUrl(customer.photoUrl())
                .build();
    }
}
