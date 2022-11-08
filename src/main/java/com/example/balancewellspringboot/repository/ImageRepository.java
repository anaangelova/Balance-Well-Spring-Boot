package com.example.balancewellspringboot.repository;

import com.example.balancewellspringboot.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
