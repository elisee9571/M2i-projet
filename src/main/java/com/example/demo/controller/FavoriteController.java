package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Offer;
import com.example.demo.jsonView.MyJsonView;
import com.example.demo.service.FavoriteService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private FavoriteService favoriteService;
    @Autowired
    public FavoriteController(FavoriteService favoriteService){
        this.favoriteService = favoriteService;
    }

    @GetMapping
    @JsonView(MyJsonView.Favorite.class)
    public ResponseEntity<?> listFavoriteByUser(
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam("page") Integer pageNumber,
            @RequestParam("size") Integer pageSize
    ) {
        try {
            return new ResponseEntity<>(favoriteService.getFavoritesByUser(category, price, pageNumber, pageSize), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/check")
    @JsonView(MyJsonView.Favorite.class)
    public ResponseEntity<?> listFavoriteByUserCheck() {
        try {
            return new ResponseEntity<>(favoriteService.getFavoritesByUserCheck(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{productId}")
    @JsonView(MyJsonView.Favorite.class)
    public ResponseEntity<?> addFavorite(@PathVariable("productId") Integer productId) {
        try {
            favoriteService.saveFavoriteByProductId(productId);
            return new ResponseEntity<>("Produit ajouté en favoris", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{productId}")
    @JsonView(MyJsonView.Favorite.class)
    public ResponseEntity<?> deleteFavorite(@PathVariable("productId") Integer productId) {
        try {
            favoriteService.deleteFavoriteByProductId(productId);
            return new ResponseEntity<>("Produit supprimé des favoris", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
