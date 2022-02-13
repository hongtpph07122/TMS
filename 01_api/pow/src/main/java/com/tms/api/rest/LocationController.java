package com.tms.api.rest;

import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.LogHelper;
import com.tms.api.response.TMSResponse;
import com.tms.api.utils.ObjectUtils;
import com.tms.dto.*;
import com.tms.entity.LCDistrict;
import com.tms.entity.LCProvince;
import com.tms.entity.Subdistrict;
import com.tms.model.Response.NeighborhoodResponseDTO;
import com.tms.service.impl.DeliveryOrderService;
import com.tms.service.impl.LCProvinceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LCProvinceService lcProvinceService;
    private final DeliveryOrderService deliveryOrderService;

    @Value("${config.country}")
    public String _COUNTRY;

    @Autowired
    public LocationController(LCProvinceService lcProvinceService, DeliveryOrderService deliveryOrderService) {
        this.lcProvinceService = lcProvinceService;
        this.deliveryOrderService = deliveryOrderService;
    }

    private String getSession() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    /**
     * get all province by partner id
     *
     * @param partnerId if has a value, get all province by partner id, but no value, get only Province
     * @return all province to user
     */
    @GetMapping("/province")
    public TMSResponse<List<GetProvinceBypartnerResp>> getProvincesByPartner(@RequestParam(value = "partnerId", required = false) Integer partnerId) throws TMSException {

        String session = getSession();
        try {
            GetProvinceBypartner provinceBypartnerResp = new GetProvinceBypartner();
            if (partnerId != null) {
                logger.info("get current partner {}", partnerId);
                provinceBypartnerResp.setPartnerId(partnerId);


                DBResponse<List<GetProvinceBypartnerResp>> dbResponse = lcProvinceService.getProvinceByPartner(session, provinceBypartnerResp);
                if (dbResponse.getResult().isEmpty()) {
                    throw new TMSException(ErrorMessage.DISTRICT_NOT_FOUND);
                }
                return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
            }

            GetProvince province = new GetProvince();
            DBResponse<List<LCProvince>> dbResponse = lcProvinceService.getProvince(session, province);
            if (dbResponse.getResult().isEmpty()) {
                throw new TMSException(ErrorMessage.DISTRICT_NOT_FOUND);
            }
            return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());

        } catch (Exception e) {
            throw new TMSException(ErrorMessage.BAD_REQUEST);
        }
    }


    @GetMapping("/district")
    public TMSResponse<List<LCDistrict>> getDistrict(@RequestParam(value = "provinceId", required = false) Integer provinceId) throws TMSException {
        try {
            GetDistrict getDistrict = new GetDistrict();
            if (provinceId != null) {
                getDistrict.setPrvId(provinceId);
            } else
                getDistrict.setPrvId(1);//default value

            DBResponse<List<LCDistrict>> lstDistrict = lcProvinceService.getDistrict(getSession(), getDistrict);

            if (lstDistrict.getResult().isEmpty()) {
                throw new TMSException(ErrorMessage.DISTRICT_NOT_FOUND);
            }

            return TMSResponse.buildResponse(lstDistrict.getResult());
        } catch (Exception e) {
            throw new TMSException(ErrorMessage.BAD_REQUEST);
        }
    }

    @GetMapping("/subdistrict")
    public TMSResponse<List<Subdistrict>> getSubDistrict(@RequestParam(value = "districtId", required = false) Integer districtId) throws TMSException {
        try {
            GetSubdistrict subdistrict = new GetSubdistrict();
            if (districtId != null) {
                subdistrict.setDtId(districtId);
            } else
                subdistrict.setDtId(1);//default value

            DBResponse<List<Subdistrict>> lstDistrict = lcProvinceService.getSubDistrict(getSession(), subdistrict);

            if (lstDistrict.getResult().isEmpty()) {
                throw new TMSException(ErrorMessage.DISTRICT_NOT_FOUND);
            }

            return TMSResponse.buildResponse(lstDistrict.getResult());
        } catch (Exception e) {
            throw new TMSException(ErrorMessage.BAD_REQUEST);
        }
    }

    /*
    @GetMapping("/neighborhood")
    public TMSResponse<List<GetNeighbordhoodResp>> getNeighborhood(@RequestParam(value = "sdtId", required = false) Integer sdtId) throws TMSException {
        try {
            GetNeighbordhood subdistrict = new GetNeighbordhood();
            if (sdtId != null) {
                subdistrict.setSdtId(sdtId);
            } else
                subdistrict.setSdtId(1);//default value

            DBResponse<List<GetNeighbordhoodResp>> lstDistrict = deliveryOrderService.getNeighborhood(getSession(), subdistrict);

            if (lstDistrict.getResult().isEmpty()) {
                throw new TMSException(ErrorMessage.DISTRICT_NOT_FOUND);
            }

            return TMSResponse.buildResponse(lstDistrict.getResult());
        } catch (Exception e) {
            throw new TMSException(ErrorMessage.BAD_REQUEST);
        }
    }
    */

    @GetMapping("/neighborhood")
    public TMSResponse<?> findAllNeighborhood(@RequestParam(value = "sdtId", required = false) Integer sdtId) {
        GetNeighbordhood subdistrict = new GetNeighbordhood();
        if (sdtId != null) {
            subdistrict.setSdtId(sdtId);
        } else {
            subdistrict.setSdtId(1);
        }
        DBResponse<List<NeighborhoodResponseDTO>> listDBResponseID = deliveryOrderService.getNeighborhoodV1(getSession(), subdistrict);
        DBResponse<List<GetNeighbordhoodResp>> listDBResponseOriginal = deliveryOrderService.getNeighborhood(getSession(), subdistrict);
        if (StringUtils.trimAllWhitespace(_COUNTRY).toLowerCase().equals("id")) {
            if (CollectionUtils.isEmpty(listDBResponseID.getResult())) {
                try {
                    throw new TMSException(ErrorMessage.DISTRICT_NOT_FOUND);
                } catch (TMSException e) {
                    logger.error("error - [findAllNeighborhood] response neighborhood ID from db is null: {}", e.getMessage(), e);
                }
            }
            return TMSResponse.buildResponse(listDBResponseID.getResult());
        } else {
            if (CollectionUtils.isEmpty(listDBResponseOriginal.getResult())) {
                try {
                    throw new TMSException(ErrorMessage.DISTRICT_NOT_FOUND);
                } catch (TMSException e) {
                    logger.error("error - [findAllNeighborhood] response neighborhood from db is null: {}", e.getMessage(), e);
                }
            }
            return TMSResponse.buildResponse(listDBResponseOriginal.getResult());
        }
    }

    @GetMapping("/zipcode")
    public TMSResponse<List<GetPostaCodeV2Resp>> getZipcode(@RequestParam(value = "nbhId", required = false) Integer nbhId) throws TMSException {
        try {
            GetLocation location = new GetLocation();
          /*  if(!Helper.isIndonesia(_COUNTRY))//ko phai INDO truyen mac dinh
                location.setCountryId(1);*/
            if (nbhId != null) {
                location.setNeighborhoodId(nbhId);
            } else
                location.setNeighborhoodId(1);//default value

            DBResponse<List<GetLocationResp>> lstDistrict = deliveryOrderService.getLocationFull(getSession(), location);

            if (lstDistrict.getResult().isEmpty()) {
                throw new TMSException(ErrorMessage.DISTRICT_NOT_FOUND);
            }

            return TMSResponse.buildResponse(lstDistrict.getResult());
        } catch (Exception e) {
            throw new TMSException(ErrorMessage.BAD_REQUEST);
        }
    }

    @GetMapping("/zipcode-location")
    public TMSResponse<List<GetLocationResp>> getZipcodeToLocation(@RequestParam(value = "zipcode", required = false) String zipcode) throws TMSException {
        try {
            GetLocation location = new GetLocation();
           /* if(!Helper.isIndonesia(_COUNTRY))//ko phai INDO truyen mac dinh
                location.setCountryId(1);*/
            if (zipcode != null) {
                location.setPostalCode(zipcode);
            } else
                location.setPostalCode("10000");//default value

            DBResponse<List<GetLocationResp>> lstDistrict = deliveryOrderService.getLocationFull(getSession(), location);

            if (lstDistrict.getResult().isEmpty()) {
                throw new TMSException(ErrorMessage.DISTRICT_NOT_FOUND);
            }

            return TMSResponse.buildResponse(lstDistrict.getResult());
        } catch (Exception e) {
            throw new TMSException(ErrorMessage.BAD_REQUEST);
        }
    }
}
