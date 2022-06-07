package com.works.services;

import com.works.configs.JwtUtil;
import com.works.entities.Customer;
import com.works.entities.Login;
import com.works.repositories.CustomerRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {
final AuthenticationManager authenticationManager;
final JwtUtil jwtUtil;
final LoginUserDetailService loginUserDetailService;
final CustomerRepository customerRepository;
final PasswordEncoder passwordEncoder;



    public LoginService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, LoginUserDetailService loginUserDetailService, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.loginUserDetailService = loginUserDetailService;

        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public ResponseEntity auth(Login login) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            System.out.println(login.getUsername());
            System.out.println(login.getPassword());
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                    login.getUsername(), login.getPassword()
            ) );

            UserDetails userDetails = loginUserDetailService.loadUserByUsername(login.getUsername());
            System.out.println("Role"+userDetails.getAuthorities());

            String jwt = jwtUtil.generateToken(userDetails);
            hm.put(REnum.status, true);
            hm.put( REnum.jwt, jwt );
            return new ResponseEntity(hm, HttpStatus.OK);
        }catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put( REnum.error, ex.getMessage() );
            return new ResponseEntity(hm, HttpStatus.NOT_ACCEPTABLE);
        }

    }


}
