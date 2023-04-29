package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.enums.Roles;
import com.example.demo.jsonView.MyJsonView;
import com.example.demo.service.ImageService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;

    @Autowired
    public UserController(
            UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody User user) {
        try {
            userService.create(user.getAvatar(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPseudo(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getPassword(),
                    Roles.USER);
            return new ResponseEntity<>("Account create", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @JsonView(MyJsonView.User.class)
    public ResponseEntity<?> users() {
        try {
            return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @JsonView(MyJsonView.User.class)
    public ResponseEntity<?> user(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}