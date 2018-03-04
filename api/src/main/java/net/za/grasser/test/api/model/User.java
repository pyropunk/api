/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * User.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A user that can be persisted in the table simple_user to avoid conflicts with system tables.
 */
@Entity
@Table(name = "api_user")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 250)
    @JsonIgnore
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

    @JsonIgnore
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

    @JsonProperty
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
