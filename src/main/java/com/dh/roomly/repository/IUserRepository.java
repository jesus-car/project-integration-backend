package com.dh.roomly.repository;

import com.dh.roomly.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<UserEntity> findAll(Pageable pageable);

    @Query("SELECT p.id FROM UserEntity u JOIN u.favoriteProperties p WHERE u.id = :userId")
    List<Long> findFavoriteProductIdsByUserId(@Param("userId") Long userId);
    UserEntity findByUsername(String username);
}
