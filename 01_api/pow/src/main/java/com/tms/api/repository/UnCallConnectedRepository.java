package com.tms.api.repository;

import com.tms.api.entity.UnCallConnected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnCallConnectedRepository extends JpaRepository<UnCallConnected, Integer> {

    @Query(value = "from UnCallConnected entity WHERE  entity.isUpdatePlaybackUrl = ?1 and entity.orgId = ?2")
    List<UnCallConnected> findUnCallConnectedsByIsUpdatePlaybackUrlAndOrgId(Integer isUpdatePlaybackUrl, Integer orgId);
}
