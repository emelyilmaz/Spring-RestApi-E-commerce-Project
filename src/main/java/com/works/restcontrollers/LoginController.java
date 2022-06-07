package com.works.restcontrollers;

import com.works.entities.Customer;
import com.works.entities.Login;
import com.works.services.LoginService;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class LoginController {

final LoginService loginService;


    public LoginController(LoginService loginService ) {
        this.loginService = loginService;

    }

    @PostMapping("/login")
    public ResponseEntity auth(@RequestBody Login login) {
        return loginService.auth(login);
    }






}
