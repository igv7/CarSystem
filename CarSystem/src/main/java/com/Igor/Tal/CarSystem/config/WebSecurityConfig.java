package com.Igor.Tal.CarSystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("**/admin/**").authenticated()
                .antMatchers("**/client/**").authenticated()
                .and()
                .logout().permitAll().logoutSuccessUrl("/login")
        		.and()
        		.formLogin().usernameParameter("userName")
                .loginPage("/login");
        		http.csrf().disable();
    }

}