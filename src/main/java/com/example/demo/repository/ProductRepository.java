package com.example.demo.repository;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByTitle(String title);
    Page<Product> findByUserPseudo(String pseudo, Pageable pagination);

    Page<Product> findByStatus(Status status, Pageable pagination);

    Page<Product> findByUserPseudoAndCategory(String pseudo, Category category, Pageable pagination);

    Page<Product> findByCategoryAndTitleContainingAndStatusOrCategoryAndContentContainingAndStatus(
            Category category1, String title, Status status1,
            Category category2, String content, Status status2,
            Pageable pagination
    );

    Page<Product> findByTitleContainingAndStatusOrContentContainingAndStatus(
            String title, Status status1,
            String content, Status status2,
            Pageable pagination
    );

    Page<Product> findByUserPseudoAndStatusIn(String pseudo, List<Status> status, Pageable pagination);

    Page<Product> findByUserPseudoAndCategoryAndStatusIn(String pseudo, Category category, List<Status> status, Pageable pagination);

    Page<Product> findByUserPseudoAndStatus(String pseudo, Status status, Pageable pagination);

    Page<Product> findByUserPseudoAndCategoryAndStatus(String pseudo, Category category, Status status, Pageable pagination);
}