package com.tms.api.dto.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.dto.GetValidation4Resp;
import com.tms.dto.GetValidation5Resp;
import com.tms.dto.GetValidation6Resp;
import com.tms.dto.GetValidation7Resp;
import org.springframework.util.StringUtils;

public class ValidationResponseDTO {

	private Integer orgId;
	private Integer soId;
	private Integer doId;
	private String doCode;
	private Integer leadId;
	private String leadName;
	@JsonIgnore
	private String leadPhone;
	private String productName;
	private String productCrosssell;
	private Double amount;
	private Integer source;
	private String sourceName;
	private String sourceCode;
	private Integer agId;
	private String agName;
	private String comment;
	private String address;
	private Integer ffmId;
	private String ffmName;
	private Integer warehouseId;
	private String warehouseName;
	private Integer lastmileId;
	private String lastmileName;
	private String paymentMethod;
	private Integer leadStatusId;
	private String leadStatus;
	private Integer soStatusId;
	private String orderStatus;
	private Integer doStatusId;
	private String deliveryStatus;
	private String locationStatus;
	private String leadType;
	private String leadTypeName;
	private Date createdate;
	private Date modifyDate;
	private Integer totalCall;
	private Integer cpId;
	private String cpName;
	private Integer callinglistId;
	private String callinglistName;
	private Integer groupId;
	private String groupName;
	private String agentNote;
	private String affiliateId;
	private String subid1;
	private String reason;
	private String qaNote;
	private Integer validateBy;
	private String validateByName;
	private String provinceName;
	private String districtName;
	private String subDistrictName;
	private String neighborhoodName;
	private String zipCode;

	public static List<ValidationResponseDTO> buildMappedDTO(List<GetValidation4Resp> validationParams,
			OrderManagementRequestDTO orderManagementRequestDTO) {
		List<ValidationResponseDTO> validationResponseDTOS = new ArrayList<>();
		for(GetValidation4Resp validationParam: validationParams){
			ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO();
			validationResponseDTO.setOrgId(validationParam.getOrgId());
			validationResponseDTO.setSoId(validationParam.getSoId());
			validationResponseDTO.setDoId(validationParam.getDoId());
			validationResponseDTO.setDoCode(validationParam.getDoCode());
			validationResponseDTO.setLeadId(validationParam.getLeadId());
			validationResponseDTO.setProductName(validationParam.getProductName());
			validationResponseDTO.setProductCrosssell(validationParam.getProductCrosssell());
			validationResponseDTO.setAmount(validationParam.getAmount());
			validationResponseDTO.setSource(validationParam.getSource());
			validationResponseDTO.setSourceName(validationParam.getSourceName());
			validationResponseDTO.setSourceCode(validationParam.getSourceCode());
			validationResponseDTO.setAgId(validationParam.getAgId());
			validationResponseDTO.setAgName(validationParam.getAgName());
			validationResponseDTO.setComment(validationParam.getComment());
			validationResponseDTO.setFfmId(validationParam.getFfmId());
			validationResponseDTO.setFfmName(validationParam.getFfmName());
			validationResponseDTO.setWarehouseId(validationParam.getWarehouseId());
			validationResponseDTO.setWarehouseName(validationParam.getWarehouseName());
			validationResponseDTO.setLastmileId(validationParam.getLastmileId());
			validationResponseDTO.setLastmileName(validationParam.getLastmileName());
			validationResponseDTO.setPaymentMethod(validationParam.getPaymentMethod());
			validationResponseDTO.setLeadStatusId(validationParam.getLeadStatusId());
			validationResponseDTO.setLeadStatus(validationParam.getLeadStatus());
			validationResponseDTO.setSoStatusId(validationParam.getSoStatusId());
			validationResponseDTO.setOrderStatus(validationParam.getOrderStatus());
			validationResponseDTO.setDoStatusId(validationParam.getDoStatusId());
			validationResponseDTO.setDeliveryStatus(validationParam.getDeliveryStatus());
			validationResponseDTO.setLocationStatus(validationParam.getLocationStatus());
			validationResponseDTO.setLeadType(validationParam.getLeadType());
			validationResponseDTO.setLeadTypeName(validationParam.getLeadTypeName());
			validationResponseDTO.setCreatedate(validationParam.getCreatedate());
			validationResponseDTO.setModifyDate(validationParam.getModifyDate());
			validationResponseDTO.setTotalCall(validationParam.getTotalCall());
			validationResponseDTO.setCpId(validationParam.getCpId());
			validationResponseDTO.setCpName(validationParam.getCpName());
			validationResponseDTO.setCallinglistId(validationParam.getCallinglistId());
			validationResponseDTO.setCallinglistName(validationParam.getCallinglistName());
			validationResponseDTO.setGroupId(validationParam.getGroupId());
			validationResponseDTO.setGroupName(validationParam.getGroupName());
			validationResponseDTO.setLeadName(validationParam.getLeadName());

			if(orderManagementRequestDTO.isDirector())
				validationResponseDTO.setLeadPhone(validationParam.getLeadPhone());
			else
				validationResponseDTO.setLeadPhone(null);

			if(orderManagementRequestDTO.isTeamLeader())
				validationResponseDTO.setAddress(null);
			else
				validationResponseDTO.setAddress(validationParam.getAddress());

			validationResponseDTOS.add(validationResponseDTO);

		}
		return validationResponseDTOS;
	}

