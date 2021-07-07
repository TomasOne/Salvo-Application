package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.*;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.GameRepository;
import com.codeoftheweb.Salvo.repository.SalvoRepository;
import com.codeoftheweb.Salvo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @RequestMapping("/games")
    public Map<String, Object> getGames()
    {
        List<Game> gameList = gameRepo.findAll();
        Map<String, Object> auxDTO = new LinkedHashMap<>();
        auxDTO.put("games", gameList.stream().map(this::gameDTO).collect(Collectors.toList()));
        return auxDTO;
    }

    @RequestMapping("/game_view/{id}")
    private Map<String, Object>  getGamePlayerId(@PathVariable Long id){
        GamePlayer gamePlay = gamePlayRepo.findById(id).get();
        Game game = gamePlay.getGame();
        Map<String, Object> shipDTO = gameDTO(game);
        shipDTO.put("ships", gamePlay.getShips().stream().map(this::shipDTO).collect(Collectors.toList()));
        shipDTO.put("salvoes", game.getGamePlayer().stream().flatMap(x -> x.getSalvo().stream().map(this::salvoDTO)).collect(Collectors.toList()));
        return shipDTO;
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

}
