package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.model.dto.UserRegistrationDto;
import com.example.balancewellspringboot.model.identity.Role;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final EndUserService userService;

    public RegisterController(EndUserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        return "register";

    }

    @PostMapping
    public String register(@Valid UserRegistrationDto dto) {

        userService.register(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getRepeatPassword(), dto.getName(), dto.getSurname(), Role.ROLE_USER);
        return "redirect:/login";

    }
}
