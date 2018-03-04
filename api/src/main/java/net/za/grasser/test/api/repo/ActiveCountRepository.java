/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * ActiveCountRepository.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api.repo;

import org.springframework.data.repository.CrudRepository;

import net.za.grasser.test.api.model.ActiveCount;

/**
 *
 */
public interface ActiveCountRepository extends CrudRepository<ActiveCount, Long> {

    // no new functions defined
}
