package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Ad;
import com.example.demo.repository.AdRepository;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    public Iterable<Ad> getAds() {
        return adRepository.findAll();
    }

    public Optional<Ad> getAdById(Integer id) {
        return adRepository.findById(id);
    }

    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }

    public void deleteAdById(Integer id) {
        adRepository.deleteById(id);
    }

}
