package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

}
