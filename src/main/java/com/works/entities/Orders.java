package com.works.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
public class Orders extends Base{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 /*   @Temporal(TemporalType.DATE)
    private Date deliveredDate;*/


    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;


    @OneToMany(cascade ={CascadeType.MERGE})
    @JoinTable(name="Order_Basket",joinColumns = @JoinColumn(name="order_id" ),
            inverseJoinColumns = @JoinColumn(name="basket_id",referencedColumnName ="id")
    )

    private List<Basket> baskets;

    int total;









}
