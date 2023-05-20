package com.example.demo.service;

import java.util.List;
import java.util.Objects;

import com.example.demo.config.token.util.TokenAuthorization;
import com.example.demo.entity.Category;
import com.example.demo.entity.Offer;
import com.example.demo.entity.User;
import com.example.demo.enums.Status;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

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

    @Autowired
    private  TokenAuthorization tokenAuthorization;

    public List<Product> getProducts(Integer pageNumber, Integer pageSize) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        Sort sort = Sort.by("createdAt").descending();
        Pageable pagination = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> productPage = productRepository.findAll(pagination);
        List<Product> products = productPage.getContent();

        if(username != null){
            User user = userRepository.findByEmail(username)
                    .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

            for (Product product : products) {
                if (!product.getOffers().isEmpty()) {
                    for (Offer offer : product.getOffers()) {
                        if (offer.getUser().equals(user)) {
                            product.setPrice(offer.getAmount());
                        }
                    }
                }
            }

        }
        return products;
    }

    public List<Product> getProductsByUser(String pseudo, Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pagination = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> productPage = productRepository.findByUserPseudo(pseudo, pagination);
        return productPage.getContent();
    }

    public Product getProductById(Integer id) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Produit introuvable avec l'id: " + id));

        if(username != null){
            User user = userRepository.findByEmail(username)
                    .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

            if (!product.getOffers().isEmpty()) {
                for (Offer offer : product.getOffers()) {
                    if (offer.getUser().equals(user)) {
                        product.setPrice(offer.getAmount());
                    }
                }
            }

        }
        return product;
    }

    public void saveProduct(Product product) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        Integer categoryId = product.getCategory().getId();

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new IllegalStateException("Catégorie introuvable avec l'id: " + categoryId));

        product.setStatus(Status.VISIBLE);
        product.setUser(user);
        product.setCategory(category);

        productRepository.save(product);
    }

    public void updateProduct(Product data, Integer id){
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Produit introuvable avec l'id: " + id));

        if(!Objects.equals(user.getId(), product.getUser().getId())){
            throw new IllegalStateException("Vous n'etes pas autorisé à effectuer cette opération.");
        }

        if (data.getTitle() != null && data.getTitle().length() > 0 && !Objects.equals(product.getTitle(), data.getTitle())){
            product.setTitle(data.getTitle());
        }

        if (data.getContent() != null && data.getContent().length() > 0 && !Objects.equals(product.getContent(), data.getContent())){
            product.setContent(data.getContent());
        }

        if (data.getPrice() != null && !Objects.equals(product.getPrice(), data.getPrice())){
            product.setPrice(data.getPrice());
        }

        productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Produit introuvable avec l'id: " + id));

        if(!Objects.equals(user.getId(), product.getUser().getId())){
            throw new IllegalStateException("L'utilisateur n'est pas autorisé à effectuer cette opération");
        }

        productRepository.deleteById(id);
    }

    public List<Product> searchProductsByCategoryAndKeyword(Category category, String keyword, String price, Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pagination;

        if(price == null){
            pagination = PageRequest.of(pageNumber, pageSize, sort);
        }else {
            Sort sortPrice;

            if("priceASC".equals(price)){
                sortPrice = Sort.by("price").ascending();
            }else {
                sortPrice = Sort.by("price").descending();
            }

            Sort sortGroup = sortPrice.and(sort);
            pagination = PageRequest.of(pageNumber, pageSize, sortGroup);
        }

        Page<Product> productPage;

        if (category == null) {
            productPage = productRepository.findByTitleContainingOrContentContaining(keyword, keyword, pagination);
        } else {
            productPage = productRepository.findByCategoryAndTitleContainingOrCategoryAndContentContaining(category, keyword, category, keyword, pagination);
        }

        return productPage.getContent();
    }
}
