package com.theagilemonkeys.crmservice.repository;

import com.theagilemonkeys.crmservice.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Optional<Authority> findById(String admin);
}
