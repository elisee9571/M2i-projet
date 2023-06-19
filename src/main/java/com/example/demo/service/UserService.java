package com.example.demo.service;

import com.example.demo.config.token.util.TokenAuthorization;
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

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenAuthorization tokenAuthorization;

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

    public void updateUser(User data, Integer id){
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        if(!Objects.equals(user.getId(), id)){
            throw new IllegalStateException("Vous n'etes pas autorisé à effectuer cette opération.");
        }

        if (data.getFirstname() != null && data.getFirstname().length() > 0 && !Objects.equals(user.getFirstname(), data.getFirstname())){
            user.setFirstname(data.getFirstname());
        }

        if (data.getLastname() != null && data.getLastname().length() > 0 && !Objects.equals(user.getLastname(), data.getLastname())){
            user.setLastname(data.getLastname());
        }

        if (data.getPseudo().length() > 20) {
            throw new IllegalArgumentException("Le pseudo ne peut pas dépasser 20 caractères");
        }

        if (data.getPseudo() != null && data.getPseudo().length() > 0 && !Objects.equals(user.getPseudo(), data.getPseudo())){
            user.setPseudo(data.getPseudo());
        }

        if (data.getEmail() != null && data.getEmail().length() > 0 && !Objects.equals(user.getEmail(), data.getEmail())){
            user.setEmail(data.getEmail());
        }

        if (data.getPhone().length() > 10) {
            throw new IllegalArgumentException("Le Téléphone ne peut pas dépasser 10 chiffres");
        }

        if (data.getPhone() != null && !Objects.equals(user.getPhone(), data.getPhone())){
            user.setPhone(data.getPhone());
        }

        if (data.getBiography() != null && data.getBiography().length() > 0 && !Objects.equals(user.getBiography(), data.getBiography())){
            user.setBiography(data.getBiography());
        }

        if (data.getAddress() != null && data.getAddress().length() > 0 && !Objects.equals(user.getAddress(), data.getAddress())){
            user.setAddress(data.getAddress());
        }

        if (!Objects.equals(user.getAdditionalAddress(), data.getAdditionalAddress())){
            user.setAdditionalAddress(data.getAdditionalAddress());
        }

        if (data.getCity() != null && data.getCity().length() > 0 && !Objects.equals(user.getCity(), data.getCity())){
            user.setCity(data.getCity());
        }

        if (data.getZipCode().length() > 5) {
            throw new IllegalArgumentException("Le code postale ne peut pas dépasser 5 chiffres");
        }

        if (data.getZipCode() != null && data.getZipCode().length() > 0 && !Objects.equals(user.getZipCode(), data.getZipCode())){
            user.setZipCode(data.getZipCode());
        }

        userRepository.save(user);
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
