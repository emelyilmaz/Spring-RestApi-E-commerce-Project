package com.works.restcontrollers;

import com.works.configs.Anotaions.CurrentUser;
import com.works.entities.Category;
import com.works.entities.Login;
import com.works.entities.Product;
import com.works.services.ProductService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductRestController {
    final ProductService productService;

    public ProductRestController(ProductService productService) {

        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody Product product){
        return productService.add(product);

    }
    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){

        return productService.productDelete(id);
    }



    @GetMapping("/list")

    public ResponseEntity list(){

        return productService.list();
    }

    @GetMapping("/list/company")
    public ResponseEntity listByCompany(){

        return productService.listByCompany();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Product product){

        return productService.update(product);
    }
    @GetMapping("/search")
    public ResponseEntity search(@RequestParam String q){

        return productService.findProductBy_Search(q);
    }
    @GetMapping("/listbyCategory")
    public ResponseEntity listBy_Cat(@RequestParam Integer id){

        return productService.findProductBy_Category(id);
    }



}
