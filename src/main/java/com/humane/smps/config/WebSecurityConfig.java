package com.humane.smps.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/", "/login", "/bower_components/**", "/dist/**", "/image/univLogo").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/main", true)
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                .and().httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("humane12!").roles("ADMIN")
                .and()
                .withUser("api").password("humane12!").roles("ADMIN")
                .and()
                .withUser("hanyang").password("hanyang").roles("USER")
                .and()
                .withUser("skku").password("skku").roles("USER")
                .and()
                .withUser("erica").password("erica").roles("USER")
                .and()
                .withUser("humane").password("humane").roles("USER");
    }
}