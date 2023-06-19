package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.jsonView.MyJsonView;
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

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @JsonView(MyJsonView.Product.class)
    public ResponseEntity<?> listProducts(
            @RequestParam("page") Integer pageNumber,
            @RequestParam("size") Integer pageSize
    ) {
        try {
            return new ResponseEntity<>(productService.getProducts(pageNumber, pageSize), HttpStatus.OK);
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
    public ResponseEntity<?> listProductsByUser(
            @PathVariable String pseudo,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam("page") Integer pageNumber,
            @RequestParam("size") Integer pageSize
    ) {
        try {
            return new ResponseEntity<>(productService.getProductsByUser(pseudo, category, price, pageNumber, pageSize), HttpStatus.OK);
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
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam("page") Integer pageNumber,
            @RequestParam("size") Integer pageSize
    ) {
        try {
            return new ResponseEntity<>(productService.searchProductsByCategoryAndKeyword(category, keyword, price, pageNumber, pageSize), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}