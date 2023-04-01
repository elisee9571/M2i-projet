package com.example.demo.service.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.admin.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

}
