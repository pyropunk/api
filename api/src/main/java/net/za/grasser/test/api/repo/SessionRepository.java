/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SessionRepository.java
 *
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package net.za.grasser.test.api.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.za.grasser.test.api.model.Session;
import net.za.grasser.test.api.model.User;

/**
 *
 */
@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findLastByUserUsernameAndEndedNull(String username);
}
