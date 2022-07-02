package com.works.services;

import com.works.entities.Basket;
import com.works.entities.Customer;
import com.works.entities.Orders;
import com.works.entities.Product;
import com.works.repositories.BasketRepository;
import com.works.repositories.CustomerRepository;
import com.works.repositories.OrdersRepository;
import com.works.repositories.ProductRepository;
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
    final ProductRepository productRepository;

    public OrdersService(OrdersRepository ordersRepository, ProductService productService, BasketRepository basketRepository, CustomerRepository customerRepository, HttpSession session, ProductRepository productRepository) {
        this.ordersRepository = ordersRepository;
        this.productService = productService;
        this.basketRepository = basketRepository;
        this.customerRepository = customerRepository;
        this.session = session;
        this.productRepository = productRepository;
    }

    public ResponseEntity add(Basket[]  baskets) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        Orders orders=new Orders();
        int sum=0;
        Customer customer= (Customer) session.getAttribute("customer");
        try {
            if (customer != null) {
                //List<Basket> baskets = basketRepository.findByCreatedByEqualsAndStatusFalse(customer.getEmail() );
                System.out.println("1"+baskets[0]);
                if (baskets.length > 0) {
                    System.out.println("2"+baskets[0]);
                  List<Basket> basketList=  Arrays.asList(baskets);
                    System.out.println("3"+baskets[0]);
                    orders.setCustomer(customer);
                    orders.setBaskets(basketList);
                    System.out.println("4"+baskets[0]);

                    for (Basket item : baskets) {
                        System.out.println("item"+item.getQuantity());
                        sum = sum + item.getProduct().getPrice() * item.getQuantity();
                        item.setStatus(true);
                        basketRepository.saveAndFlush(item);
                        System.out.println("1");
                    }
                    orders.setTotal(sum);
                    ordersRepository.save(orders);
                    System.out.println("2");
                    hm.put(REnum.status, true);
                    hm.put(REnum.result, orders);
                    return new ResponseEntity<>(hm, HttpStatus.OK);
                } else {
                    hm.put(REnum.status, false);
                    hm.put(REnum.message, "Basket is empty");
                    return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
                }
            } else {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "No such a customer ");
                return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
            }

        }catch (Exception ex){
            hm.put(REnum.status, false);
            System.out.println(ex);
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }

    }



    public ResponseEntity delete(long id) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
           Optional<Orders> optionalOrders=ordersRepository.findById(id);
           if(optionalOrders.isPresent()){
               Orders orders=optionalOrders.get();
               ordersRepository.deleteById(id);
               List<Basket> baskets=orders.getBaskets();
               for(Basket basket:baskets){
                   Integer quantity=basket.getQuantity();
                   Product product=basket.getProduct();
                   product.setStockQuantity(product.getStockQuantity()+quantity);
                   productRepository.saveAndFlush(product);
               }
                hm.put(REnum.status, true);
               hm.put(REnum.message, "Order cancellation successful.");
                return new ResponseEntity<>(hm, HttpStatus.OK);
           }else{
               hm.put(REnum.status, false);
               hm.put(REnum.message, "There is not such order id");
               return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
           }
        } catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put(REnum.error, ex.getMessage());
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity getOrderByOrder_Id(long id){
        Map<REnum, Object> hm = new LinkedHashMap<>();
        List<Orders> orders=ordersRepository.findByIdIs(id);
        hm.put(REnum.result,orders);
        return new ResponseEntity<>(hm,HttpStatus.OK);

    }

    public ResponseEntity list() {
        Map<REnum, Object> hm = new HashMap<>();
        List<Orders> ordersList = ordersRepository.findAll();
        hm.put(REnum.status, true);
        hm.put(REnum.result, ordersList);
        return new ResponseEntity<>(hm, HttpStatus.OK);


    }

    public ResponseEntity getOrderByCustomer() {
        Map<REnum, Object> hm = new HashMap<>();
        try {
            Customer customer= (Customer) session.getAttribute("customer");
            List<Orders> orders=ordersRepository.findByCustomer_Id(customer.getId());
            hm.put(REnum.status, true);
            hm.put(REnum.result, orders);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        }catch (Exception ex){
            hm.put(REnum.status, false);
            hm.put(REnum.error, ex.getMessage());
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }


    }

}
