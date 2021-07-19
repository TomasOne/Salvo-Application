package com.codeoftheweb.Salvo.DTO;

import com.codeoftheweb.Salvo.model.Game;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class makeGameDTO {


    public static Map<String, Object> gameDTO(Game game){

        Map<String, Object> gameDto = new LinkedHashMap<>();
        gameDto.put("id", game.getId());
        gameDto.put("created", game.getData());
        gameDto.put("gamePlayers", game.getGamePlayer().stream().map(makeGamePlayerDTO::gamePlayerDTO).collect(Collectors.toList()));
        gameDto.put("scores", game.getGamePlayer().stream().map(makeScoreDTO::scoreDTO).collect(Collectors.toList()));
        return gameDto;
    }

    public static Map<String, Object> gameDTOAux(Game game){
        Map<String, Object> gameDto = new LinkedHashMap<String, Object>();
        gameDto.put("id", game.getId());
        gameDto.put("created", game.getData());
        gameDto.put("gameState", "PLACESHIPS");
        gameDto.put("gamePlayers", game.getGamePlayer().stream().map(makeGamePlayerDTO::gamePlayerDTO).collect(Collectors.toList()));
        return gameDto;
    }
}
