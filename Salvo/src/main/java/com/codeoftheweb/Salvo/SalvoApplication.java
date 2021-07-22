package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.model.*;
import com.codeoftheweb.Salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	@Autowired
	PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);


	}


	@Bean
	//Palabra reservada para JavaBean. Hace una anotación del Hibernate y mapea las entidades. Es la conexión entre las clases y la base de datos.
	// Incluye un metodo en el entorno de la aplicación
	public CommandLineRunner initData(PlayerRepository repository, GameRepository gameRepo, GamePlayerRepository gameP_repo, ShipRepository shipRepo, SalvoRepository salvoRepo, ScoreRepository scoreRepo) {
		return (args) -> {
			//Creaciónes
			Player player2 = new Player("test@gmail.com", passwordEncoder.encode("calabaza123"));
			Player player1 = new Player("test00@gmail.com", passwordEncoder.encode("salamandra"));
			Player player3 = new Player("prueba3", passwordEncoder.encode("cafetera"));
			Player player4 = new Player("prueba4", passwordEncoder.encode("contraseña"));
			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			GamePlayer gamePlayer1 = new GamePlayer(game1, player1, LocalDateTime.now());
			GamePlayer gamePlayer2 = new GamePlayer(game1, player2, LocalDateTime.now());
			GamePlayer gamePlayer3 = new GamePlayer(game2, player3, LocalDateTime.now());
			GamePlayer gamePlayer4 = new GamePlayer(game2, player4, LocalDateTime.now());
			Ship ship1 = new Ship(gamePlayer1, "destroyer", List.of("H2", "H3", "H4"));
			Ship ship2 = new Ship(gamePlayer1, "submarine", List.of("E1", "F1", "G1"));
			Ship ship3 = new Ship(gamePlayer1, "patrolboat", List.of("B5", "B4"));
			Ship ship6 = new Ship(gamePlayer1, "carrier", List.of("J1", "J2", "J3", "J4", "J5"));
			Ship ship7 = new Ship(gamePlayer1, "battleship", List.of("A5", "A6", "A7", "A8"));
			Ship ship4 = new Ship(gamePlayer2, "destroyer", List.of("B5", "C5", "D5"));
			Ship ship5 = new Ship(gamePlayer2, "patrolboat", List.of("F1", "F2"));
			Ship ship8 = new Ship(gamePlayer2, "submarine", List.of("J1", "J2", "J3"));
			Ship ship9 = new Ship(gamePlayer2, "battleship", List.of("A1", "A2", "A3", "A4"));
			Ship ship10 = new Ship(gamePlayer2, "carrier", List.of("G1", "G2", "G3", "G4", "G5"));
			Salvo salvo1 = new Salvo(gamePlayer1, List.of("B5", "C5", "F1"), 1);
			Salvo salvo2 = new Salvo(gamePlayer1, List.of("F5", "D5"), 2);
			Salvo salvo3 = new Salvo(gamePlayer2, List.of("B4", "B5", "B6"), 1);
			Salvo salvo4 = new Salvo(gamePlayer2, List.of("E1", "H3", "A2"), 2);
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
			shipRepo.save(ship6);
			shipRepo.save(ship7);
			shipRepo.save(ship8);
			shipRepo.save(ship9);
			shipRepo.save(ship10);
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

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}

	@Configuration
	class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		PlayerRepository playerRepository;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userName -> {
				Player player = playerRepository.findByUserName(userName);
				if (player != null) {
					return new User(player.getUserName(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + userName);
				}
			});
		}
	}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/web/**").permitAll()
				.antMatchers("/api/games", "/api/game_view/**").permitAll()
				.antMatchers(HttpMethod.POST, "/api/players").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN")
				.antMatchers("/**").hasAuthority("USER");

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}


