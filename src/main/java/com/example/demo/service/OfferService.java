package com.example.demo.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.config.token.util.TokenAuthorization;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.enums.Status;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Offer;
import com.example.demo.repository.OfferRepository;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenAuthorization tokenAuthorization;
    @Autowired
    private ProductRepository productRepository;

    public List<Offer> getOffers() {
        return offerRepository.findAll();
    }

    public List<Offer> getOfferByProductId(Integer productId) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Produit introuvable avec l'id: " + productId));

        List<Offer> offers;

        if (username.equals(product.getUser().getEmail())) {
            // L'utilisateur connecté est le propriétaire du produit
            offers = offerRepository.findByProductId(productId, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else if (username.equals(user.getEmail())) {
            // L'utilisateur connecté est celui qui a proposé l'offre
            offers = offerRepository.findByProductIdAndUser(productId, user, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            // Autre utilisateur
            offers = new ArrayList<>(); // Array vide car c'est un autre utilisateur
        }

        return offers;
    }

    public Product proposeOffer(Integer productId, Offer offer) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Produit introuvable avec l'id: " + productId));

        if (user.equals(product.getUser())) {
            throw new IllegalArgumentException("Vous ne pouvez pas proposer une offre sur votre propre produit.");
        } else {
            double productPrice = product.getPrice();
            double proposedPrice = offer.getAmount();
            double minimumPrice = productPrice * 0.4;
            // Formater le minimumPrice avec deux chiffres après la virgule
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            String formattedMinimumPrice = decimalFormat.format(minimumPrice);

            if (proposedPrice > minimumPrice) {
                offer.setUser(user);
                offer.setProduct(product);
                offer.setStatus(Status.PENDING);

                offerRepository.save(offer);
            } else {
                throw new IllegalArgumentException("Le prix proposé doit être de " + formattedMinimumPrice + "€ minimum et ne doit pas dépasser (40% de réduction).");
            }
        }
        return product;
    }

    public void acceptOrRejectOffer(Integer productId, Integer offerId, boolean accept) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalStateException("Offre introuvable avec l'id: " + offerId));

        Product productCheck = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Produit introuvable avec l'id: " + productId));

        if (user.equals(offer.getUser()))
            throw new IllegalArgumentException("Vous n'etes pas autorisé à effectuer cette opération.");

        if (!productCheck.getId().equals(offer.getProduct().getId()))
            throw new IllegalArgumentException("L'offre ne correspond pas au bon produit. Veuillez vérifier que l'offre est associée au bon produit.");

        if (offer.getStatus() == Status.PENDING) {
            if (accept) {
                offer.setStatus(Status.ACCEPTED);
            } else {
                offer.setStatus(Status.REJECTED);
            }

            offerRepository.save(offer);

            if (offer.getStatus() == Status.ACCEPTED) {
                Product product = offer.getProduct();
                product.getOffers().add(offer);
                productRepository.save(product);
            }
        } else {
            throw new IllegalArgumentException("Cette offre n'est plus en attente de décision.");
        }
    }

}
