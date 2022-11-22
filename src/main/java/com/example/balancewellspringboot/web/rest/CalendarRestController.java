package com.example.balancewellspringboot.web.rest;

import com.example.balancewellspringboot.service.interfaces.LoggedDayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/calendar")
public class CalendarRestController {

    private final LoggedDayService loggedDayService;

    public CalendarRestController(LoggedDayService loggedDayService) {
        this.loggedDayService = loggedDayService;
    }

    @GetMapping("/logDay")
    public String getLoggedDay(@RequestParam String date, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        Instant time = Instant.parse(date);
        LocalDate localDate = time.atZone(ZoneId.systemDefault()).toLocalDate();
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        return objectMapper.writeValueAsString(loggedDayService.getLoggedDayDTO(httpServletRequest.getRemoteUser(), localDate));
    }
}
