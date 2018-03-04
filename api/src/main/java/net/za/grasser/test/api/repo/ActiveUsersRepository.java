/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * ActiveUsersRepository.java
 *
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package net.za.grasser.test.api.repo;

import org.springframework.data.repository.CrudRepository;

import net.za.grasser.test.api.model.ActiveUser;


/**
 * Repository for the active users view.
 */
public interface ActiveUsersRepository extends CrudRepository<ActiveUser, Long> {
    
   // no new functions declared
}
