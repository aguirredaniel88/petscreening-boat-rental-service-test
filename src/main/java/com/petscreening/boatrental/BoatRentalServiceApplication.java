package com.petscreening.boatrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BoatRentalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoatRentalServiceApplication.class, args);
	}

}
