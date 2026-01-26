package com.example.mini_saas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MiniSaasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniSaasApplication.class, args);
	}

}
