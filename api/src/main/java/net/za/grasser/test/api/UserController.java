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
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping(path = UserController.PATH)
public class UserController {

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    protected static final String PATH = "/api/users";

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    /**
     * Generate a SHA-256 digest and encode it in Base64 to store it as a String.
     * 
     * @param user
     * @param digest
     * @return
     * @throws NoSuchAlgorithmException
     */
    protected static String encode(String s, String p) throws NoSuchAlgorithmException {

        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(digest.digest((s + p).getBytes(StandardCharsets.UTF_8)));
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> add(@RequestBody(required = true) User user) {

        final User saved;
        try {
            LOG.info(() -> "before save");
            saved = userRepository.save(setPassword(user));
            LOG.info(() -> "after save");
        }
        catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        catch (NoSuchAlgorithmException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        final URI location = ServletUriComponentsBuilder
            .fromCurrentServletMapping()
            .path(PATH)
            .path("/{id}")
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
    private static final User setPassword(User user) throws NoSuchAlgorithmException {

        // generate a SALT using a random int stream
        user.setSalt(SecureRandom
            .getInstanceStrong()
            .ints(100)
            .map(i -> i & 0xffff)
            .filter(i -> Character.isBmpCodePoint(i) | Character.isValidCodePoint(i))
            .limit(25)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString());
        // LOG.info(() -> String.format("salt:%s", user.getSalt()));
        user.setPassword(encode(user.getSalt(), user.getPassword()));
        return user;
    }

    protected static final User copyNoPass(User user) {

        final User u = new User();
        u.setId(user.getId());
        u.setPhone(user.getPhone());
        u.setUsername(user.getUsername());
        return u;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Iterable<User> findAll() {

        final List<User> t = new ArrayList<>();
        userRepository.findAll().forEach(t::add);
        return t.stream().map(u -> copyNoPass(u)).collect(Collectors.toList());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<User> find(@PathVariable(name = "id", required = false) Long id) {

        final User u = userRepository.findOne(id);
        if (u == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(copyNoPass(u));
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Token> login(@RequestBody(required = true) Login login) {

        try {
            Optional<Session> active = sessionRepository.findLastByUserUsernameAndEndedNull(login.getUsername());
            if (active.isPresent()) {
                Session n = active.get();
                n.setActive(new Date());
                n = sessionRepository.save(n);
                
                final Token t = new Token();
                t.setId(n.getId());
                t.setToken(n.getToken());
                return ResponseEntity.ok(t);
            }

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

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

    @RequestMapping(path = "/logout/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Token> logout(@PathVariable(name = "id", required = false) Long id) {

        try {
            final Session s = sessionRepository.findOne(id);
            if (s == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            s.setEnded(new Date());

            final Token t = new Token();
            t.setId(null);
            t.setToken(s.getToken());
            return ResponseEntity.ok(t);
        }
        catch (PermissionDeniedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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

    /**
     * Constructor
     * 
     * @param userRepository
     *            an actual respository that reads and writes <code>User</code>s.
     * @param sessionRepository
     *            an actual respository that reads and writes <code>Session</code>s.
     */
    @Autowired
    public UserController(UserRepository userRepository, SessionRepository sessionRepository) {

        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

}
