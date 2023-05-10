package com.example.demo.service;

import java.util.List;
import java.util.Objects;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAds() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Produit introuvable avec l'id: " + id));
    }

    public void saveProduct(Product product) {
        Integer userId = product.getUser().getId();
        Integer categoryId = product.getCategory().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'id: " + userId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new IllegalStateException("Catégorie introuvable avec l'id: " + categoryId));

        product.setUser(user);
        product.setCategory(category);

        productRepository.save(product);
    }

    public void updateProduct(Product data, Integer id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Produit introuvable avec l'id: " + id));

        if (data.getTitle() != null && data.getTitle().length() > 0 && !Objects.equals(product.getTitle(), data.getTitle())){
            product.setTitle(data.getTitle());
        }

        if (data.getContent() != null && data.getContent().length() > 0 && !Objects.equals(product.getContent(), data.getContent())){
            product.setContent(data.getContent());
        }

        if (data.getPrice() != null && !Objects.equals(product.getPrice(), data.getPrice())){
            product.setPrice(data.getPrice());
        }

        productRepository.save(data);
    }

    public void deleteProduct(Integer id) {
        boolean isExist = productRepository.existsById(id);

        if (!isExist){
            throw new IllegalStateException("Produit introuvable");
        }

        productRepository.deleteById(id);
    }
}
