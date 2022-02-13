package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.PhoneContacted;

public interface PhoneContactedRepository extends JpaRepository<PhoneContacted, Long>, JpaSpecificationExecutor<PhoneContacted> {

}
