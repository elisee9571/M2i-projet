package com.example.demo.repository;

import com.example.demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByUserPseudo(String pseudo, Pageable pagination);
    Page<Product> findByCategoryAndTitleContainingOrCategoryAndContentContaining(Category category1, String title, Category category2, String content, Pageable pagination);
    Page<Product> findByTitleContainingOrContentContaining(String title, String content, Pageable pagination);
}
