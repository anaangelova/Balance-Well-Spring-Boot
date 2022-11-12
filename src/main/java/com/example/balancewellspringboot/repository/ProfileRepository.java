package com.example.balancewellspringboot.repository;

import com.example.balancewellspringboot.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query(value = "SELECT * FROM Profile p WHERE p.end_user_username = ?1 AND p.date_of_creation >= ALL (SELECT p2.date_of_creation FROM Profile p2 WHERE p2.end_user_username = ?1)"
            , nativeQuery = true)
    Profile getLatestProfileForUsername(String username);

    List<Profile> findAllByEndUser_Username(String username);
}
