package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Offer;
import com.example.demo.repository.OfferRepository;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    public Iterable<Offer> getOffers() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getOfferById(Integer id) {
        return offerRepository.findById(id);
    }

    public Offer saveOffer(Offer offer) {
        return offerRepository.save(offer);
    }

}
