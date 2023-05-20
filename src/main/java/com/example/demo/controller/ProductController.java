package com.example.demo.controller;

import com.example.demo.config.token.util.TokenAuthorization;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.jsonView.MyJsonView;
import com.example.demo.enums.Status;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

import java.util.Objects;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final TokenAuthorization tokenAuthorization;

    @Autowired
    public ProductController(ProductService productService, UserService userService, TokenAuthorization tokenAuthorization) {
        this.productService = productService;
        this.userService = userService;
        this.tokenAuthorization = tokenAuthorization;
    }

    @GetMapping
    @JsonView(MyJsonView.Product.class)
    public ResponseEntity<?> listProducts() {
        try {
            return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @JsonView(MyJsonView.Product.class)
    public ResponseEntity<?> productId(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{pseudo}")
    @JsonView(MyJsonView.Product.class)
    public ResponseEntity<?> listProductsByUser(@PathVariable String pseudo) {
        try {
            return new ResponseEntity<>(productService.getProductsByUser(pseudo), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Product product) {
        try {
            productService.saveProduct(product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Product product) {
        try {
            productService.updateProduct(product, id);
            return new ResponseEntity<>("Produit mis à jour", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Produit supprimé", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    @JsonView(MyJsonView.Product.class)
    public ResponseEntity<?> searchProducts(
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam("keyword") String keyword) {
        try {
            return new ResponseEntity<>(productService.searchProductsByCategoryAndKeyword(category, keyword), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}