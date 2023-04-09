package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Offer;
import com.example.demo.service.OfferService;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private OfferService offerService;
    @Autowired
    public OfferController(OfferService offerService){
        this.offerService = offerService;
    }

    @GetMapping
    public String index() {
        return "page offer";
    }

}