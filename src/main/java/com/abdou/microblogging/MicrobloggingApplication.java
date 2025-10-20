package com.abdou.microblogging;

import com.abdou.microblogging.role.Role;
import com.abdou.microblogging.account.AccountRepository;
import com.abdou.microblogging.role.RoleRepository;
import com.abdou.microblogging.common.CustomUserDetailsService;
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

import java.util.Arrays;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@SpringBootApplication
@EnableJpaAuditing
@EnableWebSecurity
public class MicrobloggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrobloggingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {

            if (roleRepository.count() == 0) {

                Role userRole = new Role();
                userRole.setName("ROLE_USER");

                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");

                roleRepository.saveAll(Arrays.asList(userRole, adminRole));

                System.out.println("✓ Rôles USER et ADMIN créés avec succès");
            } else {
                System.out.println("✓ Les rôles existent déjà dans la base de données");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/auth/login")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/accounts")
                        .permitAll()
                        .requestMatchers("/posts/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/comments/**")
                        .hasAnyRole("USER", "ADMIN")
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.invalidSessionUrl("/auth/sessionExpired")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout.logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/logoutSuccess")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)))
                        .permitAll())
                .build();
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
    public AuthenticationManager authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

}
