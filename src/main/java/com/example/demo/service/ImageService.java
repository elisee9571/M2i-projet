package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.entity.Product;
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

    public void saveFiles(Product product) throws IOException {
        if (product.getFileUploads() == null || product.getFileUploads().isEmpty()) throw new IllegalStateException("Fichiers images requis");

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : product.getFileUploads()) {
            if (file.isEmpty()) continue;

            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            String fileName = UUID.randomUUID().toString() + "." + extension;

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image(fileName, file.getContentType(), product);
                imageRepository.save(image);

            } catch (IOException ioe) {
                throw new IOException("Impossible d'enregistrer le fichier image: " + fileName, ioe);
            }
        }
    }


    public void deleteImageById(Integer id) {
        imageRepository.deleteById(id);
    }

}
