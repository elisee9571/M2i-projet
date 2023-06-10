package com.example.demo.fixture;

import com.example.demo.entity.User;
import com.example.demo.enums.Roles;
import com.example.demo.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UserFixture {
    private final UserRepository userRepository;
    private final Faker faker;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserFixture(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.faker = new Faker(new Locale("fr-FR"));
        this.passwordEncoder = passwordEncoder;
    }

    public void generateFixtures() {
        for (int i = 0; i < 10; i++) {
            String firstname = faker.name().firstName();
            String lastname = faker.name().lastName();
            String pseudo = faker.name().username();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().cellPhone();
            String password = passwordEncoder.encode("password");
            String biography = faker.lorem().characters(15, 50);
            String address = faker.address().streetAddress();
            String additionalAddress = faker.lorem().characters(5, 10);
            String city = faker.address().city();
            String zipCode = faker.address().zipCode();
            Roles role = getRandomRole();
            String avatar = faker.avatar().image();

            User user = new User(firstname, lastname, pseudo, email, phone, password, biography, address, additionalAddress, city, zipCode, role, avatar);

            userRepository.save(user);
        }
    }

    public Roles getRandomRole(){
        int randomNum = faker.random().nextInt(2);
        return randomNum == 0 ? Roles.ROLE_USER : Roles.ROLE_ADMIN;
    }
}
