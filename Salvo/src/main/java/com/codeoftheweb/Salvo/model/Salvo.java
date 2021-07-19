package com.codeoftheweb.Salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Salvo  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private  GamePlayer gamePlayer;

    @ElementCollection
    @JoinColumn(name = "salvoLocation")
    private List<String> salvoLocations;

    private Integer turnId;

    public Salvo() {
    }

    public Salvo(GamePlayer gamePlayer, List<String> salvoLocation, Integer turnId) {
        this.gamePlayer = gamePlayer;
        this.salvoLocations = salvoLocation;
        this.turnId = turnId;
    }

    //GETTERS
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public Integer getTurnId() {
        return turnId;
    }

    //SETTERS
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setSalvoLocations(List<String> salvoLocation) {
        this.salvoLocations = salvoLocation;
    }

    public void setTurnId(Integer turnId) {
        this.turnId = turnId;
    }

}
