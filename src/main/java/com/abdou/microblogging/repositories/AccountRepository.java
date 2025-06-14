package com.abdou.microblogging.repositories;

import com.abdou.microblogging.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, PagingAndSortingRepository<Account, UUID> {

}
