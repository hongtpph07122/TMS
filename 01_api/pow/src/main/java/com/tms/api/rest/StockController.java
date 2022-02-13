package com.tms.api.rest;

import com.tms.api.dto.GetPartnerDto;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.response.TMSResponse;
import com.tms.dto.*;
import com.tms.entity.log.InsStock;
import com.tms.entity.log.UpdStock;
import com.tms.service.impl.CLProductService;
import com.tms.service.impl.DeliveryOrderService;
import com.tms.service.impl.LogService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController extends BaseController {

    Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    CLProductService clProductService;

    @Autowired
    LogService logService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DeliveryOrderService deliveryOrderService;

    @GetMapping()
    public TMSResponse<List<GetPartnerResp>> getStocks(@ModelAttribute("stock") GetPartner partner) throws TMSException {
        DBResponse<List<GetPartnerResp>> lstPartner = null;
        try {
            GetPartner _partner = partner;
            _partner.setOrgId(getCurrentOriganationId());
            if (partner.getLimit() == null)
                _partner.setLimit(Const.DEFAULT_PAGE_SIZE);
            lstPartner = clProductService.getPartner(SESSION_ID, _partner);

            return TMSResponse.buildResponse(lstPartner.getResult(), lstPartner.getRowCount());
        } catch (Exception e) {
            logger.error(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
//		return null;
    }

    @GetMapping("/orderStock")
    public TMSResponse<List<GetPartnerDto>> getStocks() throws TMSException {
        DBResponse<List<GetPartnerResp>> lstPartner = null;
        try {
            GetPartner _partner = new GetPartner();
            _partner.setOrgId(getCurrentOriganationId());
//            _partner.setLimit(Const.DEFAULT_PAGE_SIZE);
            lstPartner = clProductService.getPartner(SESSION_ID, _partner);

            if (lstPartner.getResult().isEmpty())
                throw new TMSException(ErrorMessage.NOT_FOUND);

            GetPartnerDto lst = new GetPartnerDto();
            lst.setLst(lstPartner.getResult());
            lst.setIsSelected(Const._IS_SELECTED_PARTNER_IN_ORDER);

            return TMSResponse.buildResponse(lst, lstPartner.getResult().size());
//			return TMSResponse.buildResponse(lstPartner.getResult(), lstPartner.getRowCount());
        } catch (Exception e) {
            logger.error(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
//		return null;
    }

    @GetMapping("/listDO")
    public TMSResponse<List<GetDefaultDOResp>> getDefaultDO(@ModelAttribute("DO") GetDefaultDO partner) throws TMSException {
//		DBResponse<List<GetPartnerResp>> lstPartner = null;
        try {
            GetDefaultDO _partner = partner;
            _partner.setOrgId(getCurrentOriganationId());
            if (partner.getLimit() == null)
                _partner.setLimit(Const.DEFAULT_PAGE_SIZE);
            //  lstPartner = clProductService.getPartner(SESSION_ID, _partner);

            DBResponse<List<GetDefaultDOResp>> lstPartner = deliveryOrderService.getDefaultDO(SESSION_ID, _partner);
            return TMSResponse.buildResponse(lstPartner.getResult(), lstPartner.getRowCount());
        } catch (Exception e) {
            logger.error(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
//		return null;
    }


    @GetMapping("/StockByProduct/{prodId}")
    public TMSResponse getStockByProduct(@PathVariable Integer prodId) {
        // TODO get StockByProduct
        return null;
    }

    @GetMapping("/{stockId}")
    public TMSResponse getStock(@PathVariable Integer stockId) {
        // TODO get stock detail
        return null;
    }

    @PostMapping()
    public TMSResponse create(@RequestBody InsStock stock) throws TMSException {
        // TODO update stock detail
        DBResponse response = logService.insStock(SESSION_ID, stock);
        if (response.getErrorCode() != 1) {
            throw new TMSException(response.getErrorMsg());
        }
        return TMSResponse.buildResponse(response.getResult());
        //return null;
    }

    @PutMapping()
    public TMSResponse update(@RequestBody UpdStock stock) throws TMSException {
        //public Object update() {
        // TODO update stock detail
        //stock.setOrgId(getCurrentUser().getOrgId());
        DBResponse response = logService.updStock(SESSION_ID, stock);
        if (response.getErrorCode() != 1) {
            throw new TMSException(response.getErrorMsg());
        }
        return TMSResponse.buildResponse(response.getResult());
        //return null;
    }

    @GetMapping("/warehouse")
    public TMSResponse getWarehouse() {
        // TODO get stock detail
        return null;
    }

    @GetMapping("/warehouse/{warehouseId}")
    public TMSResponse getWarehouse(@PathVariable Integer warehouseId) {
        // TODO get stock detail
        return null;
    }
}
