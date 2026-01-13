package com.abdou.microblogging;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.account.AccountRepository;
import com.abdou.microblogging.common.CustomUserDetailsService;
import com.abdou.microblogging.like.Like;
import com.abdou.microblogging.like.LikeRepository;
import com.abdou.microblogging.post.Post;
import com.abdou.microblogging.post.PostRepository;
import com.abdou.microblogging.role.Role;
import com.abdou.microblogging.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@SpringBootApplication
@EnableJpaAuditing
@EnableWebSecurity
public class MicrobloggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrobloggingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository,
                                          AccountRepository accountRepository,
                                          PostRepository postRepository,
                                          LikeRepository likeRepository,
                                          PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (accountRepository.count() > 0) {
                return;
            }

            Role userRole = new Role("ROLE_USER");
            Role adminRole = new Role("ROLE_ADMIN");
            userRole = roleRepository.save(userRole);
            adminRole = roleRepository.save(adminRole);

            List<Account> allAccounts = new ArrayList<>();
            List<Post> allPosts = new ArrayList<>();

            // 20 utilisateurs (user1: ADMIN, autres: USER)
            for (int i = 1; i <= 20; i++) {
                boolean isAdmin = (i == 1);
                Role mainRole = isAdmin ? adminRole : userRole;

                Account account = new Account("user" + i,
                        "user" + i + "@example.com",
                        passwordEncoder.encode("password123"),
                        mainRole);
                account = accountRepository.save(account);
                allAccounts.add(account);

                for (int p = 1; p <= 3; p++) {
                    Post post =
                            new Post("Post " + p + " de " + account.getUsername(),
                                    account);
                    post = postRepository.save(post);
                    allPosts.add(post);

                    for (int c = 1; c <= 5; c++) {
                        Post comment =
                                new Post("Commentaire " + c + " sur le post " + p + " de " + account.getUsername(),
                                        account,
                                        post);
                        comment = postRepository.save(comment);

                        for (int r = 1; r <= 2; r++) {
                            Post reply =
                                    new Post("Réponse " + r + " au commentaire " + c + " du post " + p + " de " + account.getUsername(),
                                            account,
                                            comment);
                            postRepository.save(reply);
                        }
                    }
                }
            }

            // Peuplement des likes
            for (Post post : allPosts) {
                // Chaque post reçoit des likes d'utilisateurs aléatoires (entre 0 et 10 likes)
                int likesCount = (int) (Math.random() * 11);
                for (int l = 0; l < likesCount; l++) {
                    Account randomAccount =
                            allAccounts.get((int) (Math.random() * allAccounts.size()));
                    Like like = new Like(post, randomAccount);
                    likeRepository.save(like);
                }
            }
        };
    }

    @Bean
    @Order(1)
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(
                        "/auth/login",
                        "/auth/sessionExpired",
                        "/auth/logoutSuccess",
                        "/posts/**",
                        "/comments/**",
                        "/accounts/**"
                )
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth/login",
                                "/auth/sessionExpired",
                                "/auth/logoutSuccess")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/accounts")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/accounts/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/comments/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain authenticatedSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/auth/sessionExpired")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/logoutSuccess")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(COOKIES)))
                        .permitAll()
                )
                .build();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://example.com",
                "http://localhost:5173"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        configuration.setAllowCredentials(true);
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
