package com.api.counter.application.sercurity;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.inMemoryAuthentication()
      .withUser("user").password("password").roles("USER").and()
      .withUser("optus").password("candidates").roles("USER", "ADMIN");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
      .httpBasic().and()
      .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/counter-api/**").hasRole("ADMIN")
        .antMatchers(HttpMethod.POST, "/counter-api/**").hasRole("ADMIN")
        .and()
      .csrf().disable();
  }
}