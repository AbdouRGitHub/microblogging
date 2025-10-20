package com.abdou.microblogging.auth;

import com.abdou.microblogging.account.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    AuthService(AccountRepository accountRepository, AuthenticationManager authenticationManager) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authenticationRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
            Authentication authenticationResponse =
                    this.authenticationManager.authenticate(authenticationRequest);

            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authenticationResponse);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect Username or/and password");
        }
    }

    public ResponseEntity<?> sessionExpired() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session has expired");
    }

    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }

}
