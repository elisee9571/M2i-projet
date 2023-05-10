package com.example.demo.service;

import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Iterable<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Catégorie introuvable avec l'id: " + id));
    }

    public void saveCategory(Category category) {
        Optional<Category> optionalCategory = categoryRepository.findByTitle(category.getTitle());
        if (optionalCategory.isPresent()){
            throw new IllegalStateException("Catégorie déjà existante");
        }
        categoryRepository.save(category);
    }
    public void updateCategory(Category data, Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Catégorie introuvable avec l'id: " + id));

        if (data.getTitle() != null && data.getTitle().length() > 0 && !Objects.equals(category.getTitle(), data.getTitle())){

            Optional<Category> optionalCategory = categoryRepository.findByTitle(data.getTitle());

            if (optionalCategory.isPresent()){
                throw new IllegalStateException("Titre déjà utilisé");
            }

            category.setTitle(data.getTitle());
        }

        if (data.getParent() != null  && !Objects.equals(category.getParent(), data.getParent())){
            category.setParent(data.getParent());
        }

        categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        boolean isExist = categoryRepository.existsById(id);

        if (!isExist){
            throw new IllegalStateException("Catégorie introuvable");
        }

        categoryRepository.deleteById(id);
    }

}
