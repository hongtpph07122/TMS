package com.tms.api.service.impl;

import com.tms.api.entity.ConfigLogDevEntity;
import com.tms.api.repository.ConfigLogDevRepository;
import com.tms.api.request.ConfigLogDevRequestDTO;
import com.tms.api.response.ConfigLogDevResponseDTO;
import com.tms.api.service.ConfigLogDevService;
import com.tms.api.utils.ObjectUtils;
import com.tms.api.utils.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "configDevService")
@Transactional
public class ConfigLogDevServiceImpl implements ConfigLogDevService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigLogDevServiceImpl.class);
    private final ConfigLogDevRepository configLogDevRepository;

    @Autowired
    public ConfigLogDevServiceImpl(ConfigLogDevRepository configLogDevRepository) {
        this.configLogDevRepository = configLogDevRepository;
    }


    @Override
    public ConfigLogDevResponseDTO createOne(ConfigLogDevRequestDTO configLogDevRequest) {
        ConfigLogDevEntity configLogDevEntity = new ConfigLogDevEntity();
        BigDecimal value = configLogDevRepository.findMaxValue();
        configLogDevEntity.setDescription(StringUtility.trimSingleWhitespace(StringUtility.uppercaseFirstChar(configLogDevRequest.getDescription())));
        configLogDevEntity.setActive(!ObjectUtils.allNotNull(configLogDevRequest.getActive()) ? false : configLogDevRequest.getActive());
        configLogDevEntity.setModule(StringUtility.trimSingleWhitespace(StringUtility.uppercaseFirstChar(configLogDevRequest.getModule())));

        if (ObjectUtils.allNotNull(value)) {
            configLogDevEntity.setValue(value.intValue() + 1);
        } else {
            configLogDevEntity.setValue(1);
        }
        configLogDevRepository.save(configLogDevEntity);
        return findOne(configLogDevEntity);
    }

    @Override
    public ConfigLogDevResponseDTO updateOne(Long id, ConfigLogDevResponseDTO configLogDevResponse) {
        ConfigLogDevEntity configLogDevEntity = new ConfigLogDevEntity();
        configLogDevEntity.setId(id);
        configLogDevEntity.setDescription(StringUtility.trimSingleWhitespace(StringUtility.uppercaseFirstChar(configLogDevResponse.getDescription())));
        configLogDevEntity.setActive(configLogDevResponse.getActive());
        configLogDevEntity.setModule(StringUtility.trimSingleWhitespace(StringUtility.uppercaseFirstChar(configLogDevResponse.getModule())));
        configLogDevRepository.save(configLogDevEntity);
        return findOne(configLogDevEntity);
    }

    @Override
    public List<ConfigLogDevResponseDTO> findAll() {
        List<ConfigLogDevEntity> configLogDevEntities = configLogDevRepository.findAll();
        return configLogDevEntities.stream().map(this::findOne).collect(Collectors.toList());
    }

    @Override
    public ConfigLogDevResponseDTO findOne(Long id) {
        ConfigLogDevEntity configLogDevEntity = configLogDevRepository.getOne(id);
        return findOne(configLogDevEntity);
    }

    private ConfigLogDevResponseDTO findOne(ConfigLogDevEntity configLogDevEntity) {
        ConfigLogDevResponseDTO configLogDevResponseDTO = new ConfigLogDevResponseDTO();
        configLogDevResponseDTO.setId(configLogDevEntity.getId());
        configLogDevResponseDTO.setDescription(configLogDevEntity.getDescription());
        configLogDevResponseDTO.setModule(configLogDevEntity.getModule());
        configLogDevResponseDTO.setValue(configLogDevEntity.getValue());
        configLogDevResponseDTO.setActive(configLogDevEntity.getActive());
        return configLogDevResponseDTO;
    }

    @Override
    public ConfigLogDevResponseDTO findOneByValue(Integer value) {
        ConfigLogDevEntity configLogDevEntity = configLogDevRepository.findByValue(value);
        return findOne(configLogDevEntity);
    }

    @Override
    public boolean existById(Long id) {
        if (!ObjectUtils.allNotNull(id)) {
            return false;
        }
        return configLogDevRepository.existsById(id);
    }

    @Override
    public boolean existByValue(Integer value) {
        if (!ObjectUtils.allNotNull(value)) {
            return false;
        }
        return configLogDevRepository.existsByValue(value);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!existById(id)) {
            return false;
        }
        configLogDevRepository.deleteById(id);
        return true;
    }
}
