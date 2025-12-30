package com.abdou.microblogging.account;

import com.abdou.microblogging.account.dto.AccountDetailsDto;
import com.abdou.microblogging.account.dto.CreateAccountDto;
import com.abdou.microblogging.account.dto.UpdateAccountDto;
import com.abdou.microblogging.account.exception.AccountNotFoundException;
import com.abdou.microblogging.role.Role;
import com.abdou.microblogging.role.RoleRepository;
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
                .orElseThrow(() -> new RuntimeException(
                        "Error: Role is not found."));
        Account account = new Account(createAccountDto.username(),
                createAccountDto.email(),
                encoder.encode(createAccountDto.password()),
                userRole);
        accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    public ResponseEntity<PagedModel<AccountDetailsDto>> getPaginatedAccounts(
            int page
    ) {
        return ResponseEntity.ok()
                .body(new PagedModel<>(accountRepository.findAll(Pageable.ofSize(
                        10).withPage(page - 1)).map(AccountDetailsDto::toDto)));
    }

    public ResponseEntity<AccountDetailsDto> getAccountInfo(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        AccountDetailsDto details = AccountDetailsDto.toDto(account);
        return ResponseEntity.ok().body(details);
    }

    public ResponseEntity<?> getAccountInfo(Account account) {
        return ResponseEntity.ok()
                .body(accountRepository.findById(account.getId())
                        .orElseThrow(() -> new AccountNotFoundException(account.getId())));
    }

    public ResponseEntity<Account> updateAccount(UpdateAccountDto updateAccountDto,
                                                 UUID id
    ) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (updateAccountDto.username() != null) {
            account.setUsername(updateAccountDto.username());
        }
        if (updateAccountDto.email() != null) {
            account.setEmail(updateAccountDto.email());
        }
        if (updateAccountDto.password() != null) {
            account.setPassword(updateAccountDto.password());
        }
        return ResponseEntity.ok(accountRepository.save(account));
    }

    public ResponseEntity<?> deleteAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);
        return ResponseEntity.ok().build();
    }
}
