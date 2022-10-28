package com.example.balancewellspringboot.model.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username) {
        super(String.format("User %s doesn not exist!", username));
    }
}

