package com.dusan.villa_sorrento_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:villa_sorrento_test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class VillaSorrentoRentARoomApplicationTests {

	@Test
	void contextLoads() {
	}

}
