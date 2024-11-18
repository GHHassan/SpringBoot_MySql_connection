package com.example.sqlconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SqlconnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlconnectApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder ()  {
		return new BCryptPasswordEncoder();
	}

}
