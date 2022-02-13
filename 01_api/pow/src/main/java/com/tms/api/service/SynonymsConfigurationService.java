package com.tms.api.service;

import com.tms.api.request.SynonymsRequestDTO;
import com.tms.api.response.SynonymsConfigurationResponseDTO;

import java.util.List;

public interface SynonymsConfigurationService {

    boolean existsByTypeId(Integer typeId);

    boolean existsByNameAndTypeId(String name, Integer typeId);

    boolean existsByNameOrValue(String name, Integer value);

    SynonymsConfigurationResponseDTO findOne(Integer id);

    SynonymsConfigurationResponseDTO findOneByValueAndTypeId(Integer value, Integer typeId);

    List<SynonymsConfigurationResponseDTO> finByTypeId(Integer typeId);

    List<SynonymsConfigurationResponseDTO> snagSynonymsUnionBaseOnGEO(String currentCountry, List<SynonymsConfigurationResponseDTO> synonymsConfigurationResponseDTOS);

    SynonymsConfigurationResponseDTO createOne(SynonymsRequestDTO synonymsRequestDTO);

    List<SynonymsConfigurationResponseDTO> findByNameAndTypeId(String name, Integer typeId);

    Integer findMaxValueByTypeId(Integer typeId);
}
