/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Session.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 */
@Entity
@Table(name = "api_session")
@EntityListeners(AuditingEntityListener.class)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime active;
    private LocalDateTime ended;
    private LocalDateTime started;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String token;

    /*
    private Date createdAt;
    private Date createdBy;
    private Date updatedAt;
    private Date updatedBy;
    */
    
    /**
     * Constructor
     */
    public Session() {

        super();
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public LocalDateTime getActive() {

        return active;
    }

    public LocalDateTime getEnded() {

        return ended;
    }

    public LocalDateTime getStarted() {

        return started;
    }

    public String getToken() {

        return token;
    }

    public User getUser() {

        return user;
    }

    public void setActive(LocalDateTime active) {

        this.active = active;
    }

    public void setEnded(LocalDateTime ended) {

        this.ended = ended;
    }

    public void setStarted(LocalDateTime started) {

        this.started = started;
    }

    public void setToken(String token) {

        this.token = token;
    }

    public void setUser(User user) {

        this.user = user;
    }

}
