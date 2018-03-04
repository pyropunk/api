/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * ActiveCount.java
 *
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package net.za.grasser.test.api.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name="last_users")
public class ActiveCount {

    @Id
    Long id;
    Integer active;
    
    /**
     * Default Constructor
     */
    public ActiveCount() {

        super();
    }

    
    public Integer getActive() {
    
        return active;
    }
}
