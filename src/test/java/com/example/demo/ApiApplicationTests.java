package com.example.demo;

import com.example.demo.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiApplicationTests {

	private final UserFixture userFixture;

	@Autowired
	public ApiApplicationTests(UserFixture userFixture) {
		this.userFixture = userFixture;
	}

	@Test
	void contextLoads() {
		userFixture.generateFixtures();
	}

}
