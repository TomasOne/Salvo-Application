package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.DTO.*;
import com.codeoftheweb.Salvo.Utils.Util;
import com.codeoftheweb.Salvo.model.*;
import com.codeoftheweb.Salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private GamePlayerRepository gamePlayRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @RequestMapping("/game_view/{id}")
    private ResponseEntity<Map<String, Object>> getGamePlayerId(@PathVariable Long id, Authentication authentication){
        GamePlayer gamePlay = gamePlayRepo.findById(id).get();
        Game game = gamePlay.getGame();
        Player playerAuth = playerRepo.findByUserName(authentication.getName());
        if(gamePlay.getPlayer().getId() == playerAuth.getId()) {
            Map<String, Object> gameAux =  makeGameDTO.gameDTOAux(game);
            gameAux.put("ships", gamePlay.getShips().stream().map(makeShipDTO::shipDTO).collect(Collectors.toList()));
            gameAux.put("salvoes", game.getGamePlayer().stream().flatMap(x -> x.getSalvo().stream().map(makeSalvoDTO::salvoDTO)).collect(Collectors.toList()));
            gameAux.put("hits", hitDTO());

            return new ResponseEntity<>(gameAux, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(Util.makeMap("error", "User Not Authotized"), HttpStatus.UNAUTHORIZED);
        }
    }

    private Map<String, Object> hitDTO(){
        List<String> selfHitLocations = new ArrayList<>();
        List<String> opponentHitLocations = new ArrayList<>();
        Map<String, Object> hitDTO = new LinkedHashMap<>();
        hitDTO.put("self", selfHitLocations);
        hitDTO.put("opponent", opponentHitLocations);
        return hitDTO;
    }

}
