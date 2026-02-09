package com.abdou.microblogging.account;

import com.abdou.microblogging.account.dto.AccountDetailsDto;
import com.abdou.microblogging.account.dto.AccountSummaryDto;
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

    AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping()
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDto createAccountDto) {
        return accountService.createAccount(createAccountDto);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<AccountSummaryDto>> getPaginatedAccounts(@RequestParam(defaultValue = "1") int page) {
        return accountService.getPaginatedAccounts(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountSummaryDto> getAccountInfo(@PathVariable UUID id) {
        return accountService.getAccountInfo(id);
    }

    @GetMapping("/me")
    public ResponseEntity<AccountSummaryDto> getAccountSummary(@AuthenticationPrincipal AccountPrincipal principal) {
        return accountService.getAccountSummary(principal);
    }

    @GetMapping("/me/details")
    public ResponseEntity<AccountDetailsDto> getAccountDetails(@AuthenticationPrincipal AccountPrincipal principal) {
        return accountService.getAccountDetails(principal);
    }

    @PatchMapping()
    public ResponseEntity<Void> updateAccount(@Valid @RequestBody UpdateAccountDto updateAccountDto,
                                              @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return accountService.updateAccount(updateAccountDto, principal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal AccountPrincipal principal) {
        return accountService.deleteAccount(principal.getId());
    }
}