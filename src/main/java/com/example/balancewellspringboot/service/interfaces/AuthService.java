package com.example.balancewellspringboot.service.interfaces;

import com.example.balancewellspringboot.model.identity.EndUser;

public interface AuthService {

    EndUser login(String username, String password);
}
