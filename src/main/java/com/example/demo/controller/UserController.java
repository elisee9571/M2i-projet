package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
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

    @GetMapping("/{pseudo}")
    @JsonView(MyJsonView.User.class)
    public ResponseEntity<?> user(@PathVariable String pseudo) {
        try {
            return new ResponseEntity<>(userService.getUserByPseudo(pseudo), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    @JsonView(MyJsonView.User.class)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody User user) {
        try {
            userService.updateUser(user, id);
            return new ResponseEntity<>("Utilisateur mis à jour", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/updatePassword")
    @JsonView(MyJsonView.User.class)
    public ResponseEntity<?> updatePassword(@RequestBody User user) {
        try {
            userService.updatePassword(user.getOldPassword(), user.getPassword());
            return new ResponseEntity<>("Mot de passe mis à jour", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> requestForgotPassword(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.requestForgotPassword(user.getEmail()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(
            @RequestParam("resetToken") String resetToken,
            @RequestParam("dataToken") String dataToken,
            @RequestParam("userId") Integer userId,
            @RequestParam("newPassword") String newPassword
    ) {
        try {
            userService.resetPassword(resetToken, dataToken, userId, newPassword);
            return new ResponseEntity<>("Mot de passe réinitialisé", HttpStatus.OK);
        } catch (Exception e) {
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

    @GetMapping("/search")
    @JsonView(MyJsonView.Product.class)
    public ResponseEntity<?> searchUsers(
            @RequestParam("keyword") String keyword,
            @RequestParam("page") Integer pageNumber,
            @RequestParam("size") Integer pageSize
    ) {
        try {
            return new ResponseEntity<>(userService.searchUsersLikePseudo(keyword, pageNumber, pageSize), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}