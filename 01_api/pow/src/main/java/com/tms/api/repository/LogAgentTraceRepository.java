package com.tms.api.repository;

import com.tms.api.entity.LogAgentTrace;
import com.tms.api.entity.OrUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface LogAgentTraceRepository extends JpaRepository<LogAgentTrace, Long>, JpaSpecificationExecutor<LogAgentTrace> {
    // To get activities, agent ID is required //
    @Query(value = "SELECT lat.* FROM log_agent_trace lat " +
            "JOIN or_user u ON lat.agent_id = u.user_id " +
            "WHERE lat.agent_id = :agent_id AND lat.activity_id = :activity_id " +
            "ORDER BY lat.action_time DESC LIMIT 1", nativeQuery = true)
    LogAgentTrace getLatestActivity(@Param("agent_id") Integer agentId, @Param("activity_id") Integer activityId);

    @Query(value = "SELECT lat.* FROM log_agent_trace lat " +
            "WHERE lat.agent_id = :agent_id AND lat.activity_id = :activity_id " +
            "ORDER BY lat.action_time DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<LogAgentTrace> getLatestActivities(@Param("agent_id") Integer agentId, @Param("activity_id") Integer activityId,
                                             @Param("limit") Integer limit, @Param("offset") Integer offset);


    @Query(value = "SELECT lat.* FROM log_agent_trace lat " +
            "WHERE lat.agent_id = :agent_id AND lat.activity_id = :activity_id AND lat.action_time BETWEEN :date_from AND :date_to " +
            "ORDER BY lat.action_time ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<LogAgentTrace> getOldestActivities(@Param("agent_id") Integer agentId, @Param("activity_id") Integer activityId,
                                            @Param("limit") Integer limit, @Param("offset") Integer offset,
                                            @Param("date_from") Date dateFrom, @Param("date_to") Date dateTo);

    @Query(value = "SELECT lat.* FROM log_agent_trace lat " +
            "WHERE lat.agent_id = :agent_id AND lat.activity_id = :activity_id " +
            "ORDER BY lat.action_time ASC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<LogAgentTrace> getOldestActivities(@Param("agent_id") Integer agentId, @Param("activity_id") Integer activityId,
                                            @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(value = "SELECT u.* FROM or_user u " +
            "JOIN cl_fresh cf ON cf.assigned = u.user_id " +
            "WHERE u.phone LIKE :sip AND u.user_lock <> 1 AND cf.phone LIKE :phone", nativeQuery = true)
    List<OrUser> getAgent(@Param("sip") String sip, @Param("phone") String phone);
}