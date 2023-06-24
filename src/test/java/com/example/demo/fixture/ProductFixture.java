package com.example.demo.fixture;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.enums.Currencies;
import com.example.demo.enums.Roles;
import com.example.demo.enums.Status;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
public class ProductFixture {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final Faker faker;

    @Autowired
    public ProductFixture(ProductRepository productRepository, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.faker = new Faker(new Locale("fr-FR"));
    }

    public void generateFixtures() {
        for (int i = 0; i < 300; i++) {
            String title = faker.name().title();
            String content = faker.lorem().sentence();
            Float price = (float) faker.number().randomDouble(2, 0, 1000);
            Currencies currencyCode = Currencies.EUR;
            Status status = getRandomStatus();
            User user = getRandomUser();
            Category category = getRandomCategory();

            Product product = new Product(title, content, price, currencyCode , status, user, category);

            productRepository.save(product);
        }
    }

    public Status getRandomStatus() {
        int randomNum = faker.random().nextInt(4);
        switch (randomNum) {
            case 0:
                return Status.VISIBLE;
            case 1:
                return Status.DRAFT;
            case 2:
                return Status.SOLD;
            case 3:
                return Status.HIDE;
            default:
                throw new IllegalStateException("Statut aléatoire non pris en charge");
        }
    }

    public User getRandomUser() {
        List<User> allUsers = userService.getUsers();

        // Générer un index aléatoire pour sélectionner un utilisateur de la liste
        Random random = new Random();
        int randomIndex = random.nextInt(allUsers.size());

        // Retourner l'utilisateur correspondant à l'index aléatoire
        return allUsers.get(randomIndex);
    }

    public Category getRandomCategory() {
        List<Category> allCategories = categoryService.getCategories();
        Random random = new Random();
        int randomIndex = random.nextInt(allCategories.size());

        // Retourner l'utilisateur correspondant à l'index aléatoire
        return allCategories.get(randomIndex);

    }

}
