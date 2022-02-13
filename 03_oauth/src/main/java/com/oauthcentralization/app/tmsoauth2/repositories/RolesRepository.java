package com.oauthcentralization.app.tmsoauth2.repositories;

import com.oauthcentralization.app.tmsoauth2.entities.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Integer> {

}
