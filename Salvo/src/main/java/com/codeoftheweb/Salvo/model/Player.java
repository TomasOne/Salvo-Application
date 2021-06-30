package com.codeoftheweb.Salvo.model;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String userName;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    List<GamePlayer> gamePlayer;

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

    public Player(String userName) {
        this.userName = userName;
    }

    //GETTER
    public String getUserName() {
        return userName;
    }

    public long getId() {
        return id;
    }

    //SETTER
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
