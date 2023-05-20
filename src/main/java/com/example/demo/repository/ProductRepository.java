package com.example.demo.repository;

import com.example.demo.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByUserPseudo(String pseudo, Sort sort);
    List<Product> findByCategoryAndTitleContainingOrCategoryAndContentContaining(Category category1, String title, Category category2, String content, Sort sort);
    List<Product> findByTitleContainingOrContentContaining(String title, String content, Sort sort);
}
