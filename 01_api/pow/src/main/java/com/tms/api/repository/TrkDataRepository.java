package com.tms.api.repository;

import com.tms.api.entity.TrkData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrkDataRepository extends JpaRepository<TrkData,Integer> {
	

}
