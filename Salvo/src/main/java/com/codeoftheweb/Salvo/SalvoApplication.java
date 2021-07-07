package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.model.*;
import com.codeoftheweb.Salvo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean//Palabra reservada para JavaBean. Hace una anotación del Hibernate y mapea las entidades. Es la conexión entre las clases y la base de datos.
		// Incluye un metodo en el entorno de la aplicación
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gameP_repo, ShipRepository shipRepo, SalvoRepository salvoRepo, ScoreRepository scoreRepo) {
		return (args) -> {
			//Creación de jugadores y partidas
			Player player2 = new Player("test@gmail.com");
			Player player1 = new Player("test00@gmail.com");
			Player player3 = new Player("prueba3");
			Player player4 = new Player("prueba4");
			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			GamePlayer gamePlayer1 = new GamePlayer(game1,player1,LocalDateTime.now());
			GamePlayer gamePlayer2 = new GamePlayer(game1,player2,LocalDateTime.now());
			GamePlayer gamePlayer3 = new GamePlayer(game2,player3,LocalDateTime.now());
			GamePlayer gamePlayer4 = new GamePlayer(game2,player4,LocalDateTime.now());
			Ship ship1 = new Ship(gamePlayer1,"Destroyer", List.of("H2","H3","H4"));
			Ship ship2 = new Ship(gamePlayer1,"Submarine", List.of("E1","F1","G1"));
			Ship ship3 = new Ship(gamePlayer1,"Patrol Boat", List.of("B5","B4"));
			Ship ship4 = new Ship(gamePlayer2,"Destroyer", List.of("B5","C5","D5"));
			Ship ship5 = new Ship(gamePlayer2,"Patrol Boat", List.of("F1","F2"));
			Salvo salvo1 = new Salvo(gamePlayer1,List.of("B5","C5","F1"),1);
			Salvo salvo2 = new Salvo(gamePlayer1,List.of("F5","D5"),2);
			Salvo salvo3 = new Salvo(gamePlayer2,List.of("B4","B5","B6"),1);
			Salvo salvo4 = new Salvo(gamePlayer2,List.of("E1","H3","A2"),2);
			Score score1 = new Score(game1, player1, LocalDateTime.now(), 1);
			Score score2 = new Score(game1, player2, LocalDateTime.now(), 0.5f);
			Score score3 = new Score(game2, player3, LocalDateTime.now(), 0);
			Score score4 = new Score(game2, player4, LocalDateTime.now(), 1.5f);
			// Agrega datos a nuestro repository y se envían a la DB
			repository.save(player1);
			repository.save(player2);
			repository.save(player3);
			repository.save(player4);
			gameRepo.save(game1);
			gameRepo.save(game2);
			gameP_repo.save(gamePlayer1);
			gameP_repo.save(gamePlayer2);
			shipRepo.save(ship1);
			shipRepo.save(ship2);
			shipRepo.save(ship3);
			shipRepo.save(ship4);
			shipRepo.save(ship5);
			salvoRepo.save(salvo1);
			salvoRepo.save(salvo2);
			salvoRepo.save(salvo3);
			salvoRepo.save(salvo4);
			scoreRepo.save(score1);
			scoreRepo.save(score2);
			scoreRepo.save(score3);
			scoreRepo.save(score4);
		};
	}
}

