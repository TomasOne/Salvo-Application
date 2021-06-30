package com.codeoftheweb.Salvo.repository;

import com.codeoftheweb.Salvo.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
}
