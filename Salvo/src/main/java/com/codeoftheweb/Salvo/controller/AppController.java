package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.DTO.makeGameDTO;
import com.codeoftheweb.Salvo.DTO.makeSalvoDTO;
import com.codeoftheweb.Salvo.DTO.makeShipDTO;
import com.codeoftheweb.Salvo.Utils.ShipType;
import com.codeoftheweb.Salvo.Utils.Util;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Salvo;
import com.codeoftheweb.Salvo.model.Score;
import com.codeoftheweb.Salvo.model.Ship;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import com.codeoftheweb.Salvo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private GamePlayerRepository gamePlayRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private ScoreRepository scoreRepo;

    @RequestMapping("/game_view/{id}")
    private ResponseEntity<Map<String, Object>> getGamePlayerId(@PathVariable Long id, Authentication authentication) {
        Optional<GamePlayer> gp = gamePlayRepo.findById(id);
        ResponseEntity<Map<String, Object>> response;
        Map<String, Object> aux = new LinkedHashMap<String  , Object>();

        if (gp.isPresent()) { // Si no es null, realizo un llamado al metodo "getMapDTOs" que devuelve un mapa con los DTO.

            if (authentication.getName().compareTo(gp.get().getPlayer().getUserName()) == 0) {

                if(Util.getState(gp.get()) == "WON")
                {
                    if(gp.get().getGame().getScores().size()<2)
                    {
                        Set<Score> scores = new HashSet<>();
                        Score score1 = new Score();
                        score1.setPlayer(gp.get().getPlayer());
                        score1.setGame(gp.get().getGame());
                        score1.setFinishDate(LocalDateTime.now());
                        score1.setScore(1.0f);
                        scoreRepo.save(score1);
                        Score score2 = new Score();
                        score2.setPlayer(gp.get().getPlayer());
                        score2.setGame(gp.get().getGame());
                        score2.setFinishDate(LocalDateTime.now());
                        score2.setScore(0.0f);
                        scoreRepo.save(score2);
                        scores.add(score1);
                        scores.add(score2);
                        gp.get().getGame().setScores(scores);
                    }
                }
                if(Util.getState(gp.get()) == "TIE")
                {
                    if(gp.get().getGame().getScores().size()<2)
                    {
                        Set<Score> scores = new HashSet<>();
                        Score score1 = new Score();
                        score1.setPlayer(gp.get().getPlayer());
                        score1.setGame(gp.get().getGame());
                        score1.setFinishDate(LocalDateTime.now());
                        score1.setScore(0.5f);
                        scoreRepo.save(score1);
                        Score score2 = new Score();
                        score2.setPlayer(gp.get().getPlayer());
                        score2.setGame(gp.get().getGame());
                        score2.setFinishDate(LocalDateTime.now());
                        score2.setScore(0.5f);
                        scoreRepo.save(score2);
                        scores.add(score1);
                        scores.add(score2);
                        gp.get().getGame().setScores(scores);
                    }
                }


                response = new ResponseEntity<>(getMapDTOs(id), HttpStatus.OK);
            } else {

                aux.put("ERROR", "Player not authorized");
                response = new ResponseEntity<>(aux, HttpStatus.UNAUTHORIZED);
            }

        } else {

            aux.put("ERROR", "gamePlayer does not exist");
            response = new ResponseEntity<>(aux, HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

    private List<Map<String, Object>> hitsAndSinks(GamePlayer self, GamePlayer opponent) {

        List<Map<String, Object>> dtoSupremo = new LinkedList<>();
        int[] totalDamages = new int[5];

        Integer patrolBoatDMG = 0, destroyerDMG = 0, submarineDMG = 0, carrierDMG = 0, battleShipDMG = 0;

        List<String> patrolBoatLocation = findShipLocations(self, ShipType.patrolboat);
        List<String> destroyerLocation = findShipLocations(self, ShipType.destroyer);
        List<String> submarineLocation = findShipLocations(self, ShipType.submarine);
        List<String> battleShipLocation = findShipLocations(self, ShipType.battleship);
        List<String> carrierLocation = findShipLocations(self, ShipType.carrier);

        for (Salvo salvo : opponent.getSalvo()) {
            Map<String, Object> dtoHit = new LinkedHashMap<>();
            Map<String, Object> damage = new LinkedHashMap<>();
            ArrayList<String> hitCellList = new ArrayList<>();
            //int[] hits = new int[5];
            int missedShots = salvo.getSalvoLocations().size();

            long patrolBoatTurn = 0, destroyerTurn = 0, submarineTurn = 0, carrierTurn = 0, battleShipTurn = 0;

            for (String location : salvo.getSalvoLocations()) {

                if (carrierLocation.contains(location)) {
                    //totalDamages[0]++;
                    carrierDMG++;
                    //hits[0]++;
                    carrierTurn++;
                    hitCellList.add(location);
                    missedShots--;
                }
                if (battleShipLocation.contains(location)) {

                    //totalDamages[1]++;
                    battleShipDMG++;
                    //hits[1]++;
                    battleShipTurn++;
                    hitCellList.add(location);
                    missedShots--;
                }
                if (destroyerLocation.contains(location)) {

                    //totalDamages[2]++;
                    destroyerDMG++;
                    //hits[2]++;
                    destroyerTurn++;
                    hitCellList.add(location);
                    missedShots--;
                }
                if (submarineLocation.contains(location)) {

                    //totalDamages[3]++;
                    submarineDMG++;
                    //hits[3]++;
                    submarineTurn++;
                    hitCellList.add(location);
                    missedShots--;
                }
                if (patrolBoatLocation.contains(location)) {

                    //totalDamages[4]++;
                    patrolBoatDMG++;
                    //hits[4]++;
                    patrolBoatTurn++;
                    hitCellList.add(location);
                    missedShots--;
                }
            }

            /*damage.put("carrierHits", hits[0]);
            damage.put("battleshipHits", hits[1]);
            damage.put("destroyerHits", hits[2]);
            damage.put("submarineHits", hits[3]);
            damage.put("patrolboatHits", hits[4]);*/
            damage.put("carrierHits", carrierTurn);
            damage.put("battleshipHits", battleShipTurn);
            damage.put("destroyerHits", destroyerTurn);
            damage.put("submarineHits", submarineTurn);
            damage.put("patrolboatHits", patrolBoatTurn);
/*            damage.put("carrier", totalDamages[0]);
            damage.put("battleship", totalDamages[1]);
            damage.put("destroyer", totalDamages[2]);
            damage.put("submarine", totalDamages[3]);
            damage.put("patrolboat", totalDamages[4]);*/
            damage.put("carrier", carrierDMG);
            damage.put("battleship", battleShipDMG);
            damage.put("destroyer", destroyerDMG);
            damage.put("submarine", submarineDMG);
            damage.put("patrolboat", patrolBoatDMG);

            dtoHit.put("turn", salvo.getTurnId());
            dtoHit.put("hitLocations", hitCellList);
            dtoHit.put("damages", damage);
            dtoHit.put("missed", missedShots);

            dtoSupremo.add(dtoHit);
        }

        return dtoSupremo;
    }

    private  Map<String, Object> getMapDTOs(Long gamePlayerId) {

        // Trabajo los DTO desde GamePlayer
        // Desde game puedo utilizar el gameDTO que tiene acceso a los demás DTO
        Map<String, Object> data = makeGameDTO.gameDTOAux(gamePlayRepo.getById(gamePlayerId));
        Map<String, Object> hits = new LinkedHashMap<>();

        data.put("ships", gamePlayRepo.getById(gamePlayerId).getShips().stream().map(makeShipDTO::shipDTO).collect(Collectors.toList()));

        // Utilizando el GP consigo la ID de un jugasdor, luego su oponente y comienzo a cargar los datos de c/u
        data.put("salvoes", gamePlayRepo.getById(gamePlayerId).getGame().getGamePlayer().stream().flatMap(player -> player.getSalvo().stream().map(makeSalvoDTO::salvoDTO)).collect(Collectors.toList()));

        Optional<GamePlayer> gamePlayer = gamePlayRepo.findById(gamePlayerId);
        Optional<GamePlayer> opponent = gamePlayer.get().getGame().getGamePlayer().stream().filter(gp -> gp != gamePlayer.get()).findFirst();
        if (opponent.isPresent()) {

            hits.put("self", hitsAndSinks(gamePlayer.get(), opponent.get()));
            hits.put("opponent", hitsAndSinks(opponent.get(), gamePlayer.get()));
        } else {

            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
        }

        data.put("hits", hits);

        return data;
    }

    public List<String> findShipLocations(GamePlayer self, ShipType type) {

        Ship response;
        response = self.getShips().stream().filter(ship -> ship.getType().equals(type.name())).findFirst().orElse(null);
        if (response == null) {
            return new ArrayList<>();
        } else {
            return response.getShipLocations();
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

