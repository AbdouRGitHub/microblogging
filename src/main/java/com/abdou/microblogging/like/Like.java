package com.abdou.microblogging.like;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.post.Post;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    private Post post;

    @ManyToOne(optional = false)
    private Account account;

    @CreatedDate
    private Long createdAt;

    public Like() {
    }

    public Like(Post post, Account account) {
        this.post = post;
        this.account = account;
    }
}
