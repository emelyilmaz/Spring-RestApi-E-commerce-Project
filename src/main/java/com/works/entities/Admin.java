package com.works.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
public class Admin extends Base{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @NotBlank(message = "Company name can not be blank")
    @Length(message = "Company name must contain min 2 max  5o character.", min = 2, max = 50)
    private String companyName;

    @NotBlank(message = "Admin name can not be blank")
    @Length(message = "Admin name must contain min 2 max  5O character.", min = 2, max = 50)
    private String adminName;

    @NotBlank(message = "admin surname can not be blank")
    @Length(message = "Admin surname must contain min 2 max  5O character.", min = 2, max = 50)
    private String adminSurname;

    @Length(message = "Maximum 60", max = 60)
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Email Format Error")
    private String email;

   // @Length(message = "Maximum 10 min 3",min = 5, max = 10)
    @NotBlank(message = "password can not be blank")
    //@Pattern(message = "Password must contain min one upper,lower letter and 0-9 digit number ", regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$)")
    private String password;

    private boolean enabled;
    private boolean tokenExpired;


@ManyToMany(fetch = FetchType.EAGER)
@JoinTable( name = "admin_role",
        joinColumns = @JoinColumn( name = "admin_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "id")
)
private List<Role> roles;



}
