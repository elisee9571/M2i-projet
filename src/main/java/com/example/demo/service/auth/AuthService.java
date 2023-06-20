package com.example.demo.service.auth;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.enums.Roles;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public User register(String firstname, String lastname, String pseudo, String email, String password, Roles role) throws MessagingException {
        Optional<User> pseudoAlreadyUse = userRepository.findByPseudo(pseudo);
        Optional<User> emailAlreadyUse = userRepository.findByEmail(email);

        if (pseudoAlreadyUse.isPresent()){
            throw new IllegalStateException("Pseudo déjà utilisé");
        }

        if (emailAlreadyUse.isPresent()){
            throw new IllegalStateException("Email déjà utilisé");
        }

        if (pseudo.length() > 20) {
            throw new IllegalArgumentException("Le pseudo ne peut pas dépasser 20 caractères");
        }

        User user = new User(firstname, lastname, pseudo, email, passwordEncoder.encode(password), role);
        emailService.sendEmail("recipient@example.com", "Test d'e-mail");

        return userRepository.save(user);
    }

}
