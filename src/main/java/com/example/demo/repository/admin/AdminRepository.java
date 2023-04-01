package com.example.demo.repository.admin;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface AdminRepository extends CrudRepository<User, Integer> {
}
