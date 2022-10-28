package com.example.balancewellspringboot.service.interfaces;

import com.example.balancewellspringboot.config.CustomOAuth2User;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.model.identity.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface EndUserService extends UserDetailsService {
    EndUser register(String username, String email, String password, String repeatPassword, String firstName, String lastName, Role role);

    void processOAuthPostLogin(CustomOAuth2User oauthUser);
}
