package msu.ru.webprac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class WebpracApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebpracApplication.class, args);
	}

}
