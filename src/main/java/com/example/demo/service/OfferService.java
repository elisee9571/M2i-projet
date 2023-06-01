package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.config.token.util.TokenAuthorization;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.enums.Status;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Offer;
import com.example.demo.repository.OfferRepository;
import org.springframework.web.bind.annotation.RequestBody;

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

    public Offer getOfferById(Integer id) {
        return offerRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Offre introuvable avec l'id: " + id));
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
            offer.setUser(user);
            offer.setProduct(product);
            offer.setStatus(Status.PENDING);

            offerRepository.save(offer);
        }
        return product;
    }

    public void acceptOrRejectOffer(Integer offerId, boolean accept) {
        String username = tokenAuthorization.getUsernameFromAuthorizationHeader();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable avec l'username: " + username));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalStateException("Offre introuvable avec l'id: " + offerId));

        if (user.equals(offer.getUser()))
            throw new IllegalArgumentException("Vous n'etes pas autorisé à effectuer cette opération.");

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
