package com.example.balancewellspringboot.repository;

import com.example.balancewellspringboot.model.BMI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BMIRepository extends JpaRepository<BMI, Long> {
}
