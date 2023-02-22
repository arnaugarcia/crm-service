package com.theagilemonkeys.crmservice.service.customer.impl;

import com.theagilemonkeys.crmservice.domain.Customer;
import com.theagilemonkeys.crmservice.repository.CustomerRepository;
import com.theagilemonkeys.crmservice.service.customer.CustomerService;
import com.theagilemonkeys.crmservice.service.customer.dto.CustomerDTO;
import com.theagilemonkeys.crmservice.service.customer.exception.CustomerNotFound;
import com.theagilemonkeys.crmservice.service.customer.mapper.CustomerMapper;
import com.theagilemonkeys.crmservice.service.customer.request.CustomerRequest;
import com.theagilemonkeys.crmservice.service.storage.CloudStorageService;
import com.theagilemonkeys.crmservice.service.storage.request.UploadObjectRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.theagilemonkeys.crmservice.security.SecurityUtils.getCurrentUserEmail;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CloudStorageService cloudStorageService;

    @Override
    public List<CustomerDTO> findCustomers(Pageable pageable) {
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }
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

        customer.setCreatedBy(getCurrentUserEmail());
        customer.setLastModifiedBy(getCurrentUserEmail());

        return customerMapper.toDTO(customerRepository.save(customer));

    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id).orElseThrow(CustomerNotFound::new);

        customer.setName(customerRequest.name());
        customer.setSurname(customerRequest.surname());

        if (customerRequest.photo() != null) {
            cloudStorageService.removeObject(customer.photoUrl());
            String photoUrl = uploadPhotoAndGetUrlFor(customerRequest);
            customer.setPhotoUrl(photoUrl);
        }

        customer.setLastModifiedBy(getCurrentUserEmail());

        return customerMapper.toDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(CustomerNotFound::new);
        cloudStorageService.removeObject(customer.photoUrl());
        customerRepository.delete(customer);
    }

    private String uploadPhotoAndGetUrlFor(CustomerRequest customerRequest) {
        UploadObjectRequest uploadObjectRequest = UploadObjectRequest.builder()
                .data(customerRequest.photo().data())
                .fileName("customer-" + customerRequest.name() + "-" + customerRequest.surname())
                .fileExtension(customerRequest.photo().photoContentType().getExtension())
                .build();
        return cloudStorageService.uploadObject(uploadObjectRequest).toString();
    }
}
