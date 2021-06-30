package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
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
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gameP) {
		return (args) -> {
			//Creación de jugadores y partidas
			Player player2 = new Player("test@gmail.com");
			Player player1 = new Player("test00@gmail.com");
			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			// Agrega datos a nuestro com.codeoftheweb.Salvo.repocitory y se envían a la DB
			repository.save(player1);
			repository.save(player2);
			//Visualizamos el dia y hora exacta en la que se creó el juego usando el LocalTime
			gameRepo.save(game1);
			gameRepo.save(game2);

			gameP.save(new GamePlayer(game1,player1,LocalDateTime.now()));
			gameP.save(new GamePlayer(game1,player2,LocalDateTime.now().plusHours(1)));
		};
	}
}

