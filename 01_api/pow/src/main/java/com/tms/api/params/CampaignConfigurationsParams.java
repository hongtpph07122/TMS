package com.tms.api.params;

import com.tms.api.request.SynonymsRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class CampaignConfigurationsParams {

    public static List<SynonymsRequestDTO> snagCampaignTypeDefault() {
        List<SynonymsRequestDTO> synonymsRequestDTOS = new ArrayList<>();
        final Integer typeIds = 40;
        final String type = "campaign types";
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "Fresh", 1, typeIds));
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "Resell", 2, typeIds));
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "Digital", 3, typeIds));
        return synonymsRequestDTOS;
    }

    public static List<SynonymsRequestDTO> snagCampaignCategoriesDefault() {
        List<SynonymsRequestDTO> synonymsRequestDTOS = new ArrayList<>();
        final Integer typeIds = 41;
        final String type = "campaign categories";
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "ME", 1, typeIds));
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "HC", 2, typeIds));
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "Slimming", 3, typeIds));
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "Hair", 4, typeIds));
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "Other", 5, typeIds));
        return synonymsRequestDTOS;
    }

    public static List<SynonymsRequestDTO> snagCampaignTagsDefault() {
        List<SynonymsRequestDTO> synonymsRequestDTOS = new ArrayList<>();
        final Integer typeIds = 42;
        final String type = "campaign tags";
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "OS", 1, typeIds));
        synonymsRequestDTOS.add(new SynonymsRequestDTO(type, "OS|IS", 2, typeIds));
        return synonymsRequestDTOS;
    }
}