	public static List<ValidationResponseDTO> buildMappedDTOV5(List<GetValidation5Resp> validationParams,
			OrderManagementRequestDTO orderManagementRequestDTO) {
		List<ValidationResponseDTO> validationResponseDTOS = new ArrayList<>();
		for(GetValidation5Resp validationParam: validationParams){
			ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO();
			validationResponseDTO.setOrgId(validationParam.getOrgId());
			validationResponseDTO.setSoId(validationParam.getSoId());
			validationResponseDTO.setDoId(validationParam.getDoId());
			validationResponseDTO.setDoCode(validationParam.getDoCode());
			validationResponseDTO.setLeadId(validationParam.getLeadId());
			validationResponseDTO.setProductName(validationParam.getProductName());
			validationResponseDTO.setProductCrosssell(validationParam.getProductCrosssell());
			validationResponseDTO.setAmount(validationParam.getAmount());
			validationResponseDTO.setSource(validationParam.getSource());
			validationResponseDTO.setSourceName(validationParam.getSourceName());
			validationResponseDTO.setSourceCode(validationParam.getSourceCode());
			validationResponseDTO.setAgId(validationParam.getAgId());
			validationResponseDTO.setAgName(validationParam.getAgName());
			validationResponseDTO.setComment(validationParam.getComment());
			validationResponseDTO.setFfmId(validationParam.getFfmId());
			validationResponseDTO.setFfmName(validationParam.getFfmName());
			validationResponseDTO.setWarehouseId(validationParam.getWarehouseId());
			validationResponseDTO.setWarehouseName(validationParam.getWarehouseName());
			validationResponseDTO.setLastmileId(validationParam.getLastmileId());
			validationResponseDTO.setLastmileName(validationParam.getLastmileName());
			validationResponseDTO.setPaymentMethod(validationParam.getPaymentMethod());
			validationResponseDTO.setLeadStatusId(validationParam.getLeadStatusId());
			validationResponseDTO.setLeadStatus(validationParam.getLeadStatus());
			validationResponseDTO.setSoStatusId(validationParam.getSoStatusId());
			validationResponseDTO.setOrderStatus(validationParam.getOrderStatus());
			validationResponseDTO.setDoStatusId(validationParam.getDoStatusId());
			validationResponseDTO.setDeliveryStatus(validationParam.getDeliveryStatus());
			validationResponseDTO.setLocationStatus(validationParam.getLocationStatus());
			validationResponseDTO.setLeadType(validationParam.getLeadType());
			validationResponseDTO.setLeadTypeName(validationParam.getLeadTypeName());
			validationResponseDTO.setCreatedate(validationParam.getCreatedate());
			validationResponseDTO.setModifyDate(validationParam.getModifyDate());
			validationResponseDTO.setTotalCall(validationParam.getTotalCall());
			validationResponseDTO.setCpId(validationParam.getCpId());
			validationResponseDTO.setCpName(validationParam.getCpName());
			validationResponseDTO.setCallinglistId(validationParam.getCallinglistId());
			validationResponseDTO.setCallinglistName(validationParam.getCallinglistName());
			validationResponseDTO.setGroupId(validationParam.getGroupId());
			validationResponseDTO.setGroupName(validationParam.getGroupName());
			validationResponseDTO.setLeadName(validationParam.getLeadName());
			validationResponseDTO.setAgentNote(validationParam.getAgentNote());
			validationResponseDTO.setValidateBy(validationParam.getValidateBy());
			validationResponseDTO.setValidateByName(validationParam.getValidateByName());

			if(orderManagementRequestDTO.isDirector())
				validationResponseDTO.setLeadPhone(validationParam.getLeadPhone());
			else
				validationResponseDTO.setLeadPhone(null);

			if(orderManagementRequestDTO.isTeamLeader())
				validationResponseDTO.setAddress(null);
			else
				validationResponseDTO.setAddress(validationParam.getAddress());

			validationResponseDTOS.add(validationResponseDTO);

		}
		return validationResponseDTOS;
	}
	public static List<ValidationResponseDTO> buildMappedDTOV6(List<GetValidation6Resp> validationParams,
															   OrderManagementRequestDTO orderManagementRequestDTO) {
		List<ValidationResponseDTO> validationResponseDTOS = new ArrayList<>();
		for(GetValidation6Resp validationParam: validationParams){
			ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO();
			validationResponseDTO.setOrgId(validationParam.getOrgId());
			validationResponseDTO.setSoId(validationParam.getSoId());
			validationResponseDTO.setDoId(validationParam.getDoId());
			validationResponseDTO.setDoCode(validationParam.getDoCode());
			validationResponseDTO.setLeadId(validationParam.getLeadId());
			validationResponseDTO.setProductName(validationParam.getProductName());
			validationResponseDTO.setProductCrosssell(validationParam.getProductCrosssell());
			validationResponseDTO.setAmount(validationParam.getAmount());
			validationResponseDTO.setSource(validationParam.getSource());
			validationResponseDTO.setSourceName(validationParam.getSourceName());
			validationResponseDTO.setSourceCode(validationParam.getSourceCode());
			validationResponseDTO.setAgId(validationParam.getAgId());
			validationResponseDTO.setAgName(validationParam.getAgName());
			validationResponseDTO.setComment(validationParam.getComment());
			validationResponseDTO.setFfmId(validationParam.getFfmId());
			validationResponseDTO.setFfmName(validationParam.getFfmName());
			validationResponseDTO.setWarehouseId(validationParam.getWarehouseId());
			validationResponseDTO.setWarehouseName(validationParam.getWarehouseName());
			validationResponseDTO.setLastmileId(validationParam.getLastmileId());
			validationResponseDTO.setLastmileName(validationParam.getLastmileName());
			validationResponseDTO.setPaymentMethod(validationParam.getPaymentMethod());
			validationResponseDTO.setLeadStatusId(validationParam.getLeadStatusId());
			validationResponseDTO.setLeadStatus(validationParam.getLeadStatus());
			validationResponseDTO.setSoStatusId(validationParam.getSoStatusId());
			validationResponseDTO.setOrderStatus(validationParam.getOrderStatus());
			validationResponseDTO.setDoStatusId(validationParam.getDoStatusId());
			validationResponseDTO.setDeliveryStatus(validationParam.getDeliveryStatus());
			validationResponseDTO.setLocationStatus(validationParam.getLocationStatus());
			validationResponseDTO.setLeadType(validationParam.getLeadType());
			validationResponseDTO.setLeadTypeName(validationParam.getLeadTypeName());
			validationResponseDTO.setCreatedate(validationParam.getCreatedate());
			validationResponseDTO.setModifyDate(validationParam.getModifyDate());
			validationResponseDTO.setTotalCall(validationParam.getTotalCall());
			validationResponseDTO.setCpId(validationParam.getCpId());
			validationResponseDTO.setCpName(validationParam.getCpName());
			validationResponseDTO.setCallinglistId(validationParam.getCallinglistId());
			validationResponseDTO.setCallinglistName(validationParam.getCallinglistName());
			validationResponseDTO.setGroupId(validationParam.getGroupId());
			validationResponseDTO.setGroupName(validationParam.getGroupName());
			validationResponseDTO.setLeadName(validationParam.getLeadName());
			validationResponseDTO.setAgentNote(validationParam.getAgentNote());
			validationResponseDTO.setAffiliateId(validationParam.getAffiliateId());
			validationResponseDTO.setSubid1(validationParam.getSubid1());
			validationResponseDTO.setValidateBy(validationParam.getValidateBy());
			validationResponseDTO.setValidateByName(validationParam.getValidateByName());

			if(orderManagementRequestDTO.isDirector())
				validationResponseDTO.setLeadPhone(validationParam.getLeadPhone());
			else
				validationResponseDTO.setLeadPhone(null);

			if(orderManagementRequestDTO.isTeamLeader())
				validationResponseDTO.setAddress(null);
			else
				validationResponseDTO.setAddress(validationParam.getAddress());

			validationResponseDTOS.add(validationResponseDTO);

		}
		return validationResponseDTOS;
	}

