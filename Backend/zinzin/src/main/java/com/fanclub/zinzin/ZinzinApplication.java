package com.fanclub.zinzin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZinzinApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZinzinApplication.class, args);
	}

}
