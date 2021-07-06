package com.codeoftheweb.Salvo.model;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

//el Entity es ubicado justo encima de la clase para que sus atributos se apliqun a esta
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    //Las etiquetas asignan sus atributos a la primer variable creada. Se pone ID como primera y se la separa del resto
    private LocalDateTime data;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    List<GamePlayer> gamePlayer;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<Score> scores;

    public void addGamePlayer(GamePlayer gamePlayers)
    {
        gamePlayers.setGame(this);
        gamePlayer.add(gamePlayers);
    }

    @JsonIgnore
    public List<Player> getPlayer(){
        return gamePlayer.stream().map(GamePlayer::getPlayer).collect(toList());
    }

    public Game(LocalDateTime data) {
        this.data = data;
    }

    public Game() {
    }


    public LocalDateTime getData() {
        return data;
    }

    public long getId()
    {
        return  id;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public List<GamePlayer> getGamePlayer()
    {
            return  gamePlayer;
    }

}
