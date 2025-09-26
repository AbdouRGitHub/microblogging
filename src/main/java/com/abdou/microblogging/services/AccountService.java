package com.abdou.microblogging.services;

import com.abdou.microblogging.dto.account.AccountDto;
import com.abdou.microblogging.dto.account.AccountUpdateDto;
import com.abdou.microblogging.entities.Account;
import com.abdou.microblogging.exceptions.AccountNotFoundException;
import com.abdou.microblogging.repositories.AccountRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Account> createAccount(AccountDto accountDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        Account account = new Account(accountDto.username(), accountDto.email(), encoder.encode(accountDto.password()));
        accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    public ResponseEntity<PagedModel<Account>> getPaginatedAccounts(int page) {
        return ResponseEntity.ok().body(new PagedModel<>(accountRepository.findAll(Pageable.ofSize(10).withPage(page - 1))));
    }

    public ResponseEntity<Account> getAccountInfo(UUID id) {
        return ResponseEntity.ok().body(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id)));
    }

    public ResponseEntity<?> getAccountInfo(Account account) {
        return ResponseEntity.ok().body(accountRepository.findById(account.getId()).orElseThrow(() -> new AccountNotFoundException(account.getId())));
    }

    public ResponseEntity<Account> updateAccount(AccountUpdateDto accountUpdateDto, UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));

        if (accountUpdateDto.username() != null) {
            account.setUsername(accountUpdateDto.username());
        }
        if (accountUpdateDto.email() != null) {
            account.setEmail(accountUpdateDto.email());
        }
        if (accountUpdateDto.password() != null) {
            account.setPassword(accountUpdateDto.password());
        }
        return ResponseEntity.ok(accountRepository.save(account));
    }

    public ResponseEntity<?> deleteAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);
        return ResponseEntity.ok().build();
    }
}
