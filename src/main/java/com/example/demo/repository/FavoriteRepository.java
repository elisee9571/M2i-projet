package com.example.demo.repository;

import com.example.demo.entity.Category;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Page<Favorite> findByUserPseudoAndProductStatusIn(String pseudo, List<Status> status, Pageable pagination);
    Page<Favorite> findByUserPseudoAndProductCategoryAndProductStatusIn(String pseudo, Category category, List<Status> status, Pageable pagination);
    List<Favorite> findByUserPseudo(String pseudo);

    boolean existsByUserAndProduct(User user, Product product);

    Optional<Favorite> findByUserAndProduct(User user, Product product);
}
