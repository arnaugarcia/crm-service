package com.theagilemonkeys.crmservice.repository;

import com.theagilemonkeys.crmservice.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByEmailWithAuthorities(String email);
}
