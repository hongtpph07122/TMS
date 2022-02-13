package com.tms.api.service;

import com.tms.entity.CLBasket;

public interface BasketProcessorService {
	public Integer getSmartLoadCallingList(CLBasket basket);
	public Integer getDefaultCallingList(String callingListStr);
	public void resetRoundRobinRule();
}