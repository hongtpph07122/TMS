package com.oauthcentralization.app.tmsoauth2.repositories;

import com.oauthcentralization.app.tmsoauth2.entities.GroupsAssigneeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsAssigneeRepository extends JpaRepository<GroupsAssigneeEntity, Integer> {

    boolean existsByAgentIdAndGroupId(Integer agentId, Integer groupId);
}
