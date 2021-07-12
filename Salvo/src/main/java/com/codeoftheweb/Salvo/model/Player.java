package com.codeoftheweb.Salvo.model;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String userName;
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    List<GamePlayer> gamePlayer;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores;

    public void addGamePlayer(GamePlayer gamePlayers)
    {
        gamePlayers.setPlayer(this);
        gamePlayer.add(gamePlayers);
    }

    @JsonIgnore
    public List<Game> getGame(){
        return gamePlayer.stream().map(GamePlayer::getGame).collect(toList());
    }

    public  Player(){}

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    //GETTER
    public String getUserName() {
        return userName;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Optional<Score> getScores(Game game) {
        return this.scores.stream().filter(x -> x.getGame().equals(game)).findFirst();
    }

    //SETTER
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
