package com.theagilemonkeys.crmservice.service.customer.mapper;

import com.theagilemonkeys.crmservice.domain.Customer;
import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import org.springframework.stereotype.Component;

import static java.lang.String.valueOf;

@Component
public class CustomerMapper {
    public CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(valueOf(customer.id()))
                .name(customer.name())
                .surname(customer.surname())
                .photoUrl(customer.photoUrl())
                .lastModifiedBy(customer.getLastModifiedBy())
                .build();
    }
}
