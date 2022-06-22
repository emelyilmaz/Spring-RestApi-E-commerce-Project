package com.works.restcontrollers;

import com.works.entities.Login;
import com.works.services.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@RestController
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class LoginRestController {

final LoginService loginService;


    public LoginRestController(LoginService loginService ) {
        this.loginService = loginService;

    }

    @PostMapping("/login")
    public ResponseEntity auth(@RequestBody Login login) {
        return loginService.auth(login);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam @Email(message = "E-mail Format Error") String email) {
        return loginService.forgotPassword(email);
    }
    @PutMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestParam String resettoken,@RequestParam @Pattern(message = "Password must contain min one upper,lower letter and 0-9 digit number ",
            regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})") String password){

        return loginService.resetPassword(resettoken,password);

    }




}
