package com.abdou.microblogging.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, PagingAndSortingRepository<Account, UUID> {
    @Query("SELECT a FROM Account a WHERE a.username = :username")
    Optional<Account> findByUsername(String username);
}
