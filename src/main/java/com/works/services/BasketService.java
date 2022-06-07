package com.works.services;

import com.works.entities.Basket;
import com.works.repositories.BasketRepository;
import com.works.repositories.ProductRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
@Service
public class BasketService {


final BasketRepository basketRepo;


    public BasketService(BasketRepository basketRepository) {
        this.basketRepo = basketRepository;

    }

    public ResponseEntity add(Basket basket) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
            basketRepo.save(basket);
            hm.put(REnum.status, true);
            hm.put(REnum.result, basket);
            return new ResponseEntity<>(hm, HttpStatus.OK);

    }



 /*   public  String generateOrderId() {
        Random random = new Random();
        int num = random.nextInt(100000000);
        String formatted = String.format("%05d", num);
       return formatted;
    }*/
}
