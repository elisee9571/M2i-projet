package com.example.demo.repository;

import com.example.demo.entity.AdCategory;
import com.example.demo.entity.CleAdCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdCategoryRepository extends JpaRepository<AdCategory, CleAdCategory> {

    public Optional<AdCategory> findByAdIdAndCategoryId(Integer adId, Integer categoryId);
}
