package com.abdou.microblogging.account;

import com.abdou.microblogging.account.dto.AccountDetailsDto;
import com.abdou.microblogging.account.dto.AccountSummaryDto;
import com.abdou.microblogging.account.dto.CreateAccountDto;
import com.abdou.microblogging.account.dto.UpdateAccountDto;
import com.abdou.microblogging.account.exception.AccountNotFoundException;
import com.abdou.microblogging.common.exception.InvalidPasswordException;
import com.abdou.microblogging.common.exception.PasswordIsMissingException;
import com.abdou.microblogging.role.Role;
import com.abdou.microblogging.role.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    private final static Logger logger =
            LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    AccountService(AccountRepository accountRepository,
                   RoleRepository roleRepository
    ) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<Account> createAccount(CreateAccountDto createAccountDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> {
                    logger.warn("Role is not found: ROLE_USER");
                    return new RuntimeException("Error: Role is not found.");
                });
        Account account = new Account(createAccountDto.username(),
                createAccountDto.email(),
                encoder.encode(createAccountDto.password()),
                userRole);
        accountRepository.save(account);
        logger.info("Account created: {}", account.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<PagedModel<AccountSummaryDto>> getPaginatedAccounts(
            int page
    ) {
        return ResponseEntity.ok()
                .body(new PagedModel<>(accountRepository.findAll(Pageable.ofSize(
                        10).withPage(page - 1)).map(AccountSummaryDto::toDto)));
    }

    public ResponseEntity<AccountSummaryDto> getAccountInfo(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        AccountSummaryDto details = AccountSummaryDto.toDto(account);
        return ResponseEntity.ok().body(details);
    }

    public ResponseEntity<AccountSummaryDto> getAccountSummary(AccountPrincipal principal) {
        Account me = accountRepository.findById(principal.getId())
                .orElseThrow(() -> new AccountNotFoundException(principal.getId()));

        return ResponseEntity.ok(AccountSummaryDto.toDto(me));
    }

    public ResponseEntity<AccountDetailsDto> getAccountDetails(AccountPrincipal principal) {
        Account me = accountRepository.findById(principal.getId())
                .orElseThrow(() -> new AccountNotFoundException(principal.getId()));

        return ResponseEntity.ok(AccountDetailsDto.toDto(me));
    }

    public ResponseEntity<Void> updateAccount(UpdateAccountDto updateAccountDto,
                                              AccountPrincipal principal
    ) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        Account fullAccount = accountRepository.findById(principal.getId())
                .orElseThrow(() -> new AccountNotFoundException(principal.getId()));
        if (updateAccountDto.newPassword().isPresent()) {
            String current = updateAccountDto.currentPassword()
                    .orElseThrow(() -> new PasswordIsMissingException(
                            "Current password is required"));
            if (!encoder.matches(current, fullAccount.getPassword())) {
                throw new InvalidPasswordException(
                        "Current password is incorrect");
            }
        }
        accountRepository.save(updateAccountDto.toAccount(fullAccount));
        logger.info("Account updated: {}", principal.getId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);
        logger.info("Account deleted: {}", account.getId());
        return ResponseEntity.ok().build();
    }
}
