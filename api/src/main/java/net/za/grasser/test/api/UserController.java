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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.PermissionDeniedDataAccessException;
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
     * @return a SHA-256 encoded, salted password hash
     * @throws NoSuchAlgorithmException
     */
    protected static String encode(String s, String p) throws NoSuchAlgorithmException {

        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(digest.digest((s + p).getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Add a user to the database.<br>
     * Requirement 1: Allow users to submit their details containing a username, phone number and password.
     * 
     * @param user
     *            - user to be added
     * @return 201 if user could be created,<br>
     *         403 if user already exists or cannot be created,<br>
     *         500 if the password cannot be encrypted
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> add(@RequestBody(required = true) User user) {

        // save the user, but compute a salted password first
        final User saved;
        try {
            saved = userRepository.save(setPassword(user));
        }
        catch (DataAccessException e) {
            // user cannot be saved - return forbidden
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        catch (NoSuchAlgorithmException e) {
            // password cannot be computed - return internal server error
            LOG.log(Level.SEVERE, e, () -> "Security not configured correctly");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // create a link to the user in the headers
        // TBD ideally this id should no be sequential but random so that user IDs cannot be guessed
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

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Iterable<User> findAll() {

        return userRepository.findAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<User> find(@PathVariable(name = "id", required = false) Long id) {

        final Optional<User> u = userRepository.findById(id);
        if ( !u.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(u.get());
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Token> login(@RequestBody(required = true) Login login) {

        try {
            Optional<Session> active = sessionRepository.findLastByUserUsernameAndEndedIsNull(login.getUsername());
            if (active.isPresent()) {
                Session n = active.get();
                if (Duration.between(n.getActive(), LocalDateTime.now()).getSeconds() > 180) {
                    return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
                }
                
                n.setActive(LocalDateTime.now());
                n = sessionRepository.save(n);

                final Token t = new Token();
                t.setId(n.getId());
                t.setToken(n.getToken());
                return ResponseEntity.ok(t);
            }

            Optional<User> u = checkPassword(login);
            if (u.isPresent()) {
                Session s = new Session();
                s.setActive(LocalDateTime.now());
                s.setStarted(LocalDateTime.now());
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
            final Optional<Session> s = sessionRepository.findById(id);
            if ( !s.isPresent() || s.get().getEnded() != null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            final Session session = s.get();
            session.setEnded(LocalDateTime.now());
            sessionRepository.save(session);

            final Token t = new Token();
            t.setId(null);
            t.setToken(session.getToken());
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
