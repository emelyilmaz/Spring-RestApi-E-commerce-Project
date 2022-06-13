package com.works.services;

import com.works.entities.*;
import com.works.repositories.CategoryRepository;
import com.works.repositories.ProductRepository;
import com.works.utils.REnum;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;


@Service
public class ProductService {
    final ProductRepository proRepo;
    final CategoryRepository catRepo;
    final CommonService commonService;
    final CacheManager cacheManager;
    final HttpSession session;


    public ProductService(ProductRepository proRepo, CategoryRepository catRepo, CommonService commonService, CacheManager cacheManager, HttpSession session) {
        this.proRepo = proRepo;
        this.catRepo = catRepo;
        this.commonService=commonService;
        this.cacheManager = cacheManager;

        this.session = session;
    }

    public ResponseEntity add(Product product) {
        Map<REnum, Object> hm = new LinkedHashMap<>();

        Optional<Category> optionalCategory=catRepo.findById(product.getCategory().getId());

        if(!optionalCategory.isPresent()){
            hm.put(REnum.status, false);
            hm.put(REnum.message, " There is not such a this Category Id");
            return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
        }else
            {
            String capitalizedName= commonService.capitalizedWords(product.getName());
            product.setName(capitalizedName);
            product.setCategory(optionalCategory.get());
            Product product1 = proRepo.save(product);
            cacheManager.getCache("listCacheProduct").clear();
            hm.put(REnum.status, true);
            hm.put(REnum.result, product1);
            return new ResponseEntity<>(hm, HttpStatus.OK);

        }


    }

    public ResponseEntity productDelete(Long id) {
        Map<REnum, Object> hm = new HashMap<>();
        try {
            proRepo.deleteById(id);
            cacheManager.getCache("listCacheProduct").clear();
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put(REnum.error, ex.getMessage());
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity update(Product product) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            Optional<Product> oldProduct = proRepo.findById(product.getId());
            Optional<Category> optionalCategory=catRepo.findById(product.getCategory().getId());
            if (!oldProduct.isPresent()) {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "There is not like an id in Product Entity");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            } else if(!optionalCategory.isPresent()) {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "There is not like an id in Category Entity");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }else{
                String capitalizedName= commonService.capitalizedWords(product.getName());
                product.setName(capitalizedName);
                proRepo.saveAndFlush(product);
                cacheManager.getCache("listCacheProduct").clear();
                hm.put(REnum.status, true);
                hm.put(REnum.message, "Update is successful");
                return new ResponseEntity<>(hm, HttpStatus.OK);
            }

        } catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put(REnum.message, ex.getMessage());

        }


        return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity list() {
        Map<REnum, Object> hm = new HashMap<>();

        List<Product> productList = proRepo.findAll();
        hm.put(REnum.status, true);
        hm.put(REnum.result, productList);
        return new ResponseEntity<>(hm, HttpStatus.OK);

    }
    public Product findProductById(Long productId) {
        return proRepo.findById(productId).get();
    }

    public ResponseEntity findProductBy_Search(String q){
        Map<REnum, Object> hm = new HashMap<>();
       List<Product> products=proRepo.findByNameContainsIgnoreCase(q);

        if(products.size()>0){
            hm.put(REnum.status,true);
            hm.put(REnum.result,products);
            return new ResponseEntity<>(hm,HttpStatus.OK);
        }else {
            hm.put(REnum.status,true);
            hm.put(REnum.message,"No results were found for your search \"+"+ q +"+\"");
            return new ResponseEntity<>(hm,HttpStatus.OK);
        }

    }
    public ResponseEntity findProductBy_Category(Integer id){
        Map<REnum, Object> hm = new HashMap<>();
        Optional<Category> optionalCategory=catRepo.findById(id);
        if(optionalCategory.isPresent()){
        List<Product> products=proRepo.findByCategory_IdEquals(id);
        hm.put(REnum.status,true);
        hm.put(REnum.result,products);
        return new ResponseEntity<>(hm,HttpStatus.OK);}
        else {
            hm.put(REnum.status,false);
            hm.put(REnum.message,"There is not such category id ");
            return new ResponseEntity<>(hm,HttpStatus.BAD_REQUEST);
        }

    }
    public ResponseEntity listByCompany()  {
        Map<REnum, Object> hm = new HashMap<>();

        Admin admin= (Admin) session.getAttribute("admin");

        if (admin!=null){
            List<Product> productList=proRepo.findByCreatedByEqualsIgnoreCase(admin.getEmail());
            hm.put(REnum.status, true);
            hm.put(REnum.result, productList);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        }else{
            hm.put(REnum.status, false);
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }

    }



}
