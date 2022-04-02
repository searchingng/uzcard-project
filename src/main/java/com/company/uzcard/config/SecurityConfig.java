package com.company.uzcard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").roles("ADMIN_ROLE")
                .and()
                .withUser("user").password("{noop}user").roles("BANK_ROLE")
                .and()
                .withUser("123").password("{noop}123").roles("PAYMENT_ROLE");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/card/**").hasRole("BANK_ROLE")
                .antMatchers("/profile/**").hasRole("BANK_ROLE")
                .antMatchers(HttpMethod.POST, "/transaction").hasAnyRole("BANK_ROLE", "PAYMENT_ROLE")
                .antMatchers("/transaction/card/**").hasAnyRole("BANK_ROLE", "PAYMENT_ROLE")
                .antMatchers("/transaction/profile/**").hasAnyRole("BANK_ROLE")
                .antMatchers("/transaction/filter").hasAnyRole("ADMIN_ROLE")
//                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

    }
}
