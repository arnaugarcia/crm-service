package com.theagilemonkeys.crmservice.repository;

import com.theagilemonkeys.crmservice.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
