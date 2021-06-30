package com.codeoftheweb.Salvo;

import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @RequestMapping("/games")
    public List<Map<String, Object>> getGames()
    {
        List<Game> gameList = gameRepo.findAll();

        Map<String, Game> gameDTO = new LinkedHashMap<>();

        return gameList.stream().map(this::gameDTO).collect(Collectors.toList());
    }

    private Map<String, Object> gameDTO(Game game){
        Map<String, Object> dto= new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getData());
        dto.put("gamePlayers", game.getGamePlayer().stream().map(this::gamePlayerDTO).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> gamePlayerDTO(GamePlayer gamePlayer)
    {
        Map<String, Object> gamePlayerDto = new LinkedHashMap<String, Object>();
        gamePlayerDto.put("id", gamePlayer.getId());
        gamePlayerDto.put("player", playerDTO(gamePlayer.getPlayer()));
        return  gamePlayerDto;
    }

    private Map<String, Object> playerDTO(Player player)
    {
        Map<String, Object> playerDTO = new LinkedHashMap<String, Object>();
        playerDTO.put("id", player.getId());
        playerDTO.put("email", player.getUserName());
        return  playerDTO;
    }

}
