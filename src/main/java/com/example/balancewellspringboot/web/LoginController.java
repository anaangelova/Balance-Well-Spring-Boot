package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.model.exceptions.InvalidUserCredentialsException;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.service.interfaces.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage(){
        return "login";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model){
        EndUser user;
        try{
            user = authService.login(request.getParameter("username"),request.getParameter("password"));
            request.getSession().setAttribute("user",user);
            return "redirect:/home";
        }catch (InvalidUserCredentialsException exception){
            model.addAttribute("hasError",true);
            model.addAttribute("error",exception.getMessage());
            return "login";
        }
    }
}
