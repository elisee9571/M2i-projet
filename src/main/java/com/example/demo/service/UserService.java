package com.example.demo.service;

import com.example.demo.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Iterable<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        return userRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'id: " + id));
    }

    public User getUserByUsername(String username){
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'email: " + username));
    }

    public User create(String avatar, String firstname, String lastname, String pseudo, String email, Integer phone, String password, Roles role) {

        Optional<User> pseudoAlreadyUse = userRepository.findByPseudo(pseudo);
        Optional<User> emailAlreadyUse = userRepository.findByEmail(password);

        if (pseudoAlreadyUse.isPresent()){
            throw new IllegalStateException("Pseudo déjà utilisé");
        }

        if (emailAlreadyUse.isPresent()){
            throw new IllegalStateException("Email déjà utilisé");
        }

        User user = new User(avatar, firstname, lastname, pseudo, email, phone, passwordEncoder.encode(password), role);

        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        boolean isExist = userRepository.existsById(id);

        if (!isExist){
            throw new IllegalStateException("Catégorie introuvable");
        }

        userRepository.deleteById(id);
    }



}
