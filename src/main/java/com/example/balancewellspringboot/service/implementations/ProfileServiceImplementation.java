package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.model.*;
import com.example.balancewellspringboot.model.dto.ProfileDTO;
import com.example.balancewellspringboot.model.exceptions.ProfileDoesNotExist;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.repository.BMIRepository;
import com.example.balancewellspringboot.repository.EndUserRepository;
import com.example.balancewellspringboot.repository.ImageRepository;
import com.example.balancewellspringboot.repository.ProfileRepository;
import com.example.balancewellspringboot.service.interfaces.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileServiceImplementation implements ProfileService {

    private final ProfileRepository profileRepository;
    private final EndUserRepository endUserRepository;
    private final ImageRepository imageRepository;
    private final BMIRepository bmiRepository;

    public ProfileServiceImplementation(ProfileRepository profileRepository, EndUserRepository endUserRepository, ImageRepository imageRepository, BMIRepository bmiRepository) {
        this.profileRepository = profileRepository;
        this.endUserRepository = endUserRepository;
        this.imageRepository = imageRepository;
        this.bmiRepository = bmiRepository;
    }

    @Override
    @Transactional
    public Optional<Profile> save(ProfileDTO profileDTO, List<String> imagesNames) {
        Double height = profileDTO.getHeight();
        Double weight = profileDTO.getWeight();
        Integer age = profileDTO.getAge();
        Goal goal = Goal.valueOf(profileDTO.getGoal().toUpperCase().replace(" ","_"));
        Activity activity = Activity.valueOf(profileDTO.getActivity().toUpperCase());
        Sex sex = profileDTO.isSex() ? Sex.MALE : Sex.FEMALE;
        EndUser currentUser = endUserRepository.findByUsername(profileDTO.getEndUser()).orElseThrow();
        BMI bmi = bmiRepository.save(calculateBMI(height,weight));

        Profile profile = Profile
                .builder()
                .height(height)
                .weight(weight)
                .age(age)
                .sex(sex)
                .goalWeight(profileDTO.getGoalWeight())
                .goal(goal)
                .activity(activity)
                .endUser(currentUser)
                .BMI(bmi)
                .dateOfCreation(LocalDateTime.now())
                .totalCaloriesPerDay(calculateTotalCaloriesPerDay(height, weight,profileDTO.isSex(), age, goal, activity))
                .build()
                ;

        List<Image> imagesToAdd = new ArrayList<>();
        for (String m : imagesNames) {
            imagesToAdd.add(new Image(m, currentUser));
        }
        imageRepository.saveAll(imagesToAdd);
        currentUser.getImages().addAll(imagesToAdd);
        endUserRepository.save(currentUser);

        return Optional.of(profileRepository.save(profile));
    }

    @Override
    public Profile getProfileForUsername(String username) {

        return profileRepository.getLatestProfileForUsername(username);
    }

    @Override
    public Profile findById(Long id) {
        return profileRepository.findById(id).orElseThrow(ProfileDoesNotExist::new);
    }

    private Double calculateTotalCaloriesPerDay(Double height, Double weight,Boolean sex, Integer age, Goal goal, Activity activity) {
        Double BMR;
        if (sex) { // M
            BMR = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
        } else { // F
            BMR = 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
        }

        return BigDecimal.valueOf(BMR * activity.getFactor()).setScale(2, RoundingMode.CEILING).doubleValue();
    }

    private BMI calculateBMI(Double height, Double weight){
        BMI bmi = new BMI();
        Double bmiValue = BigDecimal.valueOf((weight / (height * height)) * 10000).setScale(2, RoundingMode.CEILING).doubleValue();
        bmi.setValue(bmiValue);

        if(bmiValue < 18.5) bmi.setRange("UNDERWEIGHT");
        else if (bmiValue >= 18.5 && bmiValue < 24.9) bmi.setRange("NORMAL");
        else if (bmiValue >= 24.9 && bmiValue < 29.9) bmi.setRange("OVERWEIGHT");
        else bmi.setRange("OBESE");

        return bmi;
    }
}
