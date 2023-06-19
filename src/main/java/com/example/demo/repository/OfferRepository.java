package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Offer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

    List<Offer> findByProductId(Integer productId, Sort sort);

    List<Offer> findByProductIdAndUser(Integer productId, User user, Sort sort);


}
