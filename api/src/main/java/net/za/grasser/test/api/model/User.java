/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * User.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A user that can be persisted in the table simple_user to avoid conflicts with system tables.
 */
@Entity
@Table(name = "simple_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 250)
    private String password;
    @Column(length = 25)
    private String phone;
    @Column(length = 250)
    @JsonIgnore
    private String salt;
    @Column(length = 25, unique = true)
    private String username;

    public User() {

        super();
    }

    public Long getId() {

        return id;
    }

    public String getPassword() {

        return password;
    }

    public String getPhone() {

        return phone;
    }

    public String getSalt() {

        return salt;
    }

    public String getUsername() {

        return username;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

    public void setSalt(String salt) {

        this.salt = salt;
    }

    public void setUsername(String username) {

        this.username = username;
    }

}
