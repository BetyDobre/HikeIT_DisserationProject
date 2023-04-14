package com.hike.repository;

import com.hike.models.Traseu;
import com.hike.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity findFirstByUsername(String username);
    Optional<UserEntity>findById(Long id);
    UserEntity findByResetParolaToken(String token);
    void deleteById(Long id);

    @Query("SELECT t FROM trasee t JOIN t.usersParcurs u WHERE u = :user")
    Page<Traseu> findTraseeParcurseByUser(UserEntity user, Pageable pageable);

    @Query("SELECT count(t) FROM trasee t JOIN t.usersParcurs u where u = :user")
    int countAllByTraseeParcurse(UserEntity user);
}
