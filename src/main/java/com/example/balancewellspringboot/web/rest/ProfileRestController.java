package com.example.balancewellspringboot.web.rest;

import com.example.balancewellspringboot.model.dto.*;
import com.example.balancewellspringboot.service.interfaces.ProfileService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/users")
public class ProfileRestController {

    private final ProfileService profileService;

    public ProfileRestController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/get-progress")
    public List<LineChartDTO> getLineChartDataForUser(HttpServletRequest httpServletRequest) {

       return profileService.getLineChartDataForUser(httpServletRequest.getRemoteUser());

    }
}
