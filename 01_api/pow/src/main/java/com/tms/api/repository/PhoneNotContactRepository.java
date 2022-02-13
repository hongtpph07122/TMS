package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.PhoneNotContact;

public interface PhoneNotContactRepository extends JpaRepository<PhoneNotContact, Long>, JpaSpecificationExecutor<PhoneNotContact> {

}
