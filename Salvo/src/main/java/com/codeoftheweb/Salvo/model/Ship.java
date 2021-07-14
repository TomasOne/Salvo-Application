package com.codeoftheweb.Salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private String type;

    @ElementCollection
    @Column(name = "location")
    private List<String> shipLocations;

    public Ship() {
    }

    public Ship(GamePlayer gamePlayer, String shipType, List<String> shipLocations) {
        this.gamePlayer = gamePlayer;
        this.type = shipType;
        this.shipLocations = shipLocations;
    }

    //GETTERS

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public String getType() {
        return type;
    }

    public List<String> getShipLocations() {
        return shipLocations;
    }

    //SETTERS

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }

}
