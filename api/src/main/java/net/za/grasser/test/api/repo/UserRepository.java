/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * UserRepository.java
 *
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package net.za.grasser.test.api.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import net.za.grasser.test.api.model.User;


/**
 *
 */
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
