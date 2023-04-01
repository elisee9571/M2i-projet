package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Ad;
import com.example.demo.service.AdService;

@RestController
@RequestMapping("/ad")
public class AdController {

    @Autowired
    private AdService adService;

    @GetMapping("")
    public String index() {
        return "page ad";
    }

}