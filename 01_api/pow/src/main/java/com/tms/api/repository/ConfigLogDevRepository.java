package com.tms.api.repository;

import com.tms.api.entity.ConfigLogDevEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ConfigLogDevRepository extends JpaRepository<ConfigLogDevEntity, Long> {

    boolean existsById(Long id);

    boolean existsByValue(Integer value);

    @Query("SELECT MAX(configDev.value) FROM ConfigLogDevEntity configDev")
    BigDecimal findMaxValue();

    @Query("FROM ConfigLogDevEntity configDev WHERE configDev.value = ?1")
    ConfigLogDevEntity findByValue(Integer value);

    @Query("FROM ConfigLogDevEntity configDev ORDER BY configDev.value DESC")
    List<ConfigLogDevEntity> findAll();
}
