package com.tms.api.repository;

import com.tms.api.entity.RcRescueJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RcRescueJobRepository extends JpaRepository<RcRescueJob, Integer> {

}
