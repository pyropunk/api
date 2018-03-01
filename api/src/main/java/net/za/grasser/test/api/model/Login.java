/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Login.java
 *
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package net.za.grasser.test.api.model;


/**
 *
 */
public class Login {

    String username;
    String password;
    
    public Login() {

        super();
    }

    
    public String getUsername() {
    
        return username;
    }

    
    public void setUsername(String username) {
    
        this.username = username;
    }

    
    public String getPassword() {
    
        return password;
    }

    
    public void setPassword(String password) {
    
        this.password = password;
    }

}
