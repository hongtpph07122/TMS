package com.tms.api.config.initializeConfigurer;

import com.tms.api.params.CampaignConfigurationsParams;
import com.tms.api.request.SynonymsRequestDTO;
import com.tms.api.service.SynonymsConfigurationService;
import com.tms.api.service.TagsService;
import com.tms.api.utils.LoggerUtils;
import com.tms.api.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class InitializeCampaignsConfigurer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitializeCampaignsConfigurer.class);
    private final TagsService tagsService;
    private final SynonymsConfigurationService synonymsService;

    @Value("${config.campaign-types.activation-value}")
    private boolean activationValueCampaignTypes;

    @Value("${config.campaign-category.activation-value}")
    private boolean activationValueCampaignCategory;

    @Value("${config.campaign-tags.activation-value}")
    private boolean activationValueCampaignTag;

    @Autowired
    public InitializeCampaignsConfigurer(TagsService tagsService, SynonymsConfigurationService synonymsService) {
        this.tagsService = tagsService;
        this.synonymsService = synonymsService;
    }

    @Override
    public void run(String... args) {
        runActivationCampaignTypesConfig();
        runActivationCampaignCategoriesConfig();
        runActivationCampaignTagsConfig();
    }

    private void runActivationCampaignTypesConfig() {
        if (ObjectUtils.allNotNull(activationValueCampaignTypes)) {
            if (activationValueCampaignTypes) {
                runActionCampaignCoreConfig(CampaignConfigurationsParams.snagCampaignTypeDefault(), "types");
            } else {
                logger.info("{} : {} - closed", "campaign-types mode", false);
            }
        }
    }

    private void runActivationCampaignCategoriesConfig() {
        if (ObjectUtils.allNotNull(activationValueCampaignCategory)) {
            if (activationValueCampaignCategory) {
                runActionCampaignCoreConfig(CampaignConfigurationsParams.snagCampaignCategoriesDefault(), "categories");
            } else {
                logger.info("{} : {} - closed", "campaign-categories mode", false);
            }
        }
    }

    private void runActivationCampaignTagsConfig() {
        if (ObjectUtils.allNotNull(activationValueCampaignTag)) {
            if (activationValueCampaignTag) {
                runActionCampaignCoreConfig(CampaignConfigurationsParams.snagCampaignTagsDefault(), "tags");
            } else {
                logger.info("{} : {} - closed", "campaign-tags mode", false);
            }
        }
    }

    private void runActionCampaignCoreConfig(List<SynonymsRequestDTO> synonymsRequestDTOS, String campaignObject) {
        int counter = 0;
        logger.info("{} : {} - opening", "campaign-@obj mode".replace("@obj", campaignObject.toLowerCase()), true);
        /* begin */
        logger.info("campaign {} default request: {}", campaignObject.toLowerCase(), LoggerUtils.snagAsLogJson(synonymsRequestDTOS));
        for (SynonymsRequestDTO synonymsRequestDTO : synonymsRequestDTOS) {
            if (!synonymsService.existsByNameAndTypeId(synonymsRequestDTO.getName(), synonymsRequestDTO.getTypeId())) {
                tagsService.createOneTag(synonymsRequestDTO);
                counter++;
            }
        }
        logger.info("campaign {} default with total : {}", campaignObject.toLowerCase(), counter);
        logger.info("{}", "success [loadDataCampaign@OBJConfig] - InitializeTypeCampaignsConfigurer".replace("@OBJ", campaignObject.toUpperCase()));
    }
}
