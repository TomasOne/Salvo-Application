package com.codeoftheweb.Salvo.DTO;

import com.codeoftheweb.Salvo.Utils.Util;
import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;

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

    public static Map<String, Object> gameDTOAux(GamePlayer gamePlayer){
        Map<String, Object> gameDto = new LinkedHashMap<String, Object>();
        gameDto.put("id", gamePlayer.getGame().getId());
        gameDto.put("created", gamePlayer.getGame().getData());
        gameDto.put("gameState", Util.getState(gamePlayer));
        gameDto.put("gamePlayers", gamePlayer.getGame().getGamePlayer().stream().map(makeGamePlayerDTO::gamePlayerDTO).collect(Collectors.toList()));
        return gameDto;
    }
}
