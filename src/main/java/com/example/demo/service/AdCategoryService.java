package com.example.demo.service;

import com.example.demo.repository.AdCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdCategoryService {

    @Autowired
    private AdCategoryRepository adCategoryRepository;
}
