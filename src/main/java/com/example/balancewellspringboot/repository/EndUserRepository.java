package com.example.balancewellspringboot.repository;


import com.example.balancewellspringboot.model.identity.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EndUserRepository extends JpaRepository<EndUser, String> {

    Optional<EndUser> findByUsernameAndPassword(String username, String password);
    Optional<EndUser> findByUsername(String username);
    Optional<EndUser> findByEmail(String email);
}
