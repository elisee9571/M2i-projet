package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserPrincipal;
import com.example.demo.repository.UserPrincipalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPrincipalService implements UserDetailsService {

    @Autowired
    private UserPrincipalRepository userPrincipalRepository;

    public Optional<User> findByEmail(String email){
        return userPrincipalRepository.findByEmail(email);
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userPrincipalRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + username));
        return new UserPrincipal(user);
    }
}
