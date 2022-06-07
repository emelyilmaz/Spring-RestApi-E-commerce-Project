package com.works.configs;

import com.works.services.LoginUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final PasswordEncoder passwordEncoder;
    final LoginUserDetailService detailService;
    final JwtFilter jwtFilter;

    public SecurityConfig(PasswordEncoder passwordEncoder, LoginUserDetailService detailService, JwtFilter jwtFilter) {

        this.passwordEncoder = passwordEncoder;
        this.detailService = detailService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().formLogin().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

               http.authorizeRequests()
                .antMatchers("/customer/register","/login","/admin/register").permitAll()
                .antMatchers("/customer/setting").hasRole("customer")
                .antMatchers("/customer/changePassword","/customer/resetPassword","/customer/forgotPassword").hasRole("customer")
                .antMatchers("/customer/changeCustomerEnable","customer/list").hasRole("admin")
                .antMatchers("/category/add","category/delete","/category/update","/customer/delete").hasRole("admin")
                .antMatchers("/category/list","basket/add").hasAnyRole("admin","customer");


        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class );

       // http.csrf().disable().formLogin().disable();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
