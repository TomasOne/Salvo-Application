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

    private String shipType;

    @ElementCollection
    @Column(name = "location")
    private List<String> shipLocation;

    public Ship() {
    }

    public Ship(GamePlayer gamePlayer, String shipType, List<String> shipLocation) {
        this.gamePlayer = gamePlayer;
        this.shipType = shipType;
        this.shipLocation = shipLocation;
    }

    //GETTERS

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public String getShipType() {
        return shipType;
    }

    public List<String> getLocation() {
        return shipLocation;
    }

    //SETTERS

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public void setLocation(List<String> location) {
        this.shipLocation = location;
    }
}
