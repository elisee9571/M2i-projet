package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Ad;

@Repository
public interface AdRepository extends CrudRepository<Ad, Integer> {

}
