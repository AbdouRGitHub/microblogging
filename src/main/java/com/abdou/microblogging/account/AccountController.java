package com.abdou.microblogging.account;

import com.abdou.microblogging.account.dto.AccountDto;
import com.abdou.microblogging.account.dto.AccountUpdateDto;
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
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDto accountDto) {
        return accountService.createAccount(accountDto);
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
    public ResponseEntity<Account> updateAccount(@Valid @RequestBody AccountUpdateDto accountUpdateDto, @AuthenticationPrincipal Account account) {
        return accountService.updateAccount(accountUpdateDto, account.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal Account account) {
        return accountService.deleteAccount(account.getId());
    }
}