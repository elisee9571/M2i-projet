package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Rate;
import com.example.demo.service.RateService;

@RestController
@RequestMapping("/rate")
public class RateController {

    @Autowired
    private RateService rateService;

    @GetMapping("")
    public String index() {
        return "page rate";
    }

}