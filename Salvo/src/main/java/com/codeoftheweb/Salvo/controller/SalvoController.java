package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.*;
import com.codeoftheweb.Salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayRepo;

    @Autowired
    private SalvoRepository salvoRepo;

    @Autowired
    private ScoreRepository scoreRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Missing Data"), HttpStatus.FORBIDDEN);
        }

        if (playerRepo.findByUserName(email) !=  null) {
            return new ResponseEntity<>(makeMap("error", "User Already Exist"), HttpStatus.FORBIDDEN);
        }

        playerRepo.save(new Player(email,passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public Player currentPlayer(Authentication authentication){
        return  playerRepo.findByUserName(authentication.getName());
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication)
    {
        List<Game> gameList = gameRepo.findAll();
        Map<String, Object> auxDTO = new LinkedHashMap<>();

        if(!isGuest(authentication)){
            auxDTO.put("player", playerDTO(currentPlayer(authentication)));
        }
        else{
            auxDTO.put("player", "Guest");
        }
        auxDTO.put("games", gameList.stream().map(this::gameDTO).collect(Collectors.toList()));
        return auxDTO;
    }

    @PostMapping("/games")
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

            response = new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }
        else{
            response = new ResponseEntity<>(makeMap("error", "player not authorized"), HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

    @RequestMapping("/game_view/{id}")
    private ResponseEntity<Map<String, Object>>  getGamePlayerId(@PathVariable Long id, Authentication authentication){
        GamePlayer gamePlay = gamePlayRepo.findById(id).get();
        Game game = gamePlay.getGame();
        Player playerAuth = playerRepo.findByUserName(authentication.getName());
        if(gamePlay.getPlayer().getId() == playerAuth.getId()) {
            Map<String, Object> shipDTO = gameDTOAux(game);
            shipDTO.put("ships", gamePlay.getShips().stream().map(this::shipDTO).collect(Collectors.toList()));
            shipDTO.put("salvoes", game.getGamePlayer().stream().flatMap(x -> x.getSalvo().stream().map(this::salvoDTO)).collect(Collectors.toList()));
            shipDTO.put("hits", hitDTO());

            return new ResponseEntity<>(shipDTO, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(makeMap("error", "User Not Authotized"), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/game/{Id}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long Id, Authentication authentication) {

        ResponseEntity<Map<String, Object>> response;
        Optional<Game> game = gameRepo.findById(Id);
        Player player;
        GamePlayer gamePlayer;

        if (isGuest(authentication)) {

            response = new ResponseEntity<>(makeMap("error", "player is unauthorized"), HttpStatus.UNAUTHORIZED);
        } else if (!game.isPresent()) {

            response = new ResponseEntity<>(makeMap("error", "there is no game"), HttpStatus.FORBIDDEN);
        } else if (game.get().getGamePlayer().size() == 2) {

            response = new ResponseEntity<>(makeMap("error", "game is full"), HttpStatus.FORBIDDEN);
        } else {

            player = playerRepo.findByUserName(authentication.getName());
            gamePlayer = gamePlayRepo.save(new GamePlayer(game.get(), player, LocalDateTime.now()));
            response = new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }

        return response;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Map<String, Object> gameDTO(Game game){
        Map<String, Object> gameDto = new LinkedHashMap<String, Object>();
        gameDto.put("id", game.getId());
        gameDto.put("created", game.getData());
        gameDto.put("gamePlayers", game.getGamePlayer().stream().map(this::gamePlayerDTO).collect(Collectors.toList()));
        gameDto.put("scores", game.getGamePlayer().stream().map(x ->{
            if (x.getScore().isPresent()) {
                return scoreDTO(x.getScore().get());
            }
            else {return "partida en progreso";}
            }).collect(Collectors.toList()));
        return gameDto;
    }

    private Map<String, Object> gameDTOAux(Game game){
        Map<String, Object> gameDto = new LinkedHashMap<String, Object>();
        gameDto.put("id", game.getId());
        gameDto.put("created", game.getData());
        gameDto.put("gameState", "PLACESHIPS");
        gameDto.put("gamePlayers", game.getGamePlayer().stream().map(this::gamePlayerDTO).collect(Collectors.toList()));
        return gameDto;
    }


    private Map<String, Object> gamePlayerDTO(GamePlayer gamePlayer)
    {
        Map<String, Object> gamePlayerDTO = new LinkedHashMap<String, Object>();
        gamePlayerDTO.put("id", gamePlayer.getId());
        gamePlayerDTO.put("player", playerDTO(gamePlayer.getPlayer()));
        return  gamePlayerDTO;
    }

    private Map<String, Object> playerDTO(Player player)
    {
        Map<String, Object> playerDTO = new LinkedHashMap<String, Object>();
        playerDTO.put("id", player.getId());
        playerDTO.put("email", player.getUserName());
        return  playerDTO;
    }

    private  Map<String, Object> shipDTO(Ship ship)
    {
        Map<String, Object> shipDTO = new LinkedHashMap<>();
        shipDTO.put("type", ship.getShipType());
        shipDTO.put("locations", ship.getLocation());
        return shipDTO;
    }

    private Map<String, Object> salvoDTO(Salvo salvo)
    {
        Map<String, Object> salvoDTO = new LinkedHashMap<>();
        salvoDTO.put("locations", salvo.getSalvoLocation()) ;
        salvoDTO.put("turn", salvo.getTurnId());
        salvoDTO.put("player", salvo.getGamePlayer().getId());
        return  salvoDTO;
    }

    private Map<String, Object> scoreDTO(Score score)
    {
        Map<String, Object> scoreDTO = new LinkedHashMap<>();
        scoreDTO.put("score", score.getScore());
        scoreDTO.put("date", score.getFinishDate());
        scoreDTO.put("player", score.getPlayer().getId());
        return scoreDTO;
    }

    private Map<String, Object> hitDTO(){
        List<String> selfHitLocations = new ArrayList<>();
        List<String> opponentHitLocations = new ArrayList<>();
        Map<String, Object> hitDTO = new LinkedHashMap<>();
        hitDTO.put("self", selfHitLocations);
        hitDTO.put("opponent", opponentHitLocations);
        return hitDTO;
    }

}
