package com.abdou.microblogging.entities;

import jakarta.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String message;

    @ManyToOne
    private Account account;

    public String getMessage() {
        return message;
    }
}
