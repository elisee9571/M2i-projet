package com.example.demo.fixture;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.enums.Currencies;
import com.example.demo.enums.Status;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

@Component
public class ProductFixture {
    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final Faker faker;

    @Autowired
    public ProductFixture(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.faker = new Faker(new Locale("fr-FR"));
    }

    public void generateFixtures() {
        int min = 0;
        int max = 100;
        DecimalFormat df = new DecimalFormat("0.00");
        float random = min + new Random().nextFloat() * (max - min);
        for (int i = 0; i < 10; i++) {
            String title = faker.name().title();
            String content = faker.gameOfThrones().house();
            Float price = Float.parseFloat(df.format(random));
            Currencies currencyCode = Currencies.EUR;
            Status status = getRandomStatus();
            User user = userRepository.findByEmail("eli@gmail.fr").orElseThrow(()-> new IllegalStateException("Utilisateur introuvable avec l'email: "));;
            Category category = categoryRepository.getById(1);

            Product product = new Product(title, content, price, currencyCode, status, user, category);

            productRepository.save(product);
        }
    }

    public Status getRandomStatus(){
        int randomNum = faker.random().nextInt(2);
        return randomNum == 0 ? Status.VISIBLE : Status.DRAFT;
    }
}
