package com.example.balancewellspringboot.service.interfaces;

import com.example.balancewellspringboot.model.LoggedDay;
import com.example.balancewellspringboot.model.dto.LoggedDayDTO;

import java.time.LocalDateTime;


public interface LoggedDayService {
    LoggedDay getLoggedDay(String username, LocalDateTime date);
    LoggedDayDTO getLoggedDayDTO(String username, LocalDateTime date);
}
