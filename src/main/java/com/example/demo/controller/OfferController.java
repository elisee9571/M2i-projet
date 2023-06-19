package com.example.demo.controller;

import com.example.demo.jsonView.MyJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Offer;
import com.example.demo.service.OfferService;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private OfferService offerService;
    @Autowired
    public OfferController(OfferService offerService){
        this.offerService = offerService;
    }

    @GetMapping
    @JsonView(MyJsonView.Offer.class)
    public ResponseEntity<?> listOffer() {
        try {
            return new ResponseEntity<>(offerService.getOffers(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{productId}")
    @JsonView(MyJsonView.Offer.class)
    public ResponseEntity<?> offerId(@PathVariable("productId") Integer productId) {
        try {
            return new ResponseEntity<>(offerService.getOfferByProductId(productId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/propose/{productId}")
    @JsonView(MyJsonView.Offer.class)
    public ResponseEntity<?> proposeOffer(@PathVariable("productId") Integer productId, @RequestBody Offer offer) {
        try {
            offerService.proposeOffer(productId, offer);
            return new ResponseEntity<>("Offre proposé", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/acceptOrRejectOffer/{productId}/{offerId}")
    @JsonView(MyJsonView.Offer.class)
    public ResponseEntity<String> acceptOrRejectOffer(
            @PathVariable("productId") Integer productId,
            @PathVariable("offerId") Integer offerId,
            @RequestParam("accept") boolean accept
    ) {
        try {
            offerService.acceptOrRejectOffer(productId, offerId, accept);
            return new ResponseEntity<>("Offre " + (accept ? "acceptée" : "refusée"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}