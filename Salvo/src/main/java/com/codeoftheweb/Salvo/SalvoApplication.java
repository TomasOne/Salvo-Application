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

	@Bean//Palabra reservada para JavaBean. Se utiliza para suministrar datos a una clase sin que la clase tenga que crearlos
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gameP_repo, ShipRepository shipRepo, SalvoRepository salvoRepo) {
		return (args) -> {
			//Creación de jugadores y partidas
			Player player2 = new Player("test@gmail.com");
			Player player1 = new Player("test00@gmail.com");
			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			GamePlayer gamePlayer1 = new GamePlayer(game1,player1,LocalDateTime.now());
			GamePlayer gamePlayer2 = new GamePlayer(game1,player2,LocalDateTime.now());
			Ship ship1 = new Ship(gamePlayer1,"Destroyer", List.of("A1","A2","A3"));
			Ship ship2 = new Ship(gamePlayer1,"Submarine", List.of("F1","G1","H1"));
			Ship ship3 = new Ship(gamePlayer1,"Battleship	", List.of("J7","J8","J9","J10"));
			Salvo salvo1 = new Salvo(gamePlayer1,List.of("A1","C3","D7"),1);
			Salvo salvo2 = new Salvo(gamePlayer1,List.of("G1","G3","D6"),2);
			Salvo salvo3 = new Salvo(gamePlayer2,List.of("A1","A2","A3"),1);
			Salvo salvo4 = new Salvo(gamePlayer2,List.of("B4","C5","D6"),2);
			// Agrega datos a nuestro repository y se envían a la DB
			repository.save(player1);
			repository.save(player2);
			gameRepo.save(game1);
			gameRepo.save(game2);
			gameP_repo.save(gamePlayer1);
			gameP_repo.save(gamePlayer2);
			shipRepo.save(ship1);
			shipRepo.save(ship2);
			shipRepo.save(ship3);
			salvoRepo.save(salvo1);
			salvoRepo.save(salvo2);
			salvoRepo.save(salvo3);
			salvoRepo.save(salvo4);
		};
	}
}

