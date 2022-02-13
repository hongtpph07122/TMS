package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.tms.api.entity.PdCategory;

import java.util.List;

public interface PdCategoryRepository extends JpaRepository<PdCategory, Integer>{
    PdCategory getById(Integer id);
    List<PdCategory> getByOrgId(Integer orgId);
}