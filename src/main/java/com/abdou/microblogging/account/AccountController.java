package com.abdou.microblogging.account;

import com.abdou.microblogging.account.dto.CreateAccountDto;
import com.abdou.microblogging.account.dto.UpdateAccountDto;
import jakarta.validation.Valid;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    AccountController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @PostMapping()
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDto createAccountDto) {
        return accountService.createAccount(createAccountDto);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<Account>> getPaginatedAccounts(@RequestParam(defaultValue = "1") int page) {
        return accountService.getPaginatedAccounts(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountInfo(@PathVariable UUID id) {
        return accountService.getAccountInfo(id);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAccountInfo(@AuthenticationPrincipal Account account) {
        return accountService.getAccountInfo(account);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@Valid @RequestBody UpdateAccountDto updateAccountDto, @AuthenticationPrincipal Account account) {
        return accountService.updateAccount(updateAccountDto, account.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal Account account) {
        return accountService.deleteAccount(account.getId());
    }
}