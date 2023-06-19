package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        return userRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'id: " + id));
    }

    public User getUserByPseudo(String pseudo){
        return userRepository.findByPseudo(pseudo)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'email: " + pseudo));
    }

    public User create(String avatar, String firstname, String lastname, String pseudo, String email, String phone, String password, Roles role) {

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

    public Map<String, Object> searchUsersLikePseudo(String keyword, Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pagination = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<User> userPage = userRepository.findByPseudoContainingIgnoreCase(keyword, pagination);

        List<User> users = userPage.getContent();
        int totalPages = userPage.getTotalPages();
        long totalUsers = userPage.getTotalElements();

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("totalPages", totalPages);
        response.put("totalUsers", totalUsers);

        return response;
    }

    }
