package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Rate;
import com.example.demo.repository.RateRepository;

@Service
public class RateService {

    @Autowired
    private RateRepository rateRepository;

    public Iterable<Rate> getRates() {
        return rateRepository.findAll();
    }

    public Optional<Rate> getRateById(Integer id) {
        return rateRepository.findById(id);
    }

    public Rate saveRate(Rate rate) {
        return rateRepository.save(rate);
    }

    public void deleteRateById(Integer id) {
        rateRepository.deleteById(id);
    }

}
