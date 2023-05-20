package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.enums.Roles;
import com.example.demo.jsonView.MyJsonView;
import com.example.demo.service.ImageService;
import com.example.demo.service.UserService;
import com.example.demo.service.auth.AuthService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final ImageService imageService;

    @Autowired
    public UserController(
            UserService userService, AuthService authService, ImageService imageService) {
        this.userService = userService;
        this.authService = authService;
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody User user) {
        try {
            authService.register(
                    user.getFirstname(),
                    user.getLastname(),
                    user.getPseudo(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole()
            );
            return new ResponseEntity<>("Compte crée", HttpStatus.CREATED);
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

    @GetMapping("/{username}")
    @JsonView(MyJsonView.User.class)
    public ResponseEntity<?> user(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>("Utilisateur supprimé", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}