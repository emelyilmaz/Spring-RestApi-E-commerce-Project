package com.works.restcontrollers;

import com.works.entities.Category;
import com.works.services.CategoryService;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryRestController {
    final CategoryService catService;

    public CategoryRestController(CategoryService catService) {
        this.catService = catService;
    }

    @PostMapping("/add")
    public ResponseEntity add(@Valid @RequestBody Category category){
        return catService.add(category);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Integer id){
        return catService.delete(id);
    }

    @GetMapping("/list")
    public ResponseEntity list(){

        return catService.list();
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Category category){

        return catService.update(category);
    }



}
