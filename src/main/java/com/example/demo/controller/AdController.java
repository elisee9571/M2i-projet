package com.example.demo.controller;

import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Ad;
import com.example.demo.service.AdService;

@RestController
@RequestMapping("/ads")
public class AdController {

    private AdService adService;

    @Autowired
    public AdController(AdService adService){
        this.adService = adService;
    }

    @GetMapping
    public String index() {
        return "page ad";
    }

}