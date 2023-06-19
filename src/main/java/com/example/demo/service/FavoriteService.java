package com.example.demo.service;

import com.example.demo.config.token.util.TokenAuthorization;
import com.example.demo.entity.*;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TokenAuthorization tokenAuthorization;

    public Map<String, Object> getFavoritesByUser(Category category, String price, Integer pageNumber, Integer pageSize) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        if (username == null) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Sort sort = Sort.by("createdAt").descending();
        Pageable pagination;

        if(price == null){
            pagination = PageRequest.of(pageNumber - 1, pageSize, sort);
        }else {
            Sort sortPrice;

            if("priceASC".equals(price)){
                sortPrice = Sort.by("product.price").ascending();
            }else {
                sortPrice = Sort.by("product.price").descending();
            }

            Sort sortGroup = sortPrice.and(sort);
            pagination = PageRequest.of(pageNumber - 1, pageSize, sortGroup);
        }

        Page<Favorite> favoritePage;

        if (category == null) {
            favoritePage = favoriteRepository.findByUserPseudo(user.getPseudo(), pagination);
        } else {
            favoritePage = favoriteRepository.findByUserPseudoAndProductCategory(user.getPseudo(), category, pagination);
        }

        List<Favorite> favorites = favoritePage.getContent();
        int totalPages = favoritePage.getTotalPages();
        long totalProducts = favoritePage.getTotalElements();


        Map<String, Object> response = new HashMap<>();
        response.put("favorites", favorites);
        response.put("totalPages", totalPages);
        response.put("totalProducts", totalProducts);

        return response;
    }

    public List<Favorite> getFavoritesByUserCheck() {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        if (username == null) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        return favoriteRepository.findByUserPseudo(user.getPseudo());
    }

    public Favorite getFavoriteById(Integer id) {
        return favoriteRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Favoris introuvable avec l'id: " + id));
    }

    public void saveFavoriteByProductId(Integer productId) {
        Favorite favorite = new Favorite();
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        if (username == null) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Produit introuvable avec l'id: " + productId));

        if (favoriteRepository.existsByUserAndProduct(user, product)) {
            throw new IllegalStateException("Le produit est déjà ajouté en favoris");
        }

        favorite.setUser(user);
        favorite.setProduct(product);

        favoriteRepository.save(favorite);
    }

    public void deleteFavoriteByProductId(Integer productId) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        if (username == null) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Produit introuvable avec l'id: " + productId));

        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new IllegalStateException("Le produit n'est pas dans en favoris"));

        favoriteRepository.delete(favorite);
    }
}
