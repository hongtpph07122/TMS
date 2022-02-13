package com.tms.api.service.impl;

import com.tms.api.params.CampaignConfigurationsParams;
import com.tms.api.request.CampaignTypeRequestDTO;
import com.tms.api.request.SynonymsRequestDTO;
import com.tms.api.response.SynonymsConfigurationResponseDTO;
import com.tms.api.service.SynonymsConfigurationService;
import com.tms.api.service.TagsService;
import com.tms.api.utils.LoggerUtils;
import com.tms.api.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service(value = "tagsService")
@Transactional
public class TagsServiceImpl implements TagsService {

    private static final Logger logger = LoggerFactory.getLogger(TagsServiceImpl.class);
    private final SynonymsConfigurationService synonymsService;

    @Autowired
    public TagsServiceImpl(SynonymsConfigurationService synonymsService) {
        this.synonymsService = synonymsService;
    }


    @Override
    public boolean existsByNameOrValue(String name, Integer value) {
        return synonymsService.existsByNameOrValue(name, value);
    }

    @Override
    public void createOneTag(SynonymsRequestDTO synonymTagsRequest) {
        SynonymsConfigurationResponseDTO tagsResponse = synonymsService.createOne(synonymTagsRequest);
        logger.info("body tags is created response : {} - success ", LoggerUtils.snagAsLogJson(tagsResponse));
    }

    @Override
    public SynonymsConfigurationResponseDTO createOneCampaignType(CampaignTypeRequestDTO campaignTypeRequestDTO) {
        SynonymsRequestDTO synonymsRequestDTO = new SynonymsRequestDTO();
        synonymsRequestDTO.setName(campaignTypeRequestDTO.getName());
        synonymsRequestDTO.setType(CampaignConfigurationsParams.snagCampaignTypeDefault().get(0).getType());
        synonymsRequestDTO.setTypeId(CampaignConfigurationsParams.snagCampaignTypeDefault().get(0).getTypeId());
        if (!ObjectUtils.allNotNull(campaignTypeRequestDTO.getValue())) {
            Integer value = synonymsService.findMaxValueByTypeId(CampaignConfigurationsParams.snagCampaignTypeDefault().get(0).getTypeId());
            if (!ObjectUtils.allNotNull(value)) {
                synonymsRequestDTO.setValue(1);
            } else {
                synonymsRequestDTO.setValue(value + 1);
            }
        } else {
            synonymsRequestDTO.setValue(campaignTypeRequestDTO.getValue());
        }
        return synonymsService.createOne(synonymsRequestDTO);
    }

    @Override
    public SynonymsConfigurationResponseDTO createOneCampaignCategory(CampaignTypeRequestDTO campaignTypeRequestDTO) {
        SynonymsRequestDTO synonymsRequestDTO = new SynonymsRequestDTO();
        synonymsRequestDTO.setName(campaignTypeRequestDTO.getName());
        synonymsRequestDTO.setType(CampaignConfigurationsParams.snagCampaignCategoriesDefault().get(0).getType());
        synonymsRequestDTO.setTypeId(CampaignConfigurationsParams.snagCampaignCategoriesDefault().get(0).getTypeId());
        if (!ObjectUtils.allNotNull(campaignTypeRequestDTO.getValue())) {
            Integer value = synonymsService.findMaxValueByTypeId(CampaignConfigurationsParams.snagCampaignCategoriesDefault().get(0).getTypeId());
            if (!ObjectUtils.allNotNull(value)) {
                synonymsRequestDTO.setValue(1);
            } else {
                synonymsRequestDTO.setValue(value + 1);
            }
        } else {
            synonymsRequestDTO.setValue(campaignTypeRequestDTO.getValue());
        }
        return synonymsService.createOne(synonymsRequestDTO);
    }

    @Override
    public SynonymsConfigurationResponseDTO createOneCampaignTag(CampaignTypeRequestDTO campaignTypeRequestDTO) {
        SynonymsRequestDTO synonymsRequestDTO = new SynonymsRequestDTO();
        String name = campaignTypeRequestDTO.getName();
        synonymsRequestDTO.setType(CampaignConfigurationsParams.snagCampaignTagsDefault().get(0).getType());
        synonymsRequestDTO.setTypeId(CampaignConfigurationsParams.snagCampaignTagsDefault().get(0).getTypeId());
        if (!ObjectUtils.allNotNull(campaignTypeRequestDTO.getValue())) {
            Integer value = synonymsService.findMaxValueByTypeId(CampaignConfigurationsParams.snagCampaignTagsDefault().get(0).getTypeId());
            if (!ObjectUtils.allNotNull(value)) {
                synonymsRequestDTO.setValue(1);
            } else {
                synonymsRequestDTO.setValue(value + 1);
            }
        } else {
            synonymsRequestDTO.setValue(campaignTypeRequestDTO.getValue());
        }

        if (name.contains(",")) {
            synonymsRequestDTO.setName(name.replace(",", "|"));
        } else {
            synonymsRequestDTO.setName(campaignTypeRequestDTO.getName()); /* check if */
        }
        return synonymsService.createOne(synonymsRequestDTO);
    }
}
