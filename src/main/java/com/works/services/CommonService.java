package com.works.services;

import com.works.entities.Admin;
import com.works.entities.Customer;
import com.works.repositories.AdminRepository;
import com.works.repositories.CustomerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class CommonService {
    final CustomerRepository customerRepository;
    final AdminRepository adminRepository;

    public CommonService(CustomerRepository customerRepository, AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
    }

    public  String capitalizedWords(String sentence) {
        String[] words = sentence.split(" ");
        StringBuilder sbCapitalizedWords = new StringBuilder(sentence.length());

        for (String word : words) {

            if (word.length() > 1)
                sbCapitalizedWords
                        .append(word.substring(0, 1).toUpperCase(Locale.ROOT))
                        .append(word.substring(1).toLowerCase(Locale.ROOT));
            else
                sbCapitalizedWords.append(word.toUpperCase());

            sbCapitalizedWords.append(" ");
        }
        return sbCapitalizedWords.toString().trim();
    }

    public String getAuthenticatedMail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<Customer> optionalUser = customerRepository.findByEmailEqualsIgnoreCase(userName);
        Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(userName);
        if ( optionalUser.isPresent() ) {
            return optionalUser.get().getEmail();
        }
        if ( optionalAdmin.isPresent() ) {
            return optionalAdmin.get().getEmail();
        }
        return null;
    }
}
