package com.tms.api.service;

import com.tms.api.request.CampaignTypeRequestDTO;
import com.tms.api.request.SynonymsRequestDTO;
import com.tms.api.response.SynonymsConfigurationResponseDTO;

public interface TagsService {

    boolean existsByNameOrValue(String name, Integer value);

    void createOneTag(SynonymsRequestDTO synonymTagsRequest);

    SynonymsConfigurationResponseDTO createOneCampaignType(CampaignTypeRequestDTO campaignTypeRequestDTO);

    SynonymsConfigurationResponseDTO createOneCampaignCategory(CampaignTypeRequestDTO campaignTypeRequestDTO);

    SynonymsConfigurationResponseDTO createOneCampaignTag(CampaignTypeRequestDTO campaignTypeRequestDTO);
}
