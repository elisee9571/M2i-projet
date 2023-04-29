package com.example.demo.controller.auth;

import com.example.demo.config.token.JwtUtil;
import com.example.demo.entity.User;
import com.example.demo.entity.UserPrincipal;
import com.example.demo.enums.Roles;
import com.example.demo.service.auth.AuthService;
import com.example.demo.service.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserPrincipalService userPrincipalService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(
            AuthService authService,
            UserPrincipalService userPrincipalService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.authService = authService;
        this.userPrincipalService = userPrincipalService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registration(@RequestBody User user) {
        try {
            authService.register(user.getFirstName(),
                    user.getLastName(),
                    user.getPseudo(),
                    user.getEmail(),
                    user.getPassword(),
                    Roles.USER);
            return new ResponseEntity<>("Account create", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> authentification(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        }
        catch (BadCredentialsException e) {
            return new ResponseEntity<>("Identified or password incorrect", HttpStatus.BAD_REQUEST);
        }

        final UserDetails userDetails = userPrincipalService.loadUserByUsername(user.getEmail());

        return new ResponseEntity<>(jwtUtil.generateToken(userDetails), HttpStatus.ACCEPTED);
    }
}
