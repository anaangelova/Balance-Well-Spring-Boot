package com.example.balancewellspringboot.service.interfaces;

import com.example.balancewellspringboot.model.Profile;
import com.example.balancewellspringboot.model.dto.ProfileDTO;

import java.util.List;
import java.util.Optional;

public interface ProfileService {
    Optional<Profile> save(ProfileDTO profileDTO, List<String> imagesNames);
    Profile getProfileForUsername(String username);
    Profile findById(Long id);
}
