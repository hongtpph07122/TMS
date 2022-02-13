package com.tms.api.helper;

import com.tms.dto.*;
import com.tms.service.impl.LCProvinceService;
import com.tms.service.impl.DeliveryOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by dinhanhthai on 10/08/2019.
 */
public class LocationHelper {
    private final Logger logger = LoggerFactory.getLogger(MailHelper.class);

    public GetProvinceMapResp getProvinceMapper(LCProvinceService provinceService, int partnerId, int provinceId, String session_id) {
        int proId = 0;
        GetProvinceMap provinceMap = new GetProvinceMap();
        provinceMap.setPrvId(provinceId);
        provinceMap.setPartnerId(partnerId);
        logger.info("-----------------------------------{}|{}", provinceId, partnerId);
        DBResponse<List<GetProvinceMapResp>> dbResponse = provinceService.getProvinceMap(session_id, provinceMap);
        if (dbResponse.getResult().isEmpty())
            return new GetProvinceMapResp();
        GetProvinceMapResp getProvince = dbResponse.getResult().get(0);

        return getProvince;
    }

    public GetDistrictMapResp getDistrictMapper(LCProvinceService provinceService, int partnerId, int districtId, String session_id) {
        int proId = 0;
        GetDistrictMap districtMap = new GetDistrictMap();
        districtMap.setDistrictId(districtId);
        districtMap.setPartnerId(partnerId);
        DBResponse<List<GetDistrictMapResp>> dbResponse = provinceService.getDistrictMap(session_id, districtMap);
        if (dbResponse.getResult().isEmpty())
            return new GetDistrictMapResp();
        GetDistrictMapResp getDistrictMapResp = dbResponse.getResult().get(0);

        return getDistrictMapResp;
    }

    public GetSubdistrictMapResp getSubDistrictMapper(LCProvinceService provinceService, int partnerId, int subDistId, String session_id) {
        int proId = 0;
        GetSubdistrictMap subdistrictMap = new GetSubdistrictMap();
        subdistrictMap.setSubdistrictId(subDistId);
        subdistrictMap.setPartnerId(partnerId);
        DBResponse<List<GetSubdistrictMapResp>> dbResponse = provinceService.getSubDistrictMap(session_id, subdistrictMap);
        if (dbResponse.getResult().isEmpty())
            return null;
        GetSubdistrictMapResp getDistrictMapResp = dbResponse.getResult().get(0);

        return getDistrictMapResp;
    }

    public GetNeighbordhoodResp getNeighborhoodMapper(DeliveryOrderService provinceService, int partnerId, int neighborId, String session_id) {
        int proId = 0;
        if(partnerId != Const.KERRY_LM_PARTNER_ID)//chi Kerry moi dung cap neighborhood
            return null;
        GetNeighbordhood nghMap = new GetNeighbordhood();
        nghMap.setId(neighborId);//default kerry
//        nghMap.setPartnerId(partnerId);
        DBResponse<List<GetNeighbordhoodResp>> dbResponse = provinceService.getNeighborhood(session_id, nghMap);
        if (dbResponse.getResult().isEmpty())
            return null;
        GetNeighbordhoodResp getDistrictMapResp = dbResponse.getResult().get(0);

        return getDistrictMapResp;
    }
}
