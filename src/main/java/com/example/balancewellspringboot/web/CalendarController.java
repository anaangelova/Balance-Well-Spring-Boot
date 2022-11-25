package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.model.dto.CaloriesGoalDTO;
import com.example.balancewellspringboot.model.dto.LoggedDayDTO;
import com.example.balancewellspringboot.service.interfaces.LoggedDayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    private final LoggedDayService loggedDayService;

    public CalendarController(LoggedDayService loggedDayService) {
        this.loggedDayService = loggedDayService;
    }

    @GetMapping("/logDay")
    public String getLoggedDay(@RequestParam String date, HttpServletRequest httpServletRequest, Model model) {
        Instant time = Instant.parse(date);
        LocalDate localDate = time.atZone(ZoneId.systemDefault()).toLocalDate();
        LoggedDayDTO loggedDayDTO = loggedDayService.getLoggedDayDTO(httpServletRequest.getRemoteUser(), localDate);
        model.addAttribute("day", loggedDayDTO);
        model.addAttribute("totalCalories", loggedDayDTO.getTotalCalories());
        model.addAttribute("targetCalories", loggedDayDTO.getTargetCalories());
        model.addAttribute("localDate", localDate);
        CaloriesGoalDTO caloriesGoalDTO = loggedDayService.getInfoMessage(loggedDayDTO);
        model.addAttribute("infoMessage", caloriesGoalDTO.getMessage());
        model.addAttribute("status", caloriesGoalDTO.getStatus());
        return "log-day";
    }
}
