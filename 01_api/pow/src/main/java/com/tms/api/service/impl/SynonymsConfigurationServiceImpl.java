package com.tms.api.service.impl;

import com.tms.api.entity.SynonymsConfigurationEntity;
import com.tms.api.repository.SynonymsConfigurationRepository;
import com.tms.api.request.SynonymsRequestDTO;
import com.tms.api.response.SynonymsConfigurationResponseDTO;
import com.tms.api.service.SynonymsConfigurationService;
import com.tms.api.utils.LoggerUtils;
import com.tms.api.utils.ObjectUtils;
import com.tms.api.utils.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "synonymsService")
@Transactional
public class SynonymsConfigurationServiceImpl implements SynonymsConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(SynonymsConfigurationServiceImpl.class);

    private final SynonymsConfigurationRepository synonymsConfigurationRepository;

    @Autowired
    public SynonymsConfigurationServiceImpl(SynonymsConfigurationRepository synonymsConfigurationRepository) {
        this.synonymsConfigurationRepository = synonymsConfigurationRepository;
    }

    @Override
    public SynonymsConfigurationResponseDTO findOne(Integer id) {
        SynonymsConfigurationEntity synonymsConfigurationEntity = synonymsConfigurationRepository.getOne(id);
        SynonymsConfigurationResponseDTO synonymsConfigurationResponseDTO = new SynonymsConfigurationResponseDTO();
        synonymsConfigurationResponseDTO.setId(synonymsConfigurationEntity.getId());
        synonymsConfigurationResponseDTO.setType(synonymsConfigurationEntity.getType());
        synonymsConfigurationResponseDTO.setName(synonymsConfigurationEntity.getName());
        synonymsConfigurationResponseDTO.setValue(synonymsConfigurationEntity.getValue());
        return synonymsConfigurationResponseDTO;
    }

    @Override
    public SynonymsConfigurationResponseDTO findOneByValueAndTypeId(Integer value, Integer typeId) {
        SynonymsConfigurationEntity synonymsConfigurationEntity = synonymsConfigurationRepository.findByValueAndType(value, typeId);
        return findOne(synonymsConfigurationEntity.getId());
    }

    @Override
    public List<SynonymsConfigurationResponseDTO> finByTypeId(Integer typeId) {
        List<SynonymsConfigurationEntity> synonymsConfigurationEntities = synonymsConfigurationRepository.findByTypeId(typeId);
        return synonymsConfigurationEntities.stream().map(synonymsConfigurationEntity -> findOne(synonymsConfigurationEntity.getId())).collect(Collectors.toList());
    }

    @Override
    public List<SynonymsConfigurationResponseDTO> snagSynonymsUnionBaseOnGEO(String currentCountry, List<SynonymsConfigurationResponseDTO> synonymsConfigurationResponseDTOS) {

        List<SynonymsConfigurationResponseDTO> synonymsConfigurationResponse = new ArrayList<>();

        if (StringUtils.isEmpty(currentCountry)) {
            logger.warn("{}", "no current GEO");
            return synonymsConfigurationResponseDTOS;
        }

        if (CollectionUtils.isEmpty(synonymsConfigurationResponseDTOS)) {
            logger.warn("{}", "list synonyms not found!");
            return Collections.emptyList();
        }

        switch (currentCountry.trim().toLowerCase()) {
            case "vn":
                for (SynonymsConfigurationResponseDTO synonymsConfigurationResponseDTO : synonymsConfigurationResponseDTOS) {
                    if (!synonymsConfigurationResponseDTO.getName().toLowerCase().contains("ESPay".toLowerCase()) &&
                            !synonymsConfigurationResponseDTO.getName().toLowerCase().contains("2C2P".toLowerCase())) {
                        synonymsConfigurationResponse.add(synonymsConfigurationResponseDTO);
                    }
                }
                return synonymsConfigurationResponse;
            case "tl":
            case "th":
                for (SynonymsConfigurationResponseDTO synonymsConfigurationResponseDTO : synonymsConfigurationResponseDTOS) {
                    if (!synonymsConfigurationResponseDTO.getName().toLowerCase().contains("ESPay".toLowerCase())) {
                        synonymsConfigurationResponse.add(synonymsConfigurationResponseDTO);
                    }
                }
                return synonymsConfigurationResponse;
            case "id":
            case "indo":
                for (SynonymsConfigurationResponseDTO synonymsConfigurationResponseDTO : synonymsConfigurationResponseDTOS) {
                    if (!synonymsConfigurationResponseDTO.getName().toLowerCase().contains("2C2P".toLowerCase())) {
                        synonymsConfigurationResponse.add(synonymsConfigurationResponseDTO);
                    }
                }
                return synonymsConfigurationResponse;
            default:
                logger.error("{}", "error out of geo");
                break;
        }

        return Collections.emptyList();
    }

    @Override
    public SynonymsConfigurationResponseDTO createOne(SynonymsRequestDTO synonymsRequestDTO) {
        SynonymsConfigurationEntity synonymsConfigurationEntity = new SynonymsConfigurationEntity();
        BigDecimal synonymsConfigurationEntityMax = synonymsConfigurationRepository.findMaxSynonyms();
        synonymsConfigurationEntity.setType(StringUtility.trimSingleWhitespace(synonymsRequestDTO.getType()));
        synonymsConfigurationEntity.setName(StringUtility.trimSingleWhitespace(synonymsRequestDTO.getName()));
        synonymsConfigurationEntity.setValue(synonymsRequestDTO.getValue());
        synonymsConfigurationEntity.setTypeId(synonymsRequestDTO.getTypeId());
        synonymsConfigurationEntity.setDscr(null);
        synonymsConfigurationEntity.setId((int) synonymsConfigurationEntityMax.doubleValue() + 1);
        synonymsConfigurationRepository.save(synonymsConfigurationEntity);
        return findOne(synonymsConfigurationEntity.getId());
    }

    @Override
    public boolean existsByTypeId(Integer typeId) {
        if (!ObjectUtils.allNotNull(typeId)) {
            return false;
        }
        return synonymsConfigurationRepository.existsByTypeId(typeId);
    }

    @Override
    public boolean existsByNameAndTypeId(String name, Integer typeId) {
        if (!ObjectUtils.allNotNull(typeId) || StringUtils.isEmpty(name)) {
            return false;
        }
        return synonymsConfigurationRepository.existsByNameLikeAndTypeId(StringUtility.trimSingleWhitespace(name), typeId);
    }

    @Override
    public boolean existsByNameOrValue(String name, Integer value) {
        if (StringUtils.isEmpty(name) || !ObjectUtils.allNotNull(value)) {
            return false;
        }
        return synonymsConfigurationRepository.existsByNameLikeAndValue(StringUtility.trimSingleWhitespace(name), value);
    }

    @Override
    public List<SynonymsConfigurationResponseDTO> findByNameAndTypeId(String name, Integer typeId) {
        if (StringUtils.isEmpty(name) || !ObjectUtils.allNotNull(typeId)) {
            return Collections.emptyList();
        }
        List<SynonymsConfigurationEntity> synonymsConfigurationEntities = synonymsConfigurationRepository.findByNameLikeAndTypeId(StringUtility.trimSingleWhitespace(name), typeId);
        return synonymsConfigurationEntities.stream().map(synonymsConfigurationEntity -> findOne(synonymsConfigurationEntity.getId())).collect(Collectors.toList());
    }

    @Override
    public Integer findMaxValueByTypeId(Integer typeId) {
        if (!ObjectUtils.allNotNull(typeId)) {
            return null;
        }
        return synonymsConfigurationRepository.findMaxValueByTypeId(typeId);
    }
}
