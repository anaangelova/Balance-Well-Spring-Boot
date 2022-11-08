package com.example.balancewellspringboot.model.exceptions;

public class ProfileDoesNotExist  extends RuntimeException {
    public ProfileDoesNotExist() {
        super("Profile does not exist");
    }
}
