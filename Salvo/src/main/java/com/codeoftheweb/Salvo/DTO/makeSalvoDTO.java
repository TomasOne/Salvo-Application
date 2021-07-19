package com.codeoftheweb.Salvo.DTO;

import com.codeoftheweb.Salvo.model.Salvo;

import java.util.LinkedHashMap;
import java.util.Map;

public class makeSalvoDTO {

    public static Map<String, Object> salvoDTO(Salvo salvo)
    {
        Map<String, Object> salvoDTO = new LinkedHashMap<>();
        salvoDTO.put("locations", salvo.getSalvoLocations()) ;
        salvoDTO.put("turn", salvo.getTurnId());
        salvoDTO.put("player", salvo.getGamePlayer().getId());
        return  salvoDTO;
    }
}
