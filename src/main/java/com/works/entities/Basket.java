package com.works.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Basket extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Orders orders;


    @ManyToOne()
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;

    boolean status=false;

    private int quantity;

}
