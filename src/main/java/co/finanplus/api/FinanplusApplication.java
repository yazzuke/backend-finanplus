package co.finanplus.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinanplusApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanplusApplication.class, args);
	}

}
