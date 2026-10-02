package com.Igor.CarSystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security configuration. Disables CSRF and sets up a form login page.
 * The real access control is the token check in each controller, not these matchers.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * Requires authentication for admin/client paths, configures /login as the login page
     * (with a {@code userName} parameter) and permits logout.
     */
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