	public static List<ValidationResponseDTO> buildMappedDTOV7(List<GetValidation7Resp> validationParams,
															   OrderManagementRequestDTO orderManagementRequestDTO) {
		List<ValidationResponseDTO> validationResponseDTOS = new ArrayList<>();
		for(GetValidation7Resp validationParam: validationParams){
			ValidationResponseDTO validationResponseDTO = new ValidationResponseDTO();
			validationResponseDTO.setOrgId(validationParam.getOrgId());
			validationResponseDTO.setSoId(validationParam.getSoId());
			validationResponseDTO.setDoId(validationParam.getDoId());
			validationResponseDTO.setDoCode(validationParam.getDoCode());
			validationResponseDTO.setLeadId(validationParam.getLeadId());
			validationResponseDTO.setProductName(validationParam.getProductName());
			validationResponseDTO.setProductCrosssell(validationParam.getProductCrosssell());
			validationResponseDTO.setAmount(validationParam.getAmount());
			validationResponseDTO.setSource(validationParam.getSource());
			validationResponseDTO.setSourceName(validationParam.getSourceName());
			validationResponseDTO.setSourceCode(validationParam.getSourceCode());
			validationResponseDTO.setAgId(validationParam.getAgId());
			validationResponseDTO.setAgName(validationParam.getAgName());
			validationResponseDTO.setComment(validationParam.getComment());
			validationResponseDTO.setFfmId(validationParam.getFfmId());
			validationResponseDTO.setFfmName(validationParam.getFfmName());
			validationResponseDTO.setWarehouseId(validationParam.getWarehouseId());
			validationResponseDTO.setWarehouseName(validationParam.getWarehouseName());
			validationResponseDTO.setLastmileId(validationParam.getLastmileId());
			validationResponseDTO.setLastmileName(validationParam.getLastmileName());
			validationResponseDTO.setPaymentMethod(validationParam.getPaymentMethod());
			validationResponseDTO.setLeadStatusId(validationParam.getLeadStatusId());
			validationResponseDTO.setLeadStatus(validationParam.getLeadStatus());
			validationResponseDTO.setSoStatusId(validationParam.getSoStatusId());
			validationResponseDTO.setOrderStatus(validationParam.getOrderStatus());
			validationResponseDTO.setDoStatusId(validationParam.getDoStatusId());
			validationResponseDTO.setDeliveryStatus(validationParam.getDeliveryStatus());
			validationResponseDTO.setLocationStatus(validationParam.getLocationStatus());
			validationResponseDTO.setLeadType(validationParam.getLeadType());
			validationResponseDTO.setLeadTypeName(validationParam.getLeadTypeName());
			validationResponseDTO.setCreatedate(validationParam.getCreatedate());
			validationResponseDTO.setModifyDate(validationParam.getModifyDate());
			validationResponseDTO.setTotalCall(validationParam.getTotalCall());
			validationResponseDTO.setCpId(validationParam.getCpId());
			validationResponseDTO.setCpName(validationParam.getCpName());
			validationResponseDTO.setCallinglistId(validationParam.getCallinglistId());
			validationResponseDTO.setCallinglistName(validationParam.getCallinglistName());
			validationResponseDTO.setGroupId(validationParam.getGroupId());
			validationResponseDTO.setGroupName(validationParam.getGroupName());
			validationResponseDTO.setLeadName(validationParam.getLeadName());
			validationResponseDTO.setAgentNote(validationParam.getAgentNote());
			validationResponseDTO.setAffiliateId(validationParam.getAffiliateId());
			validationResponseDTO.setSubid1(validationParam.getSubid1());
			validationResponseDTO.setReason(validationParam.getReason());
			validationResponseDTO.setQaNote(validationParam.getQaNote());
			validationResponseDTO.setValidateBy(validationParam.getValidateBy());
			validationResponseDTO.setValidateByName(validationParam.getValidateByName());
			/* begin::Location */
			validationResponseDTO.setProvinceName(validationParam.getProvinceName());
			validationResponseDTO.setDistrictName(validationParam.getDistrictName());
			validationResponseDTO.setSubDistrictName(validationParam.getSubDistrictName());
			validationResponseDTO.setNeighborhoodName(validationParam.getNeighborhoodName());
			if (!StringUtils.isEmpty(validationParam.getZipCode())) {
				validationResponseDTO.setZipCode(validationParam.getZipCode());
			}
			/* end::Location */

			if(orderManagementRequestDTO.isDirector())
				validationResponseDTO.setLeadPhone(validationParam.getLeadPhone());
			else
				validationResponseDTO.setLeadPhone(null);

			if(orderManagementRequestDTO.isTeamLeader())
				validationResponseDTO.setAddress(null);
			else
				validationResponseDTO.setAddress(validationParam.getAddress());

			validationResponseDTOS.add(validationResponseDTO);

		}
		return validationResponseDTOS;
	}
	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getSoId() {
		return soId;
	}

