package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.Utils.Util;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(Util.makeMap("error", "Missing Data"), HttpStatus.FORBIDDEN);
        }

        if (playerRepo.findByUserName(email) !=  null) {
            return new ResponseEntity<>(Util.makeMap("error", "User Already Exist"), HttpStatus.FORBIDDEN);
        }

        playerRepo.save(new Player(email,passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}
