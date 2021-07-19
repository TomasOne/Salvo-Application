package com.codeoftheweb.Salvo.DTO;

import com.codeoftheweb.Salvo.model.Ship;

import java.util.LinkedHashMap;
import java.util.Map;

public class makeShipDTO {

    public static Map<String, Object> shipDTO(Ship ship)
    {
        Map<String, Object> shipDTO = new LinkedHashMap<>();
        shipDTO.put("type", ship.getType());
        shipDTO.put("locations", ship.getShipLocations());
        return shipDTO;
    }
}
