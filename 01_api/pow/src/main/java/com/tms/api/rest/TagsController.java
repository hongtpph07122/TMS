package com.tms.api.rest;

import com.tms.api.request.CampaignTypeRequestDTO;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/tags")
public class TagsController {

    private final TagsService tagsService;

    public TagsController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @PostMapping("/campaign-type/new")
    public @ResponseBody
    ResponseEntity<?> createOneCampaignTypes(@RequestBody CampaignTypeRequestDTO campaignTypeRequest) {
        if (StringUtils.isEmpty(campaignTypeRequest.getName())) {
            return new ResponseEntity<>(TMSResponse.buildApplicationException("Name is null", 400), HttpStatus.BAD_REQUEST);
        }

        if (tagsService.existsByNameOrValue(campaignTypeRequest.getName(), campaignTypeRequest.getValue())) {
            return new ResponseEntity<>(TMSResponse.buildApplicationException("Name and value is existing", 400), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(tagsService.createOneCampaignType(campaignTypeRequest), 1), HttpStatus.OK);
    }

    @PostMapping("/campaign-category/new")
    public @ResponseBody
    ResponseEntity<?> createOneCampaignCategories(@RequestBody CampaignTypeRequestDTO campaignTypeRequest) {
        if (StringUtils.isEmpty(campaignTypeRequest.getName())) {
            return new ResponseEntity<>(TMSResponse.buildApplicationException("Name is null", 400), HttpStatus.BAD_REQUEST);
        }

        if (tagsService.existsByNameOrValue(campaignTypeRequest.getName(), campaignTypeRequest.getValue())) {
            return new ResponseEntity<>(TMSResponse.buildApplicationException("Name and value is existing", 400), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(tagsService.createOneCampaignCategory(campaignTypeRequest), 1), HttpStatus.OK);
    }

    @PostMapping("/campaign-tag/new")
    public @ResponseBody
    ResponseEntity<?> createOneCampaignTags(@RequestBody CampaignTypeRequestDTO campaignTypeRequest) {
        if (StringUtils.isEmpty(campaignTypeRequest.getName())) {
            return new ResponseEntity<>(TMSResponse.buildApplicationException("Name is null", 400), HttpStatus.BAD_REQUEST);
        }

        if (tagsService.existsByNameOrValue(campaignTypeRequest.getName(), campaignTypeRequest.getValue())) {
            return new ResponseEntity<>(TMSResponse.buildApplicationException("Name and value is existing", 400), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(tagsService.createOneCampaignTag(campaignTypeRequest), 1), HttpStatus.OK);
    }

}
