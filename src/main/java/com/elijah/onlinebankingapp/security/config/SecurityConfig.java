/*
package com.elijah.onlinebankingapp.security.config;

import com.elijah.onlinebankingapp.security.filter.CustomAuthenticationFilter;
import com.elijah.onlinebankingapp.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("api/login/**","/token/refresh").permitAll();
        http.authorizeRequests().antMatchers(GET,"/api/users").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST,"/account/create").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/account/info/through/email").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/account/info/through/phoneNumber").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/account/info/through/accountNumber").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/card/type/create").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/card/getAll").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/card/type/find").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/debitCard/create").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/debitCard/info").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/customer/register").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/download/{imageId}").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST,"/customer/getByEmail").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST,"/customer/getByPhone").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST,"/customer/signIn").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT,"/customer/info/update").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/customers/all").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST,"/transaction/deposit").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/transaction/withdraw/at/counter").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/transaction/withdraw/with/card").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/transaction/list").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/transaction/transfer").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/account/statement").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/account/statement/from/enteredDate").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/account/from/date").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}

*/