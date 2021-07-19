package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.Utils.Util;
import com.codeoftheweb.Salvo.model.*;
import com.codeoftheweb.Salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayRepo;

    @Autowired
    private SalvoRepository salvoRepo;

    @Autowired
    private ScoreRepository scoreRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ShipRepository shipRepository;

    @PostMapping("games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String, Object>> placeSalvoes(@PathVariable Long gamePlayerId, @RequestBody Salvo salvo, Authentication authentication) {

        ResponseEntity<Map<String, Object>> response = null;
        Optional<GamePlayer> gp = gamePlayRepo.findById(gamePlayerId);
        Player currentPlayer = playerRepo.findByUserName(authentication.getName());

        if (Util.isGuest(authentication)) {

            response = new ResponseEntity<>(Util.makeMap("error", "no player logged in"), HttpStatus.UNAUTHORIZED);
        } else if (gp.isEmpty()) {

            response = new ResponseEntity<>(Util.makeMap("error", "Game Player not found"), HttpStatus.UNAUTHORIZED);
        } else if (gp.get().getPlayer().getId() != currentPlayer.getId()) {

            response = new ResponseEntity<>(Util.makeMap("error", "the current user is not the game player the ID references"), HttpStatus.UNAUTHORIZED);
        } else {
            Optional<GamePlayer> opponent = gp.get().getGame().getGamePlayer().stream().filter(gamePlayer -> gamePlayer.getId() != gp.get().getId()).findFirst();

            if (opponent.isPresent()) {
                if (salvo.getSalvoLocations().size() == 0) {
                    response = new ResponseEntity<>(Util.makeMap("error", "You must do at least 1 shot"), HttpStatus.FORBIDDEN);
                } else if (salvo.getSalvoLocations().size() >= 1) {
                    if (gp.get().getSalvo().size() != opponent.get().getSalvo().size() && gp.get().getSalvo().size() > opponent.get().getSalvo().size()) {

                        response = new ResponseEntity<>(Util.makeMap("OK", "It's not your turn"), HttpStatus.FORBIDDEN);
                    } else {
                        salvoRepo.save(new Salvo(gp.get(), salvo.getSalvoLocations(), gp.get().getSalvo().size() + 1));
                        response = new ResponseEntity<>(Util.makeMap("OK", "Shots fired"), HttpStatus.CREATED);
                    }
                } else {
                    response = new ResponseEntity<>(Util.makeMap("error", "There's no opponent"), HttpStatus.FORBIDDEN);
                }
            }
        }
        return response;
    }
}
