package com.example.demo.controller;

import com.example.demo.jsonView.MyJsonView;
import com.example.demo.enums.Status;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService adService;

    @Autowired
    public ProductController(ProductService adService){
        this.adService = adService;
    }

    @GetMapping
    @JsonView(MyJsonView.Product.class)
    public ResponseEntity<?> listAd() {
        try {
            return new ResponseEntity<>(adService.getAds(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @JsonView(MyJsonView.Product.class)
    public ResponseEntity<?> ad(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(adService.getProductById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Product product) {
        try {
            product.setStatus(Status.VISIBLE);
            adService.saveProduct(product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Product product) {
        try {
            adService.updateProduct(product, id);
            return new ResponseEntity<>("Produit mis à jour", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            adService.deleteProduct(id);
            return new ResponseEntity<>("Produit supprimé", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}