package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.model.dto.LoggedDayDTO;
import com.example.balancewellspringboot.service.interfaces.LoggedDayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
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
        LocalDateTime localDate = time.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LoggedDayDTO loggedDayDTO = loggedDayService.getLoggedDayDTO(httpServletRequest.getRemoteUser(), localDate);
        model.addAttribute("day", loggedDayDTO);
        return "log-day";
    }
}
