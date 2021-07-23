package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.DTO.makeGameDTO;
import com.codeoftheweb.Salvo.DTO.makePlayerDTO;
import com.codeoftheweb.Salvo.Utils.Util;
import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.GameRepository;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {


    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayRepo;

    @Autowired
    private PlayerRepository playerRepo;

    public boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication)
    {
        List<Game> gameList = gameRepo.findAll();
        Map<String, Object> playerFounded = new LinkedHashMap<>();

        if(!isGuest(authentication)){
            playerFounded.put("player",  makePlayerDTO.playerDTO(playerRepo.findByUserName(authentication.getName())));
        }
        else{
            playerFounded.put("player", "Guest");
        }
        playerFounded.put("games", gameList.stream().map(makeGameDTO::gameDTO).collect(Collectors.toList()));
        return playerFounded;
    }

    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> creategames(Authentication authentication)
    {
        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "There is no current user logged in"), HttpStatus.FORBIDDEN);
        }

        Player player = playerRepo.findByUserName(authentication.getName());

        if (player == null) {
            return new ResponseEntity<>(Util.makeMap("error", "This player is not logged in"), HttpStatus.FORBIDDEN);
        }

        Game game = new Game(LocalDateTime.now());
        gameRepo.save(game);
        GamePlayer gamePlayer = new GamePlayer(game, player, LocalDateTime.now());
        gamePlayRepo.save(gamePlayer);

        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }

    /*@PostMapping("/games")
    public ResponseEntity<Map<String, Object>> creategames(Authentication authentication)
    {
        ResponseEntity<Map<String, Object>> response;
        Game game;
        Player player;
        GamePlayer gamePlayer;

        if(!isGuest(authentication)) {
            game = gameRepo.save(new Game(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"))));
            player = playerRepo.findByUserName(authentication.getName());
            gamePlayer = gamePlayRepo.save(new GamePlayer(game, player, LocalDateTime.now()));

            response = new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }
        else{
            response = new ResponseEntity<>(Util.makeMap("error", "player not authorized"), HttpStatus.UNAUTHORIZED);
        }
        return response;
    }*/



    @PostMapping("/game/{Id}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long Id, Authentication authentication) {

        ResponseEntity<Map<String, Object>> response;
        Optional<Game> game = gameRepo.findById(Id);
        Player player;
        GamePlayer gamePlayer;

        if (isGuest(authentication)) {

            response = new ResponseEntity<>(Util.makeMap("error", "player is unauthorized"), HttpStatus.UNAUTHORIZED);
        } else if (!game.isPresent()) {

            response = new ResponseEntity<>(Util.makeMap("error", "there is no game"), HttpStatus.FORBIDDEN);
        } else if (game.get().getGamePlayer().size() == 2) {

            response = new ResponseEntity<>(Util.makeMap("error", "game is full"), HttpStatus.FORBIDDEN);
        } else {

            player = playerRepo.findByUserName(authentication.getName());
            gamePlayer = gamePlayRepo.save(new GamePlayer(game.get(), player, LocalDateTime.now()));
            response = new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }

        return response;
    }

}
