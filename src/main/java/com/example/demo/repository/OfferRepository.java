package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Offer;

@Repository
public interface OfferRepository extends CrudRepository<Offer, Integer> {

}
