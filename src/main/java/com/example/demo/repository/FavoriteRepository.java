package com.example.demo.repository;

import com.example.demo.entity.Favorite;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    Page<Favorite> findByUserPseudo(String pseudo, Pageable pagination);

    boolean existsByUserAndProduct(User user, Product product);

    Optional<Favorite> findByUserAndProduct(User user, Product product);
}
