package com.example.demo.service.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.admin.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Iterable<User> getUsers() {
        return adminRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return adminRepository.findById(id);
    }

    public User saveAdmin(User user) {
        return adminRepository.save(user);
    }

    public void deleteUserById(Integer id) {
        adminRepository.deleteById(id);
    }

}
