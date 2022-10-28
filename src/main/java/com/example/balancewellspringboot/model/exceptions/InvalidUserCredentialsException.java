package com.example.balancewellspringboot.model.exceptions;

public class InvalidUserCredentialsException extends RuntimeException{

    public InvalidUserCredentialsException() {
        super("Invalid user credentials");
    }
}
