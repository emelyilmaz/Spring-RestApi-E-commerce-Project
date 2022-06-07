package com.works.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "firstName  can not be blank")
    @Length(message = "firstName  must contain min 2 max  5o character.", min = 2, max = 50)
    private String firstName;


    @NotBlank(message = "secondName  can not be blank")
    @Length(message = "secondName  must contain min 2 max  5O character.", min = 2, max = 50)
    private String secondName;

    @NotBlank(message = "telephone can not be blank")
    @Length(message = "telephone must contain min 10 max  5O character.", min = 2, max = 50)
    private String telephone;

    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email Format Error")
    @Length(message = "Maximum 60", max = 60)
    private String email;

   // @Length(message = "Maximum 10 min 5",min = 4, max = 10)
    @NotBlank(message = "password can not be blank")
    //@Pattern(message = "Password must contain min one upper,lower letter and 0-9 digit number ", regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$)")
    private String password;

    private boolean enabled;
    private boolean tokenExpired;
    private String verificationCode;

    @JsonIgnore
    @OneToMany(mappedBy = "customer",cascade={CascadeType.PERSIST, CascadeType.DETACH})
    private List<Orders> orders;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "customer_role",
            joinColumns = @JoinColumn( name = "customer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;
}
