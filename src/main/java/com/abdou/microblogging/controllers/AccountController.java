package com.abdou.microblogging.controllers;

import com.abdou.microblogging.dto.AccountDto;
import com.abdou.microblogging.entities.Account;
import com.abdou.microblogging.exceptions.AccountNotFoundException;
import com.abdou.microblogging.repositories.AccountRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/accounts")
public class AccountController {
    private final AccountRepository accountRepository;

    AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping()
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDto accountDto) {
        Account account = new Account(accountDto.getUsername(), accountDto.getEmail(), accountDto.getPassword());
        accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<Account>> getPaginatedAccounts(@RequestParam(required = true) int page) {
        return ResponseEntity.ok().body(new PagedModel<>(accountRepository.findAll(Pageable.ofSize(10).withPage(page - 1))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountInfo(@PathVariable UUID id) {
        return ResponseEntity.ok().body(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@Valid @RequestBody AccountDto accountDto, @PathVariable UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        account.setUsername(accountDto.getUsername());
        account.setPassword(accountDto.getPassword());
        return ResponseEntity.ok().body(accountRepository.save(account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);
        return ResponseEntity.ok().build();
    }
}
