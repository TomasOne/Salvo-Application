package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.model.Ship;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.GameRepository;
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

    @RequestMapping("/games")
    public List<Map<String, Object>> getGames()
    {
        List<Game> gameList = gameRepo.findAll();

        Map<String, Game> gameDTO = new LinkedHashMap<>();

        return gameList.stream().map(this::gameDTO).collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{id}")
    private Map<String, Object>  getGamePlayerId(@PathVariable Long id){
        GamePlayer gamePlay = gamePlayRepo.findById(id).get();
        Game game = gamePlay.getGame();
        Map<String, Object> shipDTO = gameDTO(game);
        shipDTO.put("ships", gamePlay.getShips().stream().map(this::shipDTO).collect(Collectors.toList()));
        return shipDTO;
    }

    private Map<String, Object> gameDTO(Game game){
        Map<String, Object> gameDto = new LinkedHashMap<String, Object>();
        gameDto.put("id", game.getId());
        gameDto.put("created", game.getData());
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

}
