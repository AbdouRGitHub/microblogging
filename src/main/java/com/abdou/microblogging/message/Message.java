package com.abdou.microblogging.message;

import com.abdou.microblogging.account.Account;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 300)
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Message parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Message> replies = new ArrayList<>();

    public Message() {
    }

    public Message(String content, Account account) {
        this.content = content;
        this.account = account;
    }

    public Message(String content, Account account, Message parent) {
        this.content = content;
        this.account = account;
        this.parent = parent;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Account getAccount() {
        return account;
    }


}
