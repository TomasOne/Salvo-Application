package com.codeoftheweb.Salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean//Palabra reservada para JavaBean. Se utiliza para suministrar datos a una clase sin que la clase tenga que crearlos
	public CommandLineRunner initData(PlayerRepocitory repository) {
		return (args) -> {
			// Agrega datos a nuestro repocitory y se envían a la DB
			repository.save(new Player("testmail@test.com"));
		};
	}
}

