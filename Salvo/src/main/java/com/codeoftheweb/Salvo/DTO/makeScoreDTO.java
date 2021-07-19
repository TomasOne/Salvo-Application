package com.codeoftheweb.Salvo.DTO;

import com.codeoftheweb.Salvo.model.GamePlayer;

import java.util.LinkedHashMap;
import java.util.Map;

public class makeScoreDTO {

    public static Map<String, Object> scoreDTO(GamePlayer gp)
    {
        Map<String, Object> scoreDTO = new LinkedHashMap<>();
        if(gp.getScore().isPresent()) {
            scoreDTO.put("score", gp.getScore().get().getScore());
            scoreDTO.put("finishDate", gp.getScore().get().getFinishDate());
            scoreDTO.put("player", gp.getPlayer().getId());
        }
        else
        {
            scoreDTO.put("score", null);
            scoreDTO.put("finishDate", "Partida en Curso");
            scoreDTO.put("player", gp.getPlayer().getId());
        }
        return scoreDTO;
    }
}
