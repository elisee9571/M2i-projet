package com.example.demo;

import com.example.demo.fixture.ProductFixture;
import com.example.demo.fixture.UserFixture;
import jakarta.annotation.PostConstruct;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiApplicationTests {

	private final UserFixture userFixture;
	private final ProductFixture productFixture;
	private final MockMvc mockMvc;

	@Autowired
	ApiApplicationTests(UserFixture userFixture, ProductFixture productFixture,MockMvc mockMvc) {
		this.userFixture = userFixture;
		this.productFixture = productFixture;
		this.mockMvc = mockMvc;
	}

	@PostConstruct
	void fixture(){
//		userFixture.generateFixtures();
		productFixture.generateFixtures();
	}

	@Test
	void getUsers() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/users")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void failureLogin() throws Exception {
		String email = "eli@gmail.fr";
		String password = "wrongPassword";

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Identifiant ou mot de passe incorrect"));
	}

//	@Test
//	void successLogin() throws Exception {
//		String email = "eli@gmail.fr";
//		String password = "password";
//
//		mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.not(emptyString())));
//	}
}
