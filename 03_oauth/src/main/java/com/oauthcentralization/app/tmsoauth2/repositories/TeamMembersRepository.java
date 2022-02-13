package com.oauthcentralization.app.tmsoauth2.repositories;

import com.oauthcentralization.app.tmsoauth2.entities.TeamMembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMembersRepository extends JpaRepository<TeamMembersEntity, Integer> {

    boolean existsByTeamIdAndUserId(Integer teamId, Integer userId);

    boolean existsByUserId(Integer userId);
}
