package com.tms.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tms.api.dto.PubCallingListDto;
import com.tms.api.service.BasketProcessorService;
import com.tms.entity.CLBasket;

@Service
public class BasketProcessorServiceImpl implements BasketProcessorService {
	private static Map<String, PubCallingListDto> pubCallingListRoundRobin = new HashMap<>();
	
    public Integer getSmartLoadCallingList(CLBasket basket) {
    	String robinKey = String.format("%s:%s:%s", basket.getAgcId(), basket.getAffiliateId(), basket.getOfferId());
    	
    	//get callinglist with key (agencyId + pubId + offerId)
    	PubCallingListDto pubCallingListDto = pubCallingListRoundRobin.get(robinKey);
    	
    	//if callinglist null then create new callingList
    	if (pubCallingListDto == null) {
    		String[] callingListIds = basket.getAttribute2().split("\\s*,\\s*");
    		pubCallingListDto = new PubCallingListDto(basket.getAgcId(), basket.getAffiliateId(), basket.getOfferId(), callingListIds);
    		pubCallingListRoundRobin.put(robinKey, pubCallingListDto);
    	}

    	return pubCallingListDto.nextCallingListId();	
    }
    
    public Integer getDefaultCallingList(String callingListStr)
    {
    	Integer callingListId = -1;
    	String[] callingListIds = callingListStr.split("\\s*,\\s*");
    	if (callingListIds.length >= 1) {
    		return Integer.parseInt(callingListIds[0]);
    	}
    	return callingListId;
    }
    
    public void resetRoundRobinRule() {
    	pubCallingListRoundRobin.clear();
    }
}