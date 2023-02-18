package com.theagilemonkeys.crmservice.service.customer.impl;

import com.theagilemonkeys.crmservice.domain.Customer;
import com.theagilemonkeys.crmservice.repository.CustomerRepository;
import com.theagilemonkeys.crmservice.security.SecurityUtils;
import com.theagilemonkeys.crmservice.service.customer.CustomerService;
import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import com.theagilemonkeys.crmservice.service.customer.mapper.CustomerMapper;
import com.theagilemonkeys.crmservice.service.customer.request.CustomerRequest;
import com.theagilemonkeys.crmservice.service.storage.CloudStorageService;
import com.theagilemonkeys.crmservice.service.storage.request.UploadObjectRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.theagilemonkeys.crmservice.service.customer.request.PhotoFormat.from;

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

    @Override
    public CustomerDTO createCustomer(CustomerRequest customerRequest) {

        Customer customer = new Customer();
        customer.setName(customerRequest.name());
        customer.setSurname(customerRequest.surname());

        if (customerRequest.photo() != null) {
            String photoUrl = uploadPhotoAndGetUrlFor(customerRequest);
            customer.setPhotoUrl(photoUrl);
        }

        customer.setCreatedBy(SecurityUtils.getCurrentUserEmail());
        customer.setLastModifiedBy(SecurityUtils.getCurrentUserEmail());

        return customerMapper.toDTO(customerRepository.save(customer));

    }

    private String uploadPhotoAndGetUrlFor(CustomerRequest customerRequest) {
        UploadObjectRequest uploadObjectRequest = UploadObjectRequest.builder()
                .data(customerRequest.photo().data())
                .fileName("customer-" + customerRequest.name() + "-" + customerRequest.surname())
                .fileExtension(from(customerRequest.photo().photoContentType()).getExtension())
                .build();
        return cloudStorageService.uploadObject(uploadObjectRequest).toString();
    }
}
