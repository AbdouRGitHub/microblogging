package com.abdou.microblogging.repositories;

import com.abdou.microblogging.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, PagingAndSortingRepository<Account, Long> {

}
