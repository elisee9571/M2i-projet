package com.example.demo.service;

import java.io.IOException;
import java.util.*;

import com.example.demo.config.token.util.TokenAuthorization;
import com.example.demo.entity.*;
import com.example.demo.enums.Currencies;
import com.example.demo.enums.Status;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

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

    @Autowired
    private  ImageService imageService;

    public Map<String, Object> getProducts(Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pagination = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Product> productPage = productRepository.findByStatus(Status.VISIBLE, pagination);
        List<Product> products = productPage.getContent();
        int totalPages = productPage.getTotalPages();
        long totalProducts = productPage.getTotalElements();

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("totalPages", totalPages);
        response.put("totalProducts", totalProducts);

        return response;
    }

    public Map<String, Object> getProductsByUser(String pseudo, Category category, String sorted, Integer pageNumber, Integer pageSize) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByPseudo(pseudo)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec le pseudo: " + pseudo));

        Sort sort = Sort.by("createdAt").descending();
        Pageable pagination;

        if(sorted == null){
            pagination = PageRequest.of(pageNumber - 1, pageSize, sort);
        }else {
            Sort sortBy;

            if ("priceASC".equals(sorted)) {
                sortBy = Sort.by("price").ascending();
            } else if ("priceDESC".equals(sorted)) {
                sortBy = Sort.by("price").descending();
            } else if ("createdAtASC".equals(sorted)) {
                sortBy = Sort.by("createdAt").ascending();
            }else {
                sortBy = Sort.by("createdAt").descending();
            }

            Sort sortGroup = sortBy.and(sort);
            pagination = PageRequest.of(pageNumber - 1, pageSize, sortGroup);
        }

        Page<Product> productPage;

        if (username != null) {
            User userVisitor = userRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

            if (user.getId().equals(userVisitor.getId())) {
                // L'utilisateur connecté est le même que l'utilisateur visité,
                // donc afficher tous les produits de cet utilisateur, quels que soient leur statut
                if (category == null) {
                    productPage = productRepository.findByUserPseudo(user.getPseudo(), pagination);
                } else {
                    productPage = productRepository.findByUserPseudoAndCategory(user.getPseudo(), category, pagination);
                }
            } else {
                // L'utilisateur connecté est différent de l'utilisateur visité,
                // donc afficher uniquement les produits avec le statut visible ou vendu
                if (category == null) {
                    productPage = productRepository.findByUserPseudoAndStatusIn(user.getPseudo(), Arrays.asList(Status.VISIBLE, Status.SOLD), pagination);
                } else {
                    productPage = productRepository.findByUserPseudoAndCategoryAndStatusIn(user.getPseudo(), category, Arrays.asList(Status.VISIBLE, Status.SOLD), pagination);
                }
            }
        } else {
            // Pas d'utilisateur connecté, afficher uniquement les produits avec le statut visible
            if (category == null) {
                productPage = productRepository.findByUserPseudoAndStatus(user.getPseudo(), Status.VISIBLE, pagination);
            } else {
                productPage = productRepository.findByUserPseudoAndCategoryAndStatus(user.getPseudo(), category, Status.VISIBLE, pagination);
            }
        }

        List<Product> products = productPage.getContent();
        int totalPages = productPage.getTotalPages();
        long totalProducts = productPage.getTotalElements();

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("totalPages", totalPages);
        response.put("totalProducts", totalProducts);

        return response;
    }

    public Product getProductById(Integer id) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Produit introuvable avec l'id: " + id));

        if (username != null) {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

            if (!product.getOffers().isEmpty()) {
                for (Offer offer : product.getOffers()) {
                    if (offer.getUser().equals(user) && offer.getStatus() == Status.ACCEPTED) {
                        product.setPriceOffer(offer.getAmount());
                    }
                }
            }
        }

        return product;
    }

    public Product saveProduct(Product product) throws IOException {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        product.setStatus(Status.VISIBLE);
        product.setCurrencyCode(Currencies.EUR);
        product.setUser(user);

        Product test = productRepository.save(product);

        imageService.saveFiles(test);
        return product;
    }

    public Product saveProductStatusDraft(Product product) throws IOException {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));


        product.setStatus(Status.DRAFT);
        product.setCurrencyCode(Currencies.EUR);
        product.setUser(user);

        Product test = productRepository.save(product);

        imageService.saveFiles(test);
        return product;
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

    public void updateProductStatus(Integer id, Status status){
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Produit introuvable avec l'id: " + id));

        if(!Objects.equals(user.getId(), product.getUser().getId())){
            throw new IllegalStateException("Vous n'etes pas autorisé à effectuer cette opération.");
        }

        if(status.equals(product.getStatus())){
            throw new IllegalStateException("Il semble que votre produit soit déjà dans l'état que vous souhaitez.");
        }

        product.setStatus(status);

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

    public Map<String, Object> searchProductsByCategoryAndKeyword(Category category, String keyword, String sorted, Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pagination;

        if(sorted == null){
            pagination = PageRequest.of(pageNumber - 1, pageSize, sort);
        }else {
            Sort sortBy;

            if ("priceASC".equals(sorted)) {
                sortBy = Sort.by("price").ascending();
            } else if ("priceDESC".equals(sorted)) {
                sortBy = Sort.by("price").descending();
            } else if ("createdAtASC".equals(sorted)) {
                sortBy = Sort.by("createdAt").ascending();
            }else {
                sortBy = Sort.by("createdAt").descending();
            }

            Sort sortGroup = sortBy.and(sort);
            pagination = PageRequest.of(pageNumber - 1, pageSize, sortGroup);
        }

        Page<Product> productPage;

        if (category == null) {
            productPage = productRepository.findByTitleContainingAndStatusOrContentContainingAndStatus(keyword, Status.VISIBLE, keyword, Status.VISIBLE,pagination);
        } else {
            productPage = productRepository.findByCategoryAndTitleContainingAndStatusOrCategoryAndContentContainingAndStatus(category, keyword, Status.VISIBLE, category, keyword, Status.VISIBLE, pagination);
        }

        List<Product> products = productPage.getContent();
        int totalPages = productPage.getTotalPages();
        long totalProducts = productPage.getTotalElements();

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("totalPages", totalPages);
        response.put("totalProducts", totalProducts);

        return response;
    }


}
