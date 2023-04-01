package com.example.demo.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.admin.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("")
    public String index() {
        return "page admin";
    }

    @GetMapping("/index")
    public Iterable<User> getUsers() {
        return adminService.getUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") Integer id) {
        return adminService.getUserById(id);
    }

    @PostMapping("/post")
    public User saveAdmin() {
        return adminService.saveAdmin(new User("Admin", "Doe", "admin@gmail.com", "password", User.Role.ADMIN));
    }
}