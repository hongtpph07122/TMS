package com.tms.api.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.dto.DBResponse;
import com.tms.dto.GetCampaign;
import com.tms.entity.CPCampaign;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LCProvinceService;
import com.tms.service.impl.LogService;

@Component
public class TMSValidation {
	@Autowired
	CLFreshService freshService;
	
	/*@Autowired
	LCProvinceService provinceService;

	@Autowired
	LogService logService;*/
	
	public CPCampaign validateCampaignId(int campaignId) throws TMSException {
		GetCampaign campaignParams = new GetCampaign();
		campaignParams.setCpId(campaignId);
		DBResponse<List<CPCampaign>> dbResponse = freshService.getCampaign(Helper.getSessionId(), campaignParams);
		if (dbResponse.getResult().isEmpty()) {
			throw new TMSException(ErrorMessage.CAMPAIGN_NOT_FOUND);
		}
		return dbResponse.getResult().get(0);
	}
	
	public int validatePageSize(Integer limit) {
	    if (limit == null || limit <= 0) {
            limit = Const.DEFAULT_PAGE_SIZE;
        }
	    return limit;
	}
	
	public int validatePage(Integer offset) {
	    if (offset == null || offset < 0) {
            offset = Const.DEFAULT_PAGE;
        }
        return offset;
    }
}
