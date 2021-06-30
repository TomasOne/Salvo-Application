package com.codeoftheweb.Salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

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

    private LocalDateTime date;

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player, LocalDateTime date) {
        this.game = game;
        this.player = player;
        this.date = date;
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
