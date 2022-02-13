package com.tms.api.dto;

import com.tms.api.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubCallingListDto {
	private final Logger logger = LoggerFactory.getLogger(PubCallingListDto.class);
	
	private final Integer agencyId;
	private final String pubId;
	private final String offerId;
	private final String[] callingListIds;
	
	private Integer callingListIdIndex;

	public String getPubId() {
		return pubId;
	}
	public Integer getAgencyId() {
		return agencyId;
	}
	public String getOfferId() {
		return offerId;
	}
	public String[] getCallingListIds() {
		return callingListIds;
	}

	public PubCallingListDto(Integer agencyId, String pubId, String offerId, String[] callingListIds) {
		this.agencyId = agencyId;
		this.pubId = pubId;
		this.offerId = offerId;
		this.callingListIds = callingListIds;
		this.callingListIdIndex = Helper.getRandomNumberInRange(0, callingListIds.length);
	}
	
	public Integer nextCallingListId() {
		if (callingListIdIndex < 0 || callingListIdIndex >= callingListIds.length) {
			callingListIdIndex = 0;
		}
		Integer nextCallingListId = Integer.parseInt(callingListIds[callingListIdIndex]);
		callingListIdIndex++;
		if (callingListIdIndex == callingListIds.length) {
			callingListIdIndex = 0;
		}
		
		logger.info("  NextCallingListIdIndex={}", callingListIdIndex);
		
		return nextCallingListId;
	}
	
}
