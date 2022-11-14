package com.example.balancewellspringboot.model.identity;

import com.example.balancewellspringboot.model.Image;
import com.example.balancewellspringboot.model.LoggedDay;
import com.example.balancewellspringboot.model.Profile;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name = "end_users")
public class EndUser implements UserDetails {

    @Id
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    //oAuth2
    @Enumerated(EnumType.STRING)
    private Provider provider;

    //security
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired =  true;
    private boolean isEnabled = true;

    @OneToMany(mappedBy = "endUser",fetch = FetchType.EAGER)
    private List<Profile> profilesForUser;

    @ToString.Exclude
    @OneToMany(mappedBy = "endUserOwner")
    private List<Image> images;

    @OneToMany(mappedBy = "endUser")
    private List<LoggedDay> loggedDayList;

    public EndUser(){}


    public EndUser(String username, String email, String password, String firstName, String lastName, Role role, Provider provider, List<LoggedDay> loggedDayList) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.provider = provider;
        this.loggedDayList = loggedDayList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
