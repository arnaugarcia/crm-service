package com.theagilemonkeys.crmservice.service.customer.impl;

import com.theagilemonkeys.crmservice.repository.CustomerRepository;
import com.theagilemonkeys.crmservice.service.customer.CustomerService;
import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import com.theagilemonkeys.crmservice.service.customer.mapper.CustomerMapper;
import com.theagilemonkeys.crmservice.service.storage.CloudStorageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CloudStorageService cloudStorageService;

    @Override
    public List<CustomerDTO> findCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .stream()
                .map(customerMapper::toDTO)
                .toList();
    }
}
