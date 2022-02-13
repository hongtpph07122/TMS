package com.tms.dao;

import com.tms.model.RcLastmileStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface LastmileStatusRepository extends CrudRepository<RcLastmileStatus, Long>, JpaSpecificationExecutor<RcLastmileStatus> {

}
