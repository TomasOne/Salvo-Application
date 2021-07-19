package com.codeoftheweb.Salvo.DTO;

import com.codeoftheweb.Salvo.model.*;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.Map;

public class makeGamePlayerDTO {

    @Autowired
    private PlayerRepository playerRepo;

    public static Map<String, Object> gamePlayerDTO(GamePlayer gamePlayer)
    {
        Map<String, Object> gamePlayerDTO = new LinkedHashMap<String, Object>();
        gamePlayerDTO.put("id", gamePlayer.getId());
        gamePlayerDTO.put("player", makePlayerDTO.playerDTO(gamePlayer.getPlayer()));
        return  gamePlayerDTO;
    }
}
