package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Image;
import com.example.demo.repository.ImageRepository;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Iterable<Image> getImages() {
        return imageRepository.findAll();
    }

    public Optional<Image> getImageById(Integer id) {
        return imageRepository.findById(id);
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    public void deleteImageById(Integer id) {
        imageRepository.deleteById(id);
    }

}
