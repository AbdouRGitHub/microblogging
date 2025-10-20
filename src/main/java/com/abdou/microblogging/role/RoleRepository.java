package com.abdou.microblogging.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID>, PagingAndSortingRepository<Role, UUID> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}
