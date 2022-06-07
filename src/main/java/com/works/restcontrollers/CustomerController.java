package com.works.restcontrollers;

import com.works.entities.Customer;
import com.works.services.CustomerService;

import org.hibernate.validator.constraints.Length;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping("/customer")

@Validated

public class CustomerController {
    final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody Customer customer){
        return customerService.register(customer);

    }
    @PutMapping("/changePassword")
    public ResponseEntity changePassword(@RequestParam String oldPassword, @RequestParam @NotBlank(message = "password can not be blank") String newPassword){
      return  customerService.changePassword(oldPassword,newPassword);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        return customerService.forgotPassword(email);
    }
    @PutMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestParam String resettoken,@RequestParam String password){

        return customerService.resetPassword(resettoken,password);

    }
    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return customerService.delete(id);
    }

    @PutMapping("/setting")
    public ResponseEntity update( @RequestParam  @Length(message = "firstName  must contain min 2 max  50 character.", min = 2, max = 50) String firstName, @RequestParam @Length(message = "firstName  must contain min 2 max  50 character.", min = 2, max = 50)  String secondName, @RequestParam @Email(message = "E-mail Format error") String email, @RequestParam  @Length(message = "telephone must contain min 10 max  5O character.", min = 10, max = 50) String telephone ){
        return customerService.update(firstName, secondName,email,telephone);
    }

    @PutMapping("/changeCustomerEnable")
    public ResponseEntity changeEnable(@RequestParam Long id,@RequestParam boolean enable){
        return customerService.changeEnableCustomer(id,enable);
    }
    @GetMapping("/list")
    public ResponseEntity list(){

        return customerService.list();
    }

}
