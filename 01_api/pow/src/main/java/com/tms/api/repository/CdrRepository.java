package com.tms.api.repository;

import com.tms.api.entity.CdrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CdrRepository extends JpaRepository<CdrEntity, Integer> {

    @Query(value = "from CdrEntity cdrEntity WHERE  cdrEntity.channel = ?1 and cdrEntity.createtime >= ?2")
    List<CdrEntity> findOneByChannel(String channel, Timestamp createTime);

    @Query(value = "from CdrEntity cdrEntity WHERE  cdrEntity.channel in ?1 and cdrEntity.createtime >= ?2")
    List<CdrEntity> findByChannel(List<String> channels, Timestamp createTime);
}
