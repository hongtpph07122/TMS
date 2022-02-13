package com.tms.api.service;

import com.tms.api.dto.*;
import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.api.entity.OdSaleOrder;
import com.tms.api.exception.TMSException;
import com.tms.api.request.SaleOrderRequest;
import com.tms.api.response.TMSResponse;
import com.tms.dto.GetCollectionData;
import com.tms.dto.GetCommissionData;
import com.tms.dto.*;
import com.tms.entity.SaleOrder;
import com.tms.entity.CLFresh;
import com.tms.entity.log.InsDoNew;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface SOService {

	OrderManagement8RespDto getOrderManagerment(GetOrderManagement8 params, boolean isExport);

	OrderManagement10RespDto getOrderManagement(GetOrderManagement10 params, boolean isExport, String ssId);

	byte[] exportCSVOrderManagement(GetOrderManagement10 orderManagement,
									OrderManagementRequestDTO orderManagementRequestDTO) throws TMSException;

	byte[] exportCSVValidation(GetValidation7 validations, OrderManagementRequestDTO orderManagementRequestDTO);

	byte[] exportCommissionData(GetCommissionData commissionData, String sessionId);

	byte[] exportCollectionData(GetCollectionData collectionData, String sessionId);

	ValidationRespDto getValidation(GetValidation7 params, boolean isExport);

	String getDefaultCreateDateInCommisionDataOrCollectionData();

	boolean createDOCancel(InsDoNew insDoNew, Integer leadId, int _curOrgId, int userId) throws TMSException;

	TMSResponse mappingFFMLastsmile();
	
	boolean CreateDONewWhenChangeWarehouse(String SESSION_ID,Integer orgId,Integer userId,InsDoNew insDoNew, Integer leadId, int _curOrgId, int ffmPartnerIdNew,
			int lmPartnerIdNew, int warehouseIdNew) throws TMSException;

	int deleteCallbackByLeadId(Integer leadId);

	String deleteCallbackByLeadIds(List<Integer> leadIds);

	OdSaleOrder updateSaleOrder(OdSaleOrder saleOrder);

	List<GetLogSoResp> getLogSO (Integer soId, Integer limit, Integer offset);

	boolean isNotSaveUncall(CLFresh clFreshNew);
}
