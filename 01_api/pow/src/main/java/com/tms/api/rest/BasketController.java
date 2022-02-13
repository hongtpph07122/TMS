package com.tms.api.rest;

import com.tms.api.dto.CreateBasketDto;
import com.tms.api.exception.TMSException;
import com.tms.api.response.TMSResponse;
import com.tms.dto.DBResponse;
import com.tms.entity.log.InsCLBasketV2;
import com.tms.entity.log.InsCLBasketV3;
import com.tms.service.impl.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tms.service.impl.CLFreshService;
import com.tms.dto.GetOffer;
import com.tms.dto.GetOfferResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/basket")
@SuppressWarnings("unchecked")
public class BasketController {
    Logger logger = LoggerFactory.getLogger(BasketController.class);

    @Autowired
    LogService logceService;

    @Autowired
    CLFreshService freshService;

    @PostMapping
    public TMSResponse createBasket(CreateBasketDto basketDto) throws TMSException {
        String sessionId = UUID.randomUUID().toString();
        String offerId = basketDto.getOfferId();
        String affliate = basketDto.getAffiliate();

        String offer = offerId;
        String source = "admain";
        if (affliate != null && !affliate.isEmpty())
            offer = affliate;
        InsCLBasketV2 basketParams = new InsCLBasketV2();
        basketParams.setName(basketDto.getName());
        basketParams.setPhone(basketDto.getPhone());
        basketParams.setClickId(basketDto.getClickId());
        basketParams.setOfferId(offer);
        basketParams.setPid(basketDto.getPid());

        if (basketDto.getAdf_source() != null && !basketDto.getAdf_source().isEmpty())
            source = basketDto.getAdf_source();
        basketParams.setAgKey(source);
        DBResponse result = logceService.insBasketV2(sessionId, basketParams);
        if (result.getErrorCode() == 0) {
            return TMSResponse.buildResponse(true);
        }
        return TMSResponse.buildResponse(false);
    }


    @PostMapping("/24")
    public TMSResponse createBasket24Scan(CreateBasketDto basketDto) throws TMSException {
        String sessionId = UUID.randomUUID().toString();
        String offerId = basketDto.getOfferId();
        String affliate = basketDto.getAffiliate();

        String offer = offerId;
        String source = "admain";
        if (affliate != null && !affliate.isEmpty())
            offer = affliate;
        InsCLBasketV3 basketParams = new InsCLBasketV3();
        basketParams.setName(basketDto.getName());
        basketParams.setPhone(basketDto.getPhone());
        basketParams.setClickId(basketDto.getClickId());
        basketParams.setOfferId(offer);
        basketParams.setPid(basketDto.getPid());

        if (basketDto.getAdf_source() != null && !basketDto.getAdf_source().isEmpty())
            source = basketDto.getAdf_source();

        basketParams.setAgKey(source);
        basketParams.setProdName(basketDto.getProdName());
        basketParams.setProdId(Integer.parseInt(basketDto.getProdId()));
        if (!affliate.isEmpty())
            basketParams.setAffiliateId(affliate);


        //todo: need to read from DB
        switch (source) {
            case "24scan":
                GetOffer getOffer = new GetOffer();
                getOffer.setOfferId(1);//current offerID of 24scan
                DBResponse<List<GetOfferResp>> dbResponse = freshService.getOffer(sessionId, getOffer);

                GetOfferResp getOfferResp = dbResponse.getResult().get(0);
                basketParams.setOfferId("1");
                basketParams.setTerms(String.valueOf(getOfferResp.getTerms()));
                basketParams.setUnit(getOfferResp.getUnit());
                basketParams.setPrice(String.valueOf(getOfferResp.getPrice().intValue()));
                basketParams.setOrgId(getOfferResp.getOrgId());
                basketParams.setAgKey(getOfferResp.getAgcShortname());
                basketParams.setAgcId(getOfferResp.getAgencyId());
                basketParams.setAttribute2(getOfferResp.getCallinglist());
                // basketParams.setAgc

                break;
        }


        DBResponse result = logceService.insBasketV3(sessionId, basketParams);
        if (result.getErrorCode() == 0) {
            return TMSResponse.buildResponse(true);
        }
        return TMSResponse.buildResponse(false);
    }
}
