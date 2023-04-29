package com.example.demo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Ad;
import com.example.demo.repository.AdRepository;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Ad> getAds() {
        return adRepository.findAll();
    }

    public Ad getAdById(Integer id) {
        return adRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found ad with id: " + id));
    }

    public void saveAd(Ad ad) {
        Integer userId = ad.getUser().getId();
        Integer categoryId = ad.getCategory().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalStateException("Not found user with id: " + userId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new IllegalStateException("Not found category with id: " + categoryId));

        ad.setUser(user);
        ad.setCategory(category);

        adRepository.save(ad);
    }

    public void updateAd(Ad data, Integer id){
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Not found ad with id: " + id));

        if (data.getTitle() != null && data.getTitle().length() > 0 && !Objects.equals(ad.getTitle(), data.getTitle())){
            ad.setTitle(data.getTitle());
        }

        if (data.getContent() != null && data.getContent().length() > 0 && !Objects.equals(ad.getContent(), data.getContent())){
            ad.setContent(data.getContent());
        }

        if (data.getPrice() != null && !Objects.equals(ad.getPrice(), data.getPrice())){
            ad.setPrice(data.getPrice());
        }

        adRepository.save(data);
    }

    public void deleteAd(Integer id) {
        boolean isExist = adRepository.existsById(id);

        if (!isExist){
            throw new IllegalStateException("Ad not found");
        }

        adRepository.deleteById(id);
    }
}
