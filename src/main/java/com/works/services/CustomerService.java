package com.works.services;

import com.works.entities.Customer;
import com.works.repositories.CustomerRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class CustomerService {
    final CustomerRepository customerRepository;
    final PasswordEncoder passwordEncoder;
    final CommonService commonService;
    final HttpSession session;


    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, CommonService commonService, HttpSession session) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;

        this.commonService = commonService;
        this.session = session;
    }

    public ResponseEntity register(Customer customer) {
        Optional<Customer> optionalJWTUser = customerRepository.findByEmailEqualsIgnoreCase(customer.getEmail());
        Map<REnum, Object> hm = new LinkedHashMap();
        if (!optionalJWTUser.isPresent()) {
            String capitalizedName = commonService.capitalizedWords(customer.getFirstName());
            String capitalizedSecondName = commonService.capitalizedWords(customer.getSecondName());
            customer.setFirstName(capitalizedName);
            customer.setSecondName(capitalizedSecondName);
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customer.setEmail(customer.getEmail().toLowerCase(new java.util.Locale("en","US")));
            Customer customerNew = customerRepository.save(customer);
            hm.put(REnum.status, true);
            hm.put(REnum.result, customerNew);
            return new ResponseEntity(hm, HttpStatus.OK);
        } else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "This e-mail is already registered");
            hm.put(REnum.result, customer);
            return new ResponseEntity(hm, HttpStatus.NOT_ACCEPTABLE);
        }


    }

    public ResponseEntity changePassword(String oldPassword, String newPassword) {
        Map<REnum, Object> hm = new LinkedHashMap();
       Object object=session.getAttribute("customer");
       if(object!=null){
        Customer customer= (Customer) object;

        if (this.passwordEncoder.matches(oldPassword, customer.getPassword())) {
            customer.setPassword(passwordEncoder.encode(newPassword));
            Customer updatedCustomer = customerRepository.save(customer);
            hm.put(REnum.status, "true");
            hm.put(REnum.result, updatedCustomer);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } else {
            hm.put(REnum.message, "Please check again current password");
            hm.put(REnum.status, "false");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }else{
           hm.put(REnum.message, "Session customer is null");
           hm.put(REnum.status, "false");
           return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
       }
    }


    public ResponseEntity delete(Long id) {
        Map<REnum, Object> hm = new LinkedHashMap();
        try {
            customerRepository.deleteById(id);
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } catch (Exception exception) {
            hm.put(REnum.status, false);
            System.out.println(exception);
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity update( String firstName, String secondName, String email, String telephone) {
        Map<REnum, Object> hm = new LinkedHashMap();
        try {
            Customer oldCustomer= (Customer) session.getAttribute("customer");
            Optional<Customer> customer1 = customerRepository.findByEmailEqualsIgnoreCase(email);

                if ((oldCustomer.getEmail().equals(email)) || !customer1.isPresent()) {
                    System.out.println(oldCustomer.getEmail());
                    String capitalizedName = commonService.capitalizedWords(firstName);
                    String capitalizedSecondName = commonService.capitalizedWords(secondName);
                    oldCustomer.setFirstName(capitalizedName);
                    oldCustomer.setSecondName(capitalizedSecondName);
                    oldCustomer.setEmail(email);
                    oldCustomer.setTelephone(telephone);
                    customerRepository.saveAndFlush(oldCustomer);
                    hm.put(REnum.status, true);
                    hm.put(REnum.result, oldCustomer);
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


    public ResponseEntity changeEnableCustomer(Long id,boolean enable){
        Map<REnum, Object> hm = new LinkedHashMap();
        Optional<Customer> optionalCustomer=customerRepository.findById(id);
        if(optionalCustomer.isPresent()){
            Customer customer=optionalCustomer.get();
            customer.setEnabled(enable);
            customerRepository.save(customer);
            hm.put(REnum.status,true);
            hm.put(REnum.result,customer);
            return new ResponseEntity<>(hm,HttpStatus.OK);
        }
        else {
            hm.put(REnum.status, false);
            hm.put(REnum.message,"Invalid customer id");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity list() {
        Map<REnum, Object> hm = new HashMap<>();
        List<Customer> customerList = customerRepository.findAll();
        hm.put(REnum.status, true);
        hm.put(REnum.result, customerList);
        return new ResponseEntity<>(hm, HttpStatus.OK);

    }




}
