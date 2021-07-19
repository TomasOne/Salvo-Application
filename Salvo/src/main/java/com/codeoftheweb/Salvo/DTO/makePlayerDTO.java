package com.codeoftheweb.Salvo.DTO;

import com.codeoftheweb.Salvo.model.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class makePlayerDTO {

    public static Map<String, Object> playerDTO(Player player)
    {
        Map<String, Object> playerDTO = new LinkedHashMap<String, Object>();
        playerDTO.put("id", player.getId());
        playerDTO.put("email", player.getUserName());
        return  playerDTO;
    }
}
