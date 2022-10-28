package com.example.balancewellspringboot.config;

import com.example.balancewellspringboot.service.implementations.CustomOAuth2UserService;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;
    private final CustomOAuth2UserService oauthUserService;
    private final EndUserService userService;

    public WebSecurityConfig(CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider, CustomOAuth2UserService oauthUserService, EndUserService userService) {
        this.customUsernamePasswordAuthenticationProvider = customUsernamePasswordAuthenticationProvider;
        this.oauthUserService = oauthUserService;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/main.css","/main.js","/style.css", "/images/**", "/home", "/register","/oauth/**","/sendEmail","/uploads/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .failureUrl("/login?error=BadCredentials")
                .defaultSuccessUrl("/home", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler((request, response, authentication) -> {
                    CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                    userService.processOAuthPostLogin(oauthUser);
                    response.sendRedirect("/home");
                });
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customUsernamePasswordAuthenticationProvider);
    }
}
