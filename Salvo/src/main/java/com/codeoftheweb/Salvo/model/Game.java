package com.codeoftheweb.Salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

//el Entity es ubicado justo encima de la clase para que sus atributos se apliqun a esta
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    //Las etiquetas asignan sus atributos a la primer variable creada. Se pone ID como primera y se la separa del resto
    private LocalDateTime data;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<GamePlayer> gamePlayer;



    public Game(LocalDateTime data) {
        this.data = data;
    }

    public Game() {
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
