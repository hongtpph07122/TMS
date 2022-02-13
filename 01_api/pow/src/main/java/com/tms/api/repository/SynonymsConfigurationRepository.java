package com.tms.api.repository;

import com.tms.api.entity.SynonymsConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SynonymsConfigurationRepository extends JpaRepository<SynonymsConfigurationEntity, Integer> {

    boolean existsByTypeId(Integer typeId);

    boolean existsByNameLikeAndTypeId(String name, Integer typeId);

    boolean existsByNameLikeAndValue(String name, Integer value);

    @Query("select synonym from SynonymsConfigurationEntity synonym where synonym.value =?1 AND synonym.typeId =?2 ")
    SynonymsConfigurationEntity findByValueAndType(Integer value, int typeId);

    @Query("select synonym from SynonymsConfigurationEntity synonym where synonym.typeId =?1 ")
    List<SynonymsConfigurationEntity> findByTypeId(int typeId);

    @Query("select MAX(synonym.id) from SynonymsConfigurationEntity synonym ")
    BigDecimal findMaxSynonyms();

    @Query("select MAX(synonym.value) from SynonymsConfigurationEntity synonym WHERE synonym.typeId = ?1")
    Integer findMaxValueByTypeId(Integer typeId);

    List<SynonymsConfigurationEntity> findByNameLikeAndTypeId(String name, Integer typeId);
}
