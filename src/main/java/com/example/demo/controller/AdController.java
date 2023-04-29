package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.jsonView.MyJsonView;
import com.example.demo.enums.Status;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Ad;
import com.example.demo.service.AdService;

@Controller
@RequestMapping("/ads")
public class AdController {

    private final AdService adService;

    @Autowired
    public AdController(AdService adService){
        this.adService = adService;
    }

    @GetMapping
    @JsonView(MyJsonView.Ad.class)
    public ResponseEntity<?> listAd() {
        try {
            return new ResponseEntity<>(adService.getAds(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @JsonView(MyJsonView.Ad.class)
    public ResponseEntity<?> ad(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(adService.getAdById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Ad ad) {
        try {
            ad.setStatus(Status.VISIBLE);
            adService.saveAd(ad);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Ad ad) {
        try {
            adService.updateAd(ad, id);
            return new ResponseEntity<>("Ad updated", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            adService.deleteAd(id);
            return new ResponseEntity<>("Ad deleted", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}