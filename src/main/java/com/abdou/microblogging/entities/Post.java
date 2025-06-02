package com.abdou.microblogging.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 300)
    private String message;

    @ManyToOne
    private Account account;

    public String getMessage() {
        return message;
    }
}
