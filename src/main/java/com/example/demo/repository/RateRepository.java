package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Rate;

@Repository
public interface RateRepository extends CrudRepository<Rate, Integer> {

}
