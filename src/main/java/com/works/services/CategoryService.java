package com.works.services;

import com.works.entities.Category;
import com.works.repositories.CategoryRepository;
import com.works.utils.REnum;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;



@Service
public class CategoryService {

    final CategoryRepository catRepo;
    final CommonService commonService;


    public CategoryService(CategoryRepository catRepo,CommonService commonService) {

        this.catRepo = catRepo;
        this.commonService=commonService;
    }

    public ResponseEntity add(Category category) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        Optional<Category> optionalCategory = catRepo.findByCategoryNameEqualsIgnoreCase(category.getCategoryName());
        if (!optionalCategory.isPresent()) {
            String capitalizedName= commonService.capitalizedWords(category.getCategoryName());
            category.setCategoryName(capitalizedName);
            Category category1 = catRepo.save(category);
            hm.put(REnum.status, true);
            hm.put(REnum.result, category1);
            return new ResponseEntity<>(hm, HttpStatus.OK);

        } else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "There is already a category with this name");
            return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    public ResponseEntity delete(Integer id) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            catRepo.deleteById(id);
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put(REnum.error, ex.getMessage());
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity update(Category category) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            Optional<Category> oldCategory = catRepo.findById(category.getId());
            if (oldCategory.isPresent()) {
                String capitalizedName=commonService.capitalizedWords(category.getCategoryName());
                category.setCategoryName(capitalizedName);
                catRepo.saveAndFlush(category);
                hm.put(REnum.status, true);
                hm.put(REnum.message, "Update is successful");
                System.out.println("if" + hm);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            } else {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "There is not like an id in Category Entity");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put(REnum.message, ex.getMessage());

        }

        return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity list() {
        Map<REnum, Object> hm = new HashMap<>();
        List<Category> categoryList = catRepo.findAll();
        hm.put(REnum.status, true);
        hm.put(REnum.result, categoryList);
        return new ResponseEntity<>(hm, HttpStatus.OK);

    }


}

