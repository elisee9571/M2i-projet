package com.example.demo.service.auth;

import com.example.demo.entity.User;
import com.example.demo.enums.Roles;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(String firstname, String lastname, String pseudo, String email, String password, Roles role){
        User user = new User(firstname, lastname, pseudo,email, passwordEncoder.encode(password), role);
        return userRepository.save(user);
    }

}