	public void setSoId(Integer soId) {
		this.soId = soId;
	}

	public Integer getDoId() {
		return doId;
	}

	public void setDoId(Integer doId) {
		this.doId = doId;
	}

	public String getDoCode() {
		return doCode;
	}

	public void setDoCode(String doCode) {
		this.doCode = doCode;
	}

	public Integer getLeadId() {
		return leadId;
	}

	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}

	public String getLeadName() {
		return leadName;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public String getLeadPhone() {
		return leadPhone;
	}

	public void setLeadPhone(String leadPhone) {
		this.leadPhone = leadPhone;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCrosssell() {
		return productCrosssell;
	}

	public void setProductCrosssell(String productCrosssell) {
		this.productCrosssell = productCrosssell;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public Integer getAgId() {
		return agId;
	}

	public void setAgId(Integer agId) {
		this.agId = agId;
	}

	public String getAgName() {
		return agName;
	}

	public void setAgName(String agName) {
		this.agName = agName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getFfmId() {
		return ffmId;
	}

	public void setFfmId(Integer ffmId) {
		this.ffmId = ffmId;
	}

	public String getFfmName() {
		return ffmName;
	}

	public void setFfmName(String ffmName) {
		this.ffmName = ffmName;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Integer getLastmileId() {
		return lastmileId;
	}

	public void setLastmileId(Integer lastmileId) {
		this.lastmileId = lastmileId;
	}

	public String getLastmileName() {
		return lastmileName;
	}

	public void setLastmileName(String lastmileName) {
		this.lastmileName = lastmileName;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Integer getLeadStatusId() {
		return leadStatusId;
	}

	public void setLeadStatusId(Integer leadStatusId) {
		this.leadStatusId = leadStatusId;
	}

	public String getLeadStatus() {
		return leadStatus;
	}

	public void setLeadStatus(String leadStatus) {
		this.leadStatus = leadStatus;
	}

	public Integer getSoStatusId() {
		return soStatusId;
	}

	public void setSoStatusId(Integer soStatusId) {
		this.soStatusId = soStatusId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getDoStatusId() {
		return doStatusId;
	}

	public void setDoStatusId(Integer doStatusId) {
		this.doStatusId = doStatusId;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}

	public String getLeadType() {
		return leadType;
	}

	public void setLeadType(String leadType) {
		this.leadType = leadType;
	}

	public String getLeadTypeName() {
		return leadTypeName;
	}

	public void setLeadTypeName(String leadTypeName) {
		this.leadTypeName = leadTypeName;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getTotalCall() {
		return totalCall;
	}

	public void setTotalCall(Integer totalCall) {
		this.totalCall = totalCall;
	}

	public Integer getCpId() {
		return cpId;
	}

	public void setCpId(Integer cpId) {
		this.cpId = cpId;
	}

	public String getCpName() {
		return cpName;
	}

	public void setCpName(String cpName) {
		this.cpName = cpName;
	}

	public Integer getCallinglistId() {
		return callinglistId;
	}

	public void setCallinglistId(Integer callinglistId) {
		this.callinglistId = callinglistId;
	}

	public String getCallinglistName() {
		return callinglistName;
	}

	public void setCallinglistName(String callinglistName) {
		this.callinglistName = callinglistName;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAgentNote() {
		return agentNote;
	}

	public void setAgentNote(String agentNote) {
		this.agentNote = agentNote;
	}

	public String getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(String affiliateId) {
		this.affiliateId = affiliateId;
	}

	public String getSubid1() {
		return subid1;
	}

	public void setSubid1(String subid1) {
		this.subid1 = subid1;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getQaNote() {
		return qaNote;
	}

	public void setQaNote(String qaNote) {
		this.qaNote = qaNote;
	}

	public Integer getValidateBy() {
		return validateBy;
	}

	public void setValidateBy(Integer validateBy) {
		this.validateBy = validateBy;
	}

	public String getValidateByName() {
		return validateByName;
	}

	public void setValidateByName(String validateByName) {
		this.validateByName = validateByName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getSubDistrictName() {
		return subDistrictName;
	}

	public void setSubDistrictName(String subDistrictName) {
		this.subDistrictName = subDistrictName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getNeighborhoodName() {
		return neighborhoodName;
	}

	public void setNeighborhoodName(String neighborhoodName) {
		this.neighborhoodName = neighborhoodName;
	}
}
