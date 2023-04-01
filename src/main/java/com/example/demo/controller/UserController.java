package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index() {
        return "page user";
    }

    @GetMapping("/index")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/post")
    public User saveUser() {
        return userService.saveUser(new User("User", "Doe", "user@gmail.com", "password", User.Role.USER));
    }
}