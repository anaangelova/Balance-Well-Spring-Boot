package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.config.CustomOAuth2User;
import com.example.balancewellspringboot.model.exceptions.PasswordValidationFailedException;
import com.example.balancewellspringboot.model.exceptions.PasswordsDoNotMatchException;
import com.example.balancewellspringboot.model.exceptions.UserExistsException;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.model.identity.Provider;
import com.example.balancewellspringboot.model.identity.Role;
import com.example.balancewellspringboot.repository.EndUserRepository;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EndUserServiceImplementation implements EndUserService {

    private final EndUserRepository endUserRepository;
    private final PasswordEncoder passwordEncoder;

    public EndUserServiceImplementation(EndUserRepository endUserRepository, PasswordEncoder passwordEncoder) {
        this.endUserRepository = endUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EndUser register(String username, String email, String password, String repeatPassword, String firstName, String lastName, Role role) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException();
        if (password.equals(repeatPassword)) {
            if(passwordValidation(password)){
                EndUser user = new EndUser(username,email, passwordEncoder.encode(password), firstName, lastName,role, Provider.LOCAL);
                if (endUserRepository.findByUsername(username).isPresent()) {
                    throw new UserExistsException(username);
                }

                return endUserRepository.save(user);
            }else throw new PasswordValidationFailedException();

        } else throw new PasswordsDoNotMatchException();
    }

    @Override
    public void processOAuthPostLogin(CustomOAuth2User oauthUser) {

        Optional<EndUser> existUser = endUserRepository.findByEmail(oauthUser.getEmail());
        String clientName=oauthUser.getOauth2ClientName().toLowerCase();
        if (existUser.isEmpty()) {
            EndUser newUser = new EndUser();
            newUser.setRole(Role.ROLE_USER);
            newUser.setUsername(oauthUser.getName());
            newUser.setEmail(oauthUser.getEmail());
            if(clientName.equals("google"))
                newUser.setProvider(Provider.GOOGLE);
            else newUser.setProvider(Provider.FACEBOOK);
            newUser.setEnabled(true);

            endUserRepository.save(newUser);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return endUserRepository.findByUsername(username).orElse(null);
    }

    private boolean passwordValidation(String password)
    {

        if(password.length()>=8)
        {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();

        }
        else return false;

    }
}
