/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SessionController.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.za.grasser.test.api.model.ActiveCount;
import net.za.grasser.test.api.model.ActiveUser;
import net.za.grasser.test.api.model.User;
import net.za.grasser.test.api.repo.ActiveCountRepository;
import net.za.grasser.test.api.repo.ActiveUsersRepository;
import net.za.grasser.test.api.repo.SessionRepository;
import net.za.grasser.test.api.repo.UserRepository;

/**
 *
 */
@RestController
@RequestMapping(path = SessionController.PATH)
public class SessionController {

    protected static final String PATH = "/api/reports";
    private ActiveUsersRepository activeUserRepository;
    private ActiveCountRepository activeCountRepository;

    @RequestMapping(path = "/active", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Iterable<ActiveUser>> getActive() {

        // not standard but it allows us to set a time
        return ResponseEntity.ok().header("Refresh", "1;").body(activeUserRepository.findAll());
    }

    @RequestMapping(path = "/count", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Iterable<ActiveCount>> getCount() {

        return ResponseEntity.ok().header("Refresh", "1;").body(activeCountRepository.findAll());
    }

    /**
     * Constructor
     * 
     * @param activeUserRepository
     *            an actual respository that reads <code>ActiveUser</code>s.
     * @param activeCountRepository
     *            an actual respository that reads and writes <code>ActiveCount</code>s.
     */
    @Autowired
    public SessionController(ActiveUsersRepository activeUserRepository, ActiveCountRepository activeCountRepository) {

        this.activeUserRepository = activeUserRepository;
        this.activeCountRepository = activeCountRepository;
    }
}
