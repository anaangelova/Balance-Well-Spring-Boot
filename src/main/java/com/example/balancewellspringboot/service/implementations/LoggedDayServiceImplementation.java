package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.model.LoggedDay;
import com.example.balancewellspringboot.model.Profile;
import com.example.balancewellspringboot.model.dto.LoggedDayDTO;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.repository.LoggedDayRepository;
import com.example.balancewellspringboot.repository.ProfileRepository;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import com.example.balancewellspringboot.service.interfaces.LoggedDayService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class LoggedDayServiceImplementation implements LoggedDayService {

    private final LoggedDayRepository loggedDayRepository;
    private final ProfileRepository profileRepository;
    private final EndUserService endUserService;

    public LoggedDayServiceImplementation(LoggedDayRepository loggedDayRepository, ProfileRepository profileRepository, EndUserService endUserService) {
        this.loggedDayRepository = loggedDayRepository;
        this.profileRepository = profileRepository;
        this.endUserService = endUserService;

    }

    @Override
    public LoggedDay getLoggedDay(String username, LocalDateTime date) {
        EndUser currentUser = (EndUser) endUserService.loadUserByUsername(username);
        Profile currentProfileForUser = profileRepository.getLatestProfileForUsername(username);

        return loggedDayRepository.findByEndUser_UsernameAndDateForDay(username,date)
                .orElseGet(() -> {
                   LoggedDay created = LoggedDay.builder()
                           .dateForDay(date)
                           .endUser(currentUser)
                           .targetCalories(currentProfileForUser.getTotalCaloriesPerDay())
                           .totalCalories(0L)
                           .allMealsForDay(new ArrayList<>())
                           .build()
                           ;
                  loggedDayRepository.save(created);
                  return created;
                });

    }

    @Override
    public LoggedDayDTO getLoggedDayDTO(String username, LocalDateTime date) {
        LoggedDay loggedDay = this.getLoggedDay(username, date);
        return LoggedDayDTO
                .builder()
                .dateForDay(loggedDay.getDateForDay())
                .targetCalories(loggedDay.getTargetCalories())
                .totalCalories(loggedDay.getTotalCalories())
                .build();
    }
}
