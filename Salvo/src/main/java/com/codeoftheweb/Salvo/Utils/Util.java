package com.codeoftheweb.Salvo.Utils;


import com.codeoftheweb.Salvo.model.GamePlayer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class Util {

    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public static Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static String getState(GamePlayer self) {

        Optional<GamePlayer> opponent = self.getGame().getGamePlayer().stream().filter(gp -> gp.getId() != self.getId()).findFirst();

        if (self.getShips().size() == 0) {
            return GameState.PLACESHIPS.name();
        }
        if (opponent.isEmpty()) {

            return GameState.WAITINGFOROPP.name();
        }
        if (opponent.get().getShips().isEmpty()) {

            return GameState.WAIT.name();
        }
        if (self.getSalvo().size() < opponent.get().getSalvo().size()) {

            return GameState.PLAY.name();
        }

        if (self.getSalvo().size() > opponent.get().getSalvo().size()) {

            return GameState.WAIT.name();
        }
        if (self.getSalvo().size() == opponent.get().getSalvo().size()) {

            boolean selfLost = getIfAllSunk(self, opponent.get());
            boolean opponentLost = getIfAllSunk(opponent.get(), self);

            if(selfLost && opponentLost)
            {
                return GameState.TIE.name();
            }

            if (!selfLost && opponentLost)
            {
                return  GameState.WON.name();
            }

            if (!opponentLost && selfLost)
            {
                return GameState.LOST.name();
            }

            return GameState.PLAY.name();
        }
        return GameState.UNDEFINED.name();
    }

    public static boolean getIfAllSunk(GamePlayer self, GamePlayer opponent) {//compruebo si todos los barcos fueron hundidos


        if (!opponent.getShips().isEmpty() && !self.getSalvo().isEmpty()) {
            return opponent.getSalvo().stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList())
                    .containsAll(self.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList()));
        }
        return false;
    }


}
