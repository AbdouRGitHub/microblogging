package com.abdou.microblogging.configuration;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.account.AccountRepository;
import com.abdou.microblogging.like.Like;
import com.abdou.microblogging.like.LikeRepository;
import com.abdou.microblogging.post.Post;
import com.abdou.microblogging.post.PostRepository;
import com.abdou.microblogging.role.Role;
import com.abdou.microblogging.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableJpaAuditing
public class DatabaseConfiguration {

    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository,
                                          AccountRepository accountRepository,
                                          PostRepository postRepository,
                                          LikeRepository likeRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            if (accountRepository.count() > 0) {
                return;
            }

            Role userRole = roleRepository.save(new Role("ROLE_USER"));
            Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
            List<Account> allAccounts = new ArrayList<>();
            List<Post> allPosts = new ArrayList<>();

            for (int i = 1; i <= 20; i++) {
                Role mainRole = i == 1 ? adminRole : userRole;
                Account account = accountRepository.save(new Account("user" + i,
                        "user" + i + "@example.com",
                        passwordEncoder.encode("password123"), mainRole));
                allAccounts.add(account);

                for (int p = 1; p <= 3; p++) {
                    Post post = postRepository.save(new Post("Post " + p + " de " + account.getUsername(), account));
                    allPosts.add(post);
                    for (int c = 1; c <= 5; c++) {
                        Post comment = postRepository.save(new Post(
                                "Commentaire " + c + " sur le post " + p + " de " + account.getUsername(), account, post));
                        for (int r = 1; r <= 2; r++) {
                            postRepository.save(new Post(
                                    "Réponse " + r + " au commentaire " + c + " du post " + p + " de " + account.getUsername(),
                                    account, comment));
                        }
                    }
                }
            }

            for (Post post : allPosts) {
                int likesCount = (int) (Math.random() * 11);
                for (int l = 0; l < likesCount; l++) {
                    Account randomAccount = allAccounts.get((int) (Math.random() * allAccounts.size()));
                    likeRepository.save(new Like(post, randomAccount));
                }
            }

            for (Account account : allAccounts) {
                int bookmarksCount = (int) (Math.random() * 6);
                Set<Post> bookmarkedPosts = new HashSet<>();
                for (int b = 0; b < bookmarksCount; b++) {
                    bookmarkedPosts.add(allPosts.get((int) (Math.random() * allPosts.size())));
                }
                account.setBookmarks(bookmarkedPosts);
                accountRepository.save(account);
            }
        };
    }
}
