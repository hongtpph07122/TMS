package com.tms.api.repository;

import com.tms.api.entity.LogToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogTokenRepository extends JpaRepository<LogToken,Integer> {
	

}
