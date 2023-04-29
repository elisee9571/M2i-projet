package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Image;
import com.example.demo.repository.ImageRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Value("${uploadDir}")
    private String uploadDir;

    @Autowired
    private ImageRepository imageRepository;

    public Iterable<Image> getImages() {
        return imageRepository.findAll();
    }

    public Optional<Image> getImageById(Integer id) {
        return imageRepository.findById(id);
    }

    public void saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalStateException("file required");

        Path uploadPath = Paths.get(uploadDir);
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            Image image = new Image(fileName, file.getContentType());
            imageRepository.save(image);

        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public void deleteImageById(Integer id) {
        imageRepository.deleteById(id);
    }

}
