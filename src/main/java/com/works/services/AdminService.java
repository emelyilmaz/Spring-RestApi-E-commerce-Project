package com.works.services;

import com.works.entities.Admin;
import com.works.repositories.AdminRepository;
import com.works.repositories.RoleRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class AdminService {
  final AdminRepository adminRepository;
  final RoleRepository roleRepository;
  final PasswordEncoder passwordEncoder;
  final CommonService commonService;
  final HttpSession session;

    public AdminService(AdminRepository adminRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CommonService commonService, HttpSession session) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.commonService = commonService;
        this.session = session;
    }

    public ResponseEntity register(Admin admin){
        HashMap<REnum,Object> hm=new LinkedHashMap<>();
        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(admin.getEmail());
        if(optionalAdmin.isPresent()){
            hm.put(REnum.status, false);
            hm.put(REnum.message,"This admin have already registered");
            return new ResponseEntity( hm, HttpStatus.NOT_ACCEPTABLE );
        }else{
            String capitalizedName =commonService.capitalizedWords(admin.getAdminName());
            String capitalizedSecondName =commonService.capitalizedWords(admin.getAdminSurname());
            String capitalizedCompanyName=commonService.capitalizedWords(admin.getCompanyName());
            admin.setAdminName(capitalizedName);
            admin.setAdminSurname(capitalizedSecondName);
            admin.setCompanyName(capitalizedCompanyName);
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            Admin adminNew=adminRepository.save(admin);
            hm.put(REnum.status,true);
            hm.put(REnum.result,adminNew);
            return new ResponseEntity<>(hm,HttpStatus.OK);

        }

    }

    public ResponseEntity changePassword(String oldPassword, String newPassword) {
        Map<REnum, Object> hm = new LinkedHashMap();

        Admin admin= (Admin) session.getAttribute("admin");

        if (this.passwordEncoder.matches(oldPassword, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            Admin updatedAdmin = adminRepository.save(admin);
            hm.put(REnum.status, "true");
            hm.put(REnum.result, updatedAdmin);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } else {
            hm.put(REnum.message, "Please check again current password");
            hm.put(REnum.status, "false");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity update( String companyName, String adminName,String adminSurname, String email ) {

        Map<REnum, Object> hm = new LinkedHashMap();
        try {

            Admin oldAdmin= (Admin) session.getAttribute("admin");
            Optional<Admin> admin_ByEmail = adminRepository.findByEmailEqualsIgnoreCase(email);

            if ((oldAdmin.getEmail().equals(email)) || !admin_ByEmail.isPresent()) {

                    String capitalizedCompanyName = commonService.capitalizedWords(companyName);
                    String capitalizedAdminName = commonService.capitalizedWords(adminName);
                    String capitalizedSurName = commonService.capitalizedWords(adminSurname);
                    oldAdmin.setCompanyName(capitalizedCompanyName);
                    oldAdmin.setAdminName(capitalizedAdminName);
                    oldAdmin.setAdminSurname(capitalizedSurName);
                    oldAdmin.setEmail(email);

                    adminRepository.saveAndFlush(oldAdmin);
                    hm.put(REnum.status, true);
                    hm.put(REnum.result, oldAdmin);
                    return new ResponseEntity<>(hm, HttpStatus.OK);
                } else {
                    hm.put(REnum.status, false);
                    hm.put(REnum.message, "This email already registered");
                    return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
                }


        } catch (Exception exception) {
            hm.put(REnum.status, false);
            hm.put(REnum.message,exception);
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }

    }

}
