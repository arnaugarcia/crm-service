package com.theagilemonkeys.crmservice.service.customer.impl;

import com.theagilemonkeys.crmservice.service.customer.CustomerService;
import com.theagilemonkeys.crmservice.service.storage.CloudStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CloudStorageService cloudStorageService;

}
