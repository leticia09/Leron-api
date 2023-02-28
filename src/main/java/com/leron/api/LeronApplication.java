package com.leron.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
		"com.leron.api.model.entities"
})
@EnableJpaRepositories(basePackages = {
		"com.leron.api.repository"
})
public class LeronApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeronApplication.class, args);
	}

}
