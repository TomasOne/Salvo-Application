package com.codeoftheweb.Salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name =  "Game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Player_id")
    private Player player;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    List<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    List<Salvo> salvos;



    private LocalDateTime date;

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player, LocalDateTime date) {
        this.game = game;
        this.player = player;
        this.date = date;
    }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    //GETTERS
    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public long getId() {
        return id;
    }

    //SETTERS
    public void setGame(Game game) {
        this.game = game;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
