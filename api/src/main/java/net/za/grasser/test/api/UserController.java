/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * UserController.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.id.GUIDGenerator;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.za.grasser.test.api.model.Login;
import net.za.grasser.test.api.model.Session;
import net.za.grasser.test.api.model.Token;
import net.za.grasser.test.api.model.User;
import net.za.grasser.test.api.repo.SessionRepository;
import net.za.grasser.test.api.repo.UserRepository;

/**
 * A REST controller for the {@link User} entity.<br>
 * It is unconventional in the following way:
 * <ul>
 * <li>POST path is singular and GET is plural - REST resources paths should be plural</li>
 * <li>Handles two different entities - REST resources should handle on entity only</li>
 * </ul>
 */
@RestController
public class UserController {

    private static final String PATH = "/api/user";
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @RequestMapping(path = PATH + "/add", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> add(@RequestBody(required = true) User user) {

        final User saved;
        try {
            saved = userRepository.save(setPassword(user));
        }
        catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        final URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path(PATH + "s/{id}")
            .buildAndExpand(saved.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Add a salt and encrypt the password
     * 
     * @param user
     * @return user
     * @throws NoSuchAlgorithmException
     */
    private static User setPassword(User user) throws NoSuchAlgorithmException {

        user.setSalt(SecureRandom.getInstanceStrong().ints(25).collect(StringBuilder::new,
            StringBuilder::appendCodePoint, StringBuilder::append)
            .toString());
        user.setPassword(encode(user.getSalt(), user.getPassword()));
        return user;
    }

    /**
     * @param user
     * @param digest
     * @return
     * @throws NoSuchAlgorithmException
     */
    protected static String encode(String s, String p) throws NoSuchAlgorithmException {

        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(digest.digest((s + p).getBytes(StandardCharsets.UTF_8)));
    }

    @RequestMapping(path = PATH + "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Token> login(@RequestBody(required = true) Login login) {

        final User saved;
        try {
            Optional<User> u = checkPassword(login);
            if (u.isPresent()) {
                Session s = new Session();
                s.setActive(new Date());
                s.setStarted(new Date());
                s.setToken(UUID.randomUUID().toString());
                s.setUser(u.get());
                sessionRepository.save(s);
                
                final Token t = new Token();
                t.setId(s.getId());
                t.setToken(s.getToken());
                return ResponseEntity.ok(t);
            }
        }
        catch (PermissionDeniedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * @param login
     * @return
     * @throws NoSuchAlgorithmException
     */
    private Optional<User> checkPassword(Login login) throws PermissionDeniedDataAccessException, NoSuchAlgorithmException {

        Optional<User> user = userRepository.findByUsername(login.getUsername());
        if ( !user.isPresent()) {
            throw new PermissionDeniedDataAccessException("No user", new Throwable());
        }
        if ( !encode(user.get().getSalt(), login.getPassword()).equals(user.get().getPassword())) {
            throw new PermissionDeniedDataAccessException("password mismatch", new Throwable());
        }
        return user;
    }

    @RequestMapping(path = PATH + "s", method = RequestMethod.GET, produces = "application/json")
    public Iterable<User> findAll() {

        return userRepository.findAll();
    }

    @RequestMapping(path = PATH + "s/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<User> find(@PathVariable(name = "id", required = false) Long id) {

        final User u = userRepository.findOne(id);
        if (u == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(u);
    }

    /**
     * Constructor
     * 
     * @param userRepository
     *            an actual respository that reads and writes <code>User</code>s.
     */
    public UserController(UserRepository userRepository, SessionRepository sessionRepository) {

        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

}
