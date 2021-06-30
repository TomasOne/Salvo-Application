package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.GameRepository;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean//Palabra reservada para JavaBean. Se utiliza para suministrar datos a una clase sin que la clase tenga que crearlos
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo) {
		return (args) -> {
			// Agrega datos a nuestro com.codeoftheweb.Salvo.repocitory y se envían a la DB
			repository.save(new Player("testmail@test.com"));
			repository.save(new Player("testmail1@test.com"));
			//Visualizamos el dia y hora exacta en la que se creó el juego usando el LocalTime
			gameRepo.save(new Game(LocalDateTime.now()));
			gameRepo.save(new Game(LocalDateTime.now().plusHours(1)));
		};
	}
}

