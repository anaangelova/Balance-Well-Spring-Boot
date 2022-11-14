package com.example.balancewellspringboot.repository;

import com.example.balancewellspringboot.model.LoggedDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LoggedDayRepository extends JpaRepository<LoggedDay,Long> {

    Optional<LoggedDay> findByEndUser_UsernameAndDateForDay(String username, LocalDateTime date);
}
