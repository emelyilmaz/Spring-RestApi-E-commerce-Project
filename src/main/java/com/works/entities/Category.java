package com.works.entities;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Category extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotBlank(message = "Category name name can not be blank")
    @Length(message = "Category name must contain min 2 max  50 character.", min = 2, max = 50)
    private String categoryName;


    /*@OneToMany(mappedBy = "category",fetch = FetchType.LAZY,cascade = CascadeType.)
    @Ignore
    private  List<Product> productList;*/


}
