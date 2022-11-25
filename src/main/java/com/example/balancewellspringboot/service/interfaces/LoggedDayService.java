package com.example.balancewellspringboot.service.interfaces;

import com.example.balancewellspringboot.model.LoggedDay;
import com.example.balancewellspringboot.model.dto.CaloriesGoalDTO;
import com.example.balancewellspringboot.model.dto.LoggedDayDTO;

import java.time.LocalDate;


public interface LoggedDayService {
    LoggedDay getLoggedDay(String username, LocalDate date);
    LoggedDayDTO getLoggedDayDTO(String username, LocalDate date);
    LoggedDay saveLoggedDay(LoggedDay loggedDay);
    CaloriesGoalDTO getInfoMessage(LoggedDayDTO loggedDayDTO);
}
