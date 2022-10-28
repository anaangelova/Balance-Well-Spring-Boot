package com.example.balancewellspringboot.model.dto;


import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserRegistrationDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @Email
    private String email;
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String repeatPassword;

    public UserRegistrationDto(){

    }

    public UserRegistrationDto(String name, String surname, String email, String username, String password, String repeatPassword) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }
}
