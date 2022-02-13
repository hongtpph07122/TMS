package com.oauthcentralization.app.tmsoauth2.repositories;

import com.oauthcentralization.app.tmsoauth2.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {

    long count();

    boolean existsByUsername(String username);

    @Query("SELECT MAX(userEntity.failedLoginCount) FROM UsersEntity userEntity WHERE userEntity.userId = ?1")
    BigDecimal findMaxLoggedFailed(Integer userId);

    @Query("FROM UsersEntity userEntity WHERE userEntity.username = ?1")
    UsersEntity findUsersEntity(String username);

    @Query("FROM UsersEntity userEntity WHERE userEntity.isActive = ?1 ")
    List<UsersEntity> findUsersEntities(Integer isActive);

}
