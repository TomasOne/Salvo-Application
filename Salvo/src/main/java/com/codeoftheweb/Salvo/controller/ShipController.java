package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.Utils.Util;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.model.Ship;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import com.codeoftheweb.Salvo.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    private GamePlayerRepository gamePlayRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    ShipRepository shipRepository;

    @PostMapping("games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable Long gamePlayerId, @RequestBody Set<Ship> ships, Authentication authentication ) {

        ResponseEntity<Map<String, Object>> response;
        Optional<GamePlayer> gp = gamePlayRepo.findById(gamePlayerId);
        Player currentPlayer = playerRepo.findByUserName(authentication.getName());

        if (Util.isGuest(authentication)) {

            response = new ResponseEntity<>(Util.makeMap("error", "no player logged in"), HttpStatus.UNAUTHORIZED);
        } else if (!gp.isPresent()) {

            response = new ResponseEntity<>(Util.makeMap("error", "Game Player ID doesn't exist"), HttpStatus.UNAUTHORIZED);
        } else if (gp.get().getPlayer().getId() != currentPlayer.getId()) {

            response = new ResponseEntity<>(Util.makeMap("error", "the current user is not the game player the ID references"), HttpStatus.UNAUTHORIZED);
        } else if (gp.get().getShips().size() > 0) {

            response = new ResponseEntity<>(Util.makeMap("error", "user already has ships placed"), HttpStatus.FORBIDDEN);

        }
        else {
            if (ships.size() > 0 )
            {
                for (Ship ship:ships){
                    shipRepository.save(new Ship(gp.get(), ship.getType(), ship.getShipLocations()));
                }

                response = new ResponseEntity<>(Util.makeMap("OK", "success"), HttpStatus.CREATED);
            }
            else
            {
                response = new ResponseEntity<>(Util.makeMap("error", "No ship sended"), HttpStatus.FORBIDDEN);
            }
        }
        return  response;
    }



}
