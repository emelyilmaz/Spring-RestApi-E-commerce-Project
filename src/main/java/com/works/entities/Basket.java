package com.works.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data

public class Basket extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_id")
    Product product;


    boolean status=false;

    @NotNull
    @Range(message ="You must add a minimum of 1 pc" ,min = 1)
    private int quantity;

    @Override
    public String toString() {
        return "Basket{" +
                "id=" + id +
                ", product=" + product +
                ", status=" + status +
                ", quantity=" + quantity +
                '}';
    }
}
