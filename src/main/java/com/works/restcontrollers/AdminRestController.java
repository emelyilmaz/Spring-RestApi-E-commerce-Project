package com.works.restcontrollers;

import com.works.entities.Admin;
import com.works.services.AdminService;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/admin")
public class AdminRestController {
     final AdminService adminService;


    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody Admin admin){
         return adminService.register(admin);

    }
    @PutMapping("/changePassword")
    public ResponseEntity changePassword(@RequestParam String oldPassword, @RequestParam @NotBlank(message = "password can not be blank") String newPassword){
        return  adminService.changePassword(oldPassword,newPassword);
    }
    @PutMapping("/setting")
    public ResponseEntity update(@RequestParam  @Length(message = "companyName  must contain min 2 max  50 character.", min = 2, max = 50) String companyName, @RequestParam @Length(message = "adminName  must contain min 2 max  50 character.", min = 2, max = 50)  String adminName, @RequestParam  @Length(message = "adminSurname must contain min 2 max  5O character.", min = 10, max = 50) String adminSurname , @RequestParam @Email(message = "E-mail Format error") String email){
        return adminService.update(companyName,adminName,adminSurname,email);
    }
}
