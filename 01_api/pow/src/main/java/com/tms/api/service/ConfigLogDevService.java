package com.tms.api.service;

import com.tms.api.request.ConfigLogDevRequestDTO;
import com.tms.api.response.ConfigLogDevResponseDTO;

import java.util.List;

public interface ConfigLogDevService {

    ConfigLogDevResponseDTO createOne(ConfigLogDevRequestDTO configLogDevRequest);

    ConfigLogDevResponseDTO updateOne(Long id, ConfigLogDevResponseDTO configLogDevResponse);

    List<ConfigLogDevResponseDTO> findAll();

    ConfigLogDevResponseDTO findOne(Long id);

    ConfigLogDevResponseDTO findOneByValue(Integer value);

    boolean existById(Long id);

    boolean existByValue(Integer value);

    boolean deleteById(Long id);


}
