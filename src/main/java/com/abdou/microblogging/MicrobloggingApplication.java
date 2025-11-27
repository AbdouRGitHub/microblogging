package com.abdou.microblogging;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.account.AccountRepository;
import com.abdou.microblogging.comment.Comment;
import com.abdou.microblogging.comment.CommentRepository;
import com.abdou.microblogging.common.CustomUserDetailsService;
import com.abdou.microblogging.post.Post;
import com.abdou.microblogging.post.PostRepository;
import com.abdou.microblogging.role.Role;
import com.abdou.microblogging.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@SpringBootApplication
@EnableJpaAuditing
@EnableWebSecurity
public class MicrobloggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrobloggingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(
            RoleRepository roleRepository,
            AccountRepository accountRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            System.out.println("\n========================================");
            System.out.println(
                    "🚀 Démarrage du peuplement de la base de données...");
            System.out.println("========================================\n");

            // ========== Création des rôles ==========
            if (roleRepository.count() == 0) {
                Role userRole = new Role();
                userRole.setName("ROLE_USER");

                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");

                roleRepository.saveAll(Arrays.asList(userRole, adminRole));
                System.out.println("✓ Rôles USER et ADMIN créés avec succès");
            } else {
                System.out.println(
                        "✓ Les rôles existent déjà dans la base de données");
            }

            // ========== Création des utilisateurs ==========
            if (accountRepository.count() == 0) {
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException(
                                "Role ROLE_USER non trouvé"));

                String[] usernames = {
                        "alice_martin", "bob_dupont", "charlie_bernard", "diana_petit",
                        "emma_durand", "felix_moreau", "grace_laurent", "hugo_simon",
                        "iris_michel", "jules_lefebvre"
                };

                String[] emails = {
                        "alice.martin@example.com", "bob.dupont@example.com",
                        "charlie.bernard@example.com", "diana.petit@example.com",
                        "emma.durand@example.com", "felix.moreau@example.com",
                        "grace.laurent@example.com", "hugo.simon@example.com",
                        "iris.michel@example.com", "jules.lefebvre@example.com"
                };

                for (int i = 0; i < 10; i++) {
                    Account account = new Account(
                            usernames[i],
                            emails[i],
                            passwordEncoder.encode("password123"),
                            userRole
                    );
                    accountRepository.save(account);
                }

                System.out.println("✓ 10 utilisateurs créés avec succès");
            } else {
                System.out.println(
                        "✓ Les utilisateurs existent déjà dans la base de données");
            }

            // ========== Création des posts ==========
            if (postRepository.count() == 0) {
                List<Account> accounts = accountRepository.findAll();

                String[] postContents = {
                        "Premier post ! 🎉",
                        "Belle journée ☀️",
                        "J'adore Spring Boot 💻",
                        "Besoin d'aide sur JPA ?",
                        "Café du matin ☕",
                        "Bug depuis 2h 😅",
                        "Nouveau projet 🚀",
                        "Tests unitaires #TDD",
                        "Weekend enfin !",
                        "Lecture : Clean Code 📚",
                        "IntelliJ > tout",
                        "Docker en prod ?",
                        "La doc c'est important",
                        "Pause déjeuner 🍕",
                        "Bug critique fixé",
                        "Design patterns 👌",
                        "Première contrib OS 🎊",
                        "Pair programming ?",
                        "Refactoring time 😌",
                        "Java nouvelle version !",
                        "Microservices ftw",
                        "Code review ++",
                        "PostgreSQL vs MySQL ?",
                        "Security first 🔒",
                        "REST best practices",
                        "Journée productive 💪",
                        "Raccourcis clavier 🎹",
                        "Archi hexa retour +",
                        "Clean archi validé",
                        "Bonne soirée devs 🌙"
                };

                // Chaque utilisateur crée 3 posts
                int postIndex = 0;
                for (Account account : accounts) {
                    for (int j = 0; j < 3; j++) {
                        if (postIndex < postContents.length) {
                            Post post =
                                    new Post(postContents[postIndex], account);
                            postRepository.save(post);
                            postIndex++;
                        }
                    }
                }

                System.out.println("✓ " + postIndex + " posts créés avec succès");
            } else {
                System.out.println(
                        "✓ Les posts existent déjà dans la base de données");
            }

            // ========== Création des commentaires ==========
            if (commentRepository.count() == 0) {
                List<Account> accounts = accountRepository.findAll();
                List<Post> posts = postRepository.findAll();
                Random random = new Random();

                String[] commentContents = {
                        "Super ! 👍",
                        "Totalement d'accord",
                        "Intéressant 🤔",
                        "Merci !",
                        "Pareil ici",
                        "Bonne question",
                        "Check la doc",
                        "Bravo 🎉",
                        "Très utile",
                        "Je peux aider",
                        "Nice !",
                        "Hâte de voir",
                        "GG 🎊",
                        "Exactement ça",
                        "Merci l'astuce",
                        "Je connaissais pas",
                        "Bien expliqué 👌",
                        "Bonne idée",
                        "Partant !",
                        "Top ressource"
                };

                // Créer 2-3 commentaires aléatoires pour chaque post
                int totalComments = 0;
                for (Post post : posts) {
                    int numComments =
                            2 + random.nextInt(2); // 2 ou 3 commentaires
                    for (int i = 0; i < numComments; i++) {
                        Account randomAccount =
                                accounts.get(random.nextInt(accounts.size()));
                        String content =
                                commentContents[random.nextInt(commentContents.length)];
                        Comment comment =
                                new Comment(content, post, randomAccount);
                        commentRepository.save(comment);
                        totalComments++;
                    }
                }

                System.out.println("✓ " + totalComments + " commentaires créés avec succès");

                // Créer quelques réponses aux commentaires
                List<Comment> comments = commentRepository.findAll();
                String[] replyContents = {
                        "Merci ! 😊",
                        "De rien !",
                        "Content d'aider",
                        "N'hésite pas",
                        "Merci à toi",
                        "Oui !",
                        "Exactement 💯",
                        "Je te dis",
                        "Super merci",
                        "👍👍"
                };

                // Créer 10-15 réponses aléatoires
                int numReplies = 10 + random.nextInt(6);
                int createdReplies = 0;
                for (int i = 0; i < numReplies && i < comments.size(); i++) {
                    Comment parentComment =
                            comments.get(random.nextInt(comments.size()));
                    Account randomAccount =
                            accounts.get(random.nextInt(accounts.size()));
                    String content =
                            replyContents[random.nextInt(replyContents.length)];
                    Comment reply = new Comment(content,
                            parentComment.getPost(),
                            randomAccount,
                            parentComment);
                    commentRepository.save(reply);
                    createdReplies++;
                }

                System.out.println("✓ " + createdReplies + " réponses aux commentaires créées avec succès");
            } else {
                System.out.println(
                        "✓ Les commentaires existent déjà dans la base de données");
            }

            System.out.println("\n========================================");
            System.out.println("✅ Base de données peuplée avec succès !");
            System.out.println("========================================");
            System.out.println("📊 Statistiques :");
            System.out.println("   - Rôles : " + roleRepository.count());
            System.out.println("   - Utilisateurs : " + accountRepository.count());
            System.out.println("   - Posts : " + postRepository.count());
            System.out.println("   - Commentaires : " + commentRepository.count());
            System.out.println("========================================\n");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authorize) -> authorize.requestMatchers(
                                "/auth/login")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/accounts")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/**")
                        .permitAll()
                        .requestMatchers("/posts/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/comments/**")
                        .permitAll()
                        .requestMatchers("/comments/**")
                        .hasAnyRole("USER", "ADMIN")
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.invalidSessionUrl(
                                "/auth/sessionExpired")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout.logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/logoutSuccess")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(
                                COOKIES)))
                        .permitAll())
                .build();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://example.com",
                "http://localhost:5173/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return new CustomUserDetailsService(accountRepository);
    }

    @Bean
    public AuthenticationManager authenticationProvider(UserDetailsService userDetailsService,
                                                        PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

}
