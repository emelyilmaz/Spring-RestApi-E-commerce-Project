package com.works.services;

import com.works.configs.JwtUtil;
import com.works.entities.Admin;
import com.works.entities.Customer;
import com.works.entities.Login;
import com.works.repositories.AdminRepository;
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

import javax.servlet.http.HttpSession;
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
final JavaMailSender emailSender;
final AdminRepository adminRepository;
    final HttpSession session;



    public LoginService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, LoginUserDetailService loginUserDetailService, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JavaMailSender emailSender, AdminRepository adminRepository, HttpSession session) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.loginUserDetailService = loginUserDetailService;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
        this.adminRepository = adminRepository;
        this.session = session;
    }

    public ResponseEntity auth(Login login) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {

            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                    login.getUsername(), login.getPassword()
            ) );

            UserDetails userDetails = loginUserDetailService.loadUserByUsername(login.getUsername());
            Admin admin = (Admin) session.getAttribute("admin");
            Customer customer = (Customer) session.getAttribute("customer");

            String jwt = jwtUtil.generateToken(userDetails);
            hm.put(REnum.status, true);
            if(admin !=null){
            hm.put(REnum.result,admin);
            }
            else {
                hm.put(REnum.result,customer);
            }

            hm.put( REnum.jwt, jwt );

            return new ResponseEntity(hm, HttpStatus.OK);
        }catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put( REnum.error, ex.getMessage() );
            hm.put(REnum.message,"Your email address or password is wrong");
            return new ResponseEntity(hm, HttpStatus.BAD_REQUEST);
        }

    }
    public ResponseEntity forgotPassword(String email) {
        Map<REnum, Object> hm = new LinkedHashMap();
        Optional<Customer> optionalCustomer = customerRepository.findByEmailEqualsIgnoreCase(email);
        Optional<Admin> optionalAdmin = adminRepository.findByEmailEqualsIgnoreCase(email);

        if (optionalCustomer.isPresent()||optionalAdmin.isPresent()) {
            UUID uuid = UUID.randomUUID();
            String verifyCode = uuid.toString();
            String resetPasswordLink = "http://localhost:8092/resetPassword?resettoken=" + verifyCode;

            try {
            if (optionalCustomer.isPresent()) {
                Customer customer = optionalCustomer.get();
                customer.setVerificationCode(uuid.toString());
                customerRepository.save(customer);
                sendSimpleMessage(customer.getEmail(), "Password Reset Link", resetPasswordLink);
            }else{
                Admin admin=optionalAdmin.get();
                admin.setVerificationCode(verifyCode);
                adminRepository.save(admin);
                sendSimpleMessage(admin.getEmail(), "Password Reset Link", resetPasswordLink);
            }
                hm.put(REnum.status, "true");
                hm.put(REnum.message,"Password Reset Link sent your email address.Please check email");
                hm.put(REnum.result, resetPasswordLink);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            } catch (Exception exception) {

                hm.put(REnum.status, false);
                hm.put(REnum.error, exception);
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
        } else {
            hm.put(REnum.status, "false");
            hm.put(REnum.message, "There is not such a e-mail address");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity resetPassword(String verificationCode,  String password) {
        Map<REnum, Object> hm = new LinkedHashMap();
        Optional<Customer> optionalCustomer = customerRepository.findByVerificationCodeEquals(verificationCode);
        Optional<Admin> optionalAdmin = adminRepository.findByVerificationCodeEquals(verificationCode);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setPassword(passwordEncoder.encode(password));
            customer.setVerificationCode(null);
            customerRepository.save(customer);
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        }
         else if(optionalAdmin.isPresent()){
             Admin admin =optionalAdmin.get();
             admin.setPassword(passwordEncoder.encode(password));
             admin.setVerificationCode(null);
             hm.put(REnum.status, true);
             return new ResponseEntity<>(hm, HttpStatus.OK);

         }  else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "Invalid verification code");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("javalover138@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }


}
