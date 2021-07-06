package com.codeoftheweb.Salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private  Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private  Player player;

    private LocalDateTime finishDate;
    private float score;

    public Score() {
    }

    public Score(Game game, Player player, LocalDateTime date, float score) {
        this.game = game;
        this.player = player;
        this.finishDate = date;
        this.score = score;
    }

    //GETTERS


    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public float getScore() {
        return score;
    }

    //SETTERS


    public void setGame(Game game) {
        this.game = game;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
