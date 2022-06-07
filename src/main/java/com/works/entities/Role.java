package com.works.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;


    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    List<Admin> admins;


   // @OneToMany (mappedBy = "role",fetch = FetchType.LAZY, cascade={CascadeType.ALL})
    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    List<Customer> customers;


}
