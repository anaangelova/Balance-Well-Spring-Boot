package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.model.enums.Activity;
import com.example.balancewellspringboot.model.enums.Goal;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final EndUserService endUserService;

    public HomeController(EndUserService endUserService) {
        this.endUserService = endUserService;
    }

    @GetMapping
    public String getHomePage(Model model, HttpServletRequest request){
        EndUser endUser=(EndUser) endUserService.loadUserByUsername(request.getRemoteUser());
        if(!endUser.getProfilesForUser().isEmpty()) {
            return "home";
        } else {
            model.addAttribute("goals", getGoals());
            model.addAttribute("activities", getActivities());
            return "questionnaire";
        }
    }

    private List<String> getGoals() {
        return Arrays.stream(Goal.values()).map(Goal::toString).collect(Collectors.toList());
    }

    private List<String> getActivities() {
        return Arrays.stream(Activity.values()).map(Activity::toString).collect(Collectors.toList());
    }

}
