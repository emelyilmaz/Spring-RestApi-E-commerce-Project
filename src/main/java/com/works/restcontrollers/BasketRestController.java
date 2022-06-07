package com.works.restcontrollers;

import com.works.entities.Basket;
import com.works.entities.Category;
import com.works.services.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("basket")
public class BasketRestController {

    final BasketService basketService;

    public BasketRestController(BasketService basketService) {
        this.basketService = basketService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody Basket basket){
        return basketService.add(basket);
    }


}
