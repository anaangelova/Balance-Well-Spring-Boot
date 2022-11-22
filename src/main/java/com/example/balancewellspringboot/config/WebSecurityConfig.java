package com.example.balancewellspringboot.config;

import com.example.balancewellspringboot.service.implementations.CustomOAuth2UserService;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

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
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/","/main.css","/main.js","/balancewell.js","/style.css", "/images/**", "/register","/oauth/**","/sendEmail","/uploads/**","/recipes_images/**","/api/**").permitAll()
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
                })
                .and()
                .headers().frameOptions().sameOrigin();
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customUsernamePasswordAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.httpFirewall(allowUrlEncodedPercentHttpFirewall());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOriginPatterns(Collections.singletonList("http://localhost:8080"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public HttpFirewall allowUrlEncodedPercentHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true);
        return firewall;
    }
}
