/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * UserController.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.za.grasser.test.api.model.User;
import net.za.grasser.test.api.repo.UserRepository;

/**
 *
 */
@RestController("users")
public class UserController {

    private final UserRepository userRepository;

    @RequestMapping(name = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody(required = true) User user) {

        final User saved = userRepository.save(user);

        final URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> find() {

        return new ArrayList<>();
    }

    /**
     * 
     */
    public UserController(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

}
