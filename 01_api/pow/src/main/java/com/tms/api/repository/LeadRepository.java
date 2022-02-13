package com.tms.api.repository;

import com.tms.api.entity.ClFresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<ClFresh, Integer> {
    @Query(value = "from com.tms.entity.CLFresh a where a.cpId = ?1 and a.createDate >= ?2 ")
    List<ClFresh> getLeadByCreatedate(Integer cpId, Timestamp startDate);

    @Query("SELECT MAX(fresh.actualCall) FROM ClFresh fresh WHERE  fresh.leadId = ?1")
    BigDecimal findMaxActualCall(Integer leadId);

    @Query("FROM ClFresh fresh WHERE fresh.leadId = ?1")
    ClFresh findByLeadId(Integer leadId);
}
