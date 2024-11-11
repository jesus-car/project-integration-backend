package com.dh.roomly.repository;

import com.dh.roomly.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Query("""
            SELECT t FROM TokenEntity t inner join UserEntity u
            on t.user.id = u.id 
            WHERE t.user.id = :userId and t.loggedOut = false
        """)
    List<TokenEntity> findAllTokenByUser(Long userId);

    Optional<TokenEntity> findByToken(String token);
 }
