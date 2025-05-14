package com.example.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BankingDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingDemoApplication.class, args);
	}

}
