package com.works.configs;

import com.works.services.LoginUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

        http
                .authorizeRequests()
                .antMatchers(getPermitAll()).permitAll()
                .antMatchers(getCustomerRole()).hasRole("customer")
                .antMatchers(getAdminRole()).hasRole("admin")
                .antMatchers(getCustomerAdmin_Role()).hasAnyRole("admin","customer")
                .and()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class );


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

    private String[] getCustomerRole(){
        String[] customerRole={"/customer/changePassword","/customer/setting",
                "/basket/add","/basket/delete","/basket/update",
                "/order/add","/order/delete"};
        return customerRole;
    }
    private String[] getAdminRole(){
        String[] adminRole={"/category/add","/category/delete","/category/update",
                "/customer/delete","/customer/list","/customer/changeCustomerEnable",
                "/product/add","/product/delete","/product/update",
                "/order/list","/order/getDetail","/admin/changePassword","/admin/setting","/admin/register"};
        return adminRole;
    }

    private String[] getCustomerAdmin_Role(){
        String[] bothRole={
                "/category/list","/product/list","/product/listbyCategory",
                "/basket/customer",
                "/product/search"};
        return bothRole;

    }
    private String[] getPermitAll(){
        String[] permitAll={"/customer/register","/login",
                "/forgotPassword","/resetPassword"};
        return permitAll;
    }
}
