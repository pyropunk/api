/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * ActiveUser.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Linked to the active_users view.
 */
@Entity
@Table(name = "active_users")
public class ActiveUser {

    @Id
    Long id;
    String username;
    String phone;
    
    /**
     * Default Constructor.
     */
    public ActiveUser() {

        super();
    }

    
    public String getUsername() {
    
        return username;
    }

    
     public String getPhone() {
    
        return phone;
    }
}
