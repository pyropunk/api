/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Token.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api.model;

/**
 *
 */
public class Token {

    Long id;
    String token;

    public Token() {

        super();
    }

    
    public Long getId() {
    
        return id;
    }

    
    public void setId(Long id) {
    
        this.id = id;
    }

    
    public String getToken() {
    
        return token;
    }

    
    public void setToken(String token) {
    
        this.token = token;
    }

}
