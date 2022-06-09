package com.works.services;

import com.works.entities.Basket;
import com.works.entities.Customer;
import com.works.entities.Orders;
import com.works.repositories.BasketRepository;
import com.works.repositories.CustomerRepository;
import com.works.repositories.OrdersRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class OrdersService {

    final OrdersRepository ordersRepository;
    final ProductService productService;
    final BasketRepository basketRepository;
    final CustomerRepository customerRepository;
    final HttpSession session;

    public OrdersService(OrdersRepository ordersRepository, ProductService productService, BasketRepository basketRepository, CustomerRepository customerRepository, HttpSession session) {
        this.ordersRepository = ordersRepository;
        this.productService = productService;
        this.basketRepository = basketRepository;
        this.customerRepository = customerRepository;
        this.session = session;
    }

    public ResponseEntity add(Orders orders) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        int sum=0;
        Customer customer= (Customer) session.getAttribute("customer");
        if(customer!=null) {
            System.out.println("customer mail"+customer.getEmail());
            List<Basket> baskets = basketRepository.findByCreatedByEqualsAndStatusFalse(customer.getEmail());

            if (baskets.size() > 0) {
                orders.setCustomer(customer);
                orders.setBaskets(baskets);
                for (Basket item : baskets) {
                    sum = sum + item.getProduct().getPrice() * item.getQuantity();
                    Optional<Basket> optionalBasket = basketRepository.findById(item.getId());
                    optionalBasket.get().setStatus(true);
                    basketRepository.saveAndFlush(optionalBasket.get());
                }
                orders.setTotal(sum);
                ordersRepository.save(orders);
                hm.put(REnum.status, true);
                hm.put(REnum.result, orders);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            } else {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "Basket is empty");
                return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
            }
        }else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "No such a customer ");
            return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
        }

    }
    public ResponseEntity delete(long id) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            ordersRepository.deleteById(id);
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put(REnum.error, ex.getMessage());
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity getOrderByOrder_Id(long id){
        Map<REnum, Object> hm = new LinkedHashMap<>();
      /*  List<Basket> baskets=  basketRepository.findByCustomer_Orders_IdIs(id);*/
        List<Orders> orders=ordersRepository.findByIdIs(id);
        hm.put(REnum.result,orders);
        return new ResponseEntity<>(hm,HttpStatus.OK);

    }

    public ResponseEntity list() {
        Map<REnum, Object> hm = new HashMap<>();
        List<Orders> categoryList = ordersRepository.findAll();
        hm.put(REnum.status, true);
        hm.put(REnum.result, categoryList);
        return new ResponseEntity<>(hm, HttpStatus.OK);


    }



   /* public  Integer generateOrderId() {
        Random random = new Random();
        int num = random.nextInt(100000000);
        String formatted = String.format("%05d", num);
        return num;
    }*/
}
