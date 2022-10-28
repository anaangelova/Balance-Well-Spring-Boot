package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.model.exceptions.InvalidUserCredentialsException;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.repository.EndUserRepository;
import com.example.balancewellspringboot.service.interfaces.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementation implements AuthService {

    private final EndUserRepository userRepository;

    public AuthServiceImplementation(EndUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public EndUser login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException();
        return userRepository.findByUsernameAndPassword(username, password).orElseThrow(InvalidUserCredentialsException::new);

    }
}
