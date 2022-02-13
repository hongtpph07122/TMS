package com.tms.api.dto.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.dto.GetOrderManagement10Resp;
import com.tms.dto.GetOrderManagement7Resp;
import com.tms.dto.GetOrderManagement8Resp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderManagementResponseDTO {

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
    private String affiliateId;
    private String reason;
    private String clickId;
    private String leadPostbackStatus;
    private String reasonCallback;
    private String agentNote;
    private Integer actualCall;
    private String subid5;
    private String subId1;
    private String custAge;
    private String custGender;
    private LocalDate custDob;
    private String custJob;
    private String custOtherSymptom;
    private String custArea;
    private String provinceName;
    private String districtName;
    private String subDistrictName;


    public static List<OrderManagementResponseDTO> buildMappedToDTOV7(
            List<GetOrderManagement7Resp> orderManagement7Resp, OrderManagementRequestDTO orderManagementRequestDTO) {
        List<OrderManagementResponseDTO> orderManagementResponseDTOList = new ArrayList<>();
        for (GetOrderManagement7Resp orderManagement6 : orderManagement7Resp) {
            OrderManagementResponseDTO orderManagementResponseDTO = new OrderManagementResponseDTO();
            orderManagementResponseDTO.setOrgId(orderManagement6.getOrgId());
            orderManagementResponseDTO.setSoId(orderManagement6.getSoId());
            orderManagementResponseDTO.setDoId(orderManagement6.getDoId());
            orderManagementResponseDTO.setDoCode(orderManagement6.getDoCode());
            orderManagementResponseDTO.setLeadId(orderManagement6.getLeadId());
            orderManagementResponseDTO.setProductName(orderManagement6.getProductName());
            orderManagementResponseDTO.setProductCrosssell(orderManagement6.getProductCrosssell());
            orderManagementResponseDTO.setAmount(orderManagement6.getAmount());
            orderManagementResponseDTO.setSource(orderManagement6.getSource());
            orderManagementResponseDTO.setSourceName(orderManagement6.getSourceName());
            orderManagementResponseDTO.setSourceCode(orderManagement6.getSourceCode());
            orderManagementResponseDTO.setAgId(orderManagement6.getAgId());
            orderManagementResponseDTO.setAgName(orderManagement6.getAgName());
            orderManagementResponseDTO.setComment(orderManagement6.getComment());
            orderManagementResponseDTO.setFfmId(orderManagement6.getFfmId());
            orderManagementResponseDTO.setFfmName(orderManagement6.getFfmName());
            orderManagementResponseDTO.setWarehouseId(orderManagement6.getWarehouseId());
            orderManagementResponseDTO.setWarehouseName(orderManagement6.getWarehouseName());
            orderManagementResponseDTO.setLastmileId(orderManagement6.getLastmileId());
            orderManagementResponseDTO.setLastmileName(orderManagement6.getLastmileName());
            orderManagementResponseDTO.setPaymentMethod(orderManagement6.getPaymentMethod());
            orderManagementResponseDTO.setLeadStatusId(orderManagement6.getLeadStatusId());
            orderManagementResponseDTO.setLeadStatus(orderManagement6.getLeadStatus());
            orderManagementResponseDTO.setSoStatusId(orderManagement6.getSoStatusId());
            orderManagementResponseDTO.setOrderStatus(orderManagement6.getOrderStatus());
            orderManagementResponseDTO.setDoStatusId(orderManagement6.getDoStatusId());
            orderManagementResponseDTO.setDeliveryStatus(orderManagement6.getDeliveryStatus());
            orderManagementResponseDTO.setLeadType(orderManagement6.getLeadType());
            orderManagementResponseDTO.setLeadTypeName(orderManagement6.getLeadTypeName());
            orderManagementResponseDTO.setCreatedate(orderManagement6.getCreatedate());
            orderManagementResponseDTO.setModifyDate(orderManagement6.getModifyDate());
            orderManagementResponseDTO.setTotalCall(orderManagement6.getTotalCall());
            orderManagementResponseDTO.setCpId(orderManagement6.getCpId());
            orderManagementResponseDTO.setCpName(orderManagement6.getCpName());
            orderManagementResponseDTO.setCallinglistId(orderManagement6.getCallinglistId());
            orderManagementResponseDTO.setCallinglistName(orderManagement6.getCallinglistName());
            orderManagementResponseDTO.setGroupId(orderManagement6.getGroupId());
            orderManagementResponseDTO.setGroupName(orderManagement6.getGroupName());
            orderManagementResponseDTO.setAffiliateId(orderManagement6.getAffiliateId());
            orderManagementResponseDTO.setReason(orderManagement6.getReason());
            orderManagementResponseDTO.setClickId(orderManagement6.getClickId());
            orderManagementResponseDTO.setLeadName(orderManagement6.getLeadName());
            orderManagementResponseDTO.setLeadPostbackStatus(orderManagement6.getLeadPostbackStatus());
            orderManagementResponseDTO.setReasonCallback(orderManagement6.getReasonCallback());
            orderManagementResponseDTO.setAgentNote(orderManagement6.getAgentNote());
            orderManagementResponseDTO.setActualCall(orderManagement6.getActualCall());
            orderManagementResponseDTO.setSubid5(orderManagement6.getSubid5());
            if (orderManagementRequestDTO.isDirector())
                orderManagementResponseDTO.setLeadPhone(orderManagement6.getLeadPhone());
            else
                orderManagementResponseDTO.setLeadPhone(null);

            if (orderManagementRequestDTO.isTeamLeader())
                orderManagementResponseDTO.setAddress(null);
            else
                orderManagementResponseDTO.setAddress(orderManagement6.getAddress());
            orderManagementResponseDTOList.add(orderManagementResponseDTO);
        }

        return orderManagementResponseDTOList;
    }

    public static List<OrderManagementResponseDTO> buildMappedToDTOV8(
            List<GetOrderManagement8Resp> orderManagement8Resp, OrderManagementRequestDTO orderManagementRequestDTO) {
        List<OrderManagementResponseDTO> orderManagementResponseDTOList = new ArrayList<>();
        for (GetOrderManagement8Resp orderManagement8 : orderManagement8Resp) {
            OrderManagementResponseDTO orderManagementResponseDTO = new OrderManagementResponseDTO();
            orderManagementResponseDTO.setOrgId(orderManagement8.getOrgId());
            orderManagementResponseDTO.setSoId(orderManagement8.getSoId());
            orderManagementResponseDTO.setDoId(orderManagement8.getDoId());
            orderManagementResponseDTO.setDoCode(orderManagement8.getDoCode());
            orderManagementResponseDTO.setLeadId(orderManagement8.getLeadId());
            orderManagementResponseDTO.setProductName(orderManagement8.getProductName());
            orderManagementResponseDTO.setProductCrosssell(orderManagement8.getProductCrosssell());
            orderManagementResponseDTO.setAmount(orderManagement8.getAmount());
            orderManagementResponseDTO.setSource(orderManagement8.getSource());
            orderManagementResponseDTO.setSourceName(orderManagement8.getSourceName());
            orderManagementResponseDTO.setSourceCode(orderManagement8.getSourceCode());
            orderManagementResponseDTO.setAgId(orderManagement8.getAgId());
            orderManagementResponseDTO.setAgName(orderManagement8.getAgName());
            orderManagementResponseDTO.setComment(orderManagement8.getComment());
            orderManagementResponseDTO.setFfmId(orderManagement8.getFfmId());
            orderManagementResponseDTO.setFfmName(orderManagement8.getFfmName());
            orderManagementResponseDTO.setWarehouseId(orderManagement8.getWarehouseId());
            orderManagementResponseDTO.setWarehouseName(orderManagement8.getWarehouseName());
            orderManagementResponseDTO.setLastmileId(orderManagement8.getLastmileId());
            orderManagementResponseDTO.setLastmileName(orderManagement8.getLastmileName());
            orderManagementResponseDTO.setPaymentMethod(orderManagement8.getPaymentMethod());
            orderManagementResponseDTO.setLeadStatusId(orderManagement8.getLeadStatusId());
            orderManagementResponseDTO.setLeadStatus(orderManagement8.getLeadStatus());
            orderManagementResponseDTO.setSoStatusId(orderManagement8.getSoStatusId());
            orderManagementResponseDTO.setOrderStatus(orderManagement8.getOrderStatus());
            orderManagementResponseDTO.setDoStatusId(orderManagement8.getDoStatusId());
            orderManagementResponseDTO.setDeliveryStatus(orderManagement8.getDeliveryStatus());
            orderManagementResponseDTO.setLeadType(orderManagement8.getLeadType());
            orderManagementResponseDTO.setLeadTypeName(orderManagement8.getLeadTypeName());
            orderManagementResponseDTO.setCreatedate(orderManagement8.getCreatedate());
            orderManagementResponseDTO.setModifyDate(orderManagement8.getModifyDate());
            orderManagementResponseDTO.setTotalCall(orderManagement8.getTotalCall());
            orderManagementResponseDTO.setCpId(orderManagement8.getCpId());
            orderManagementResponseDTO.setCpName(orderManagement8.getCpName());
            orderManagementResponseDTO.setCallinglistId(orderManagement8.getCallinglistId());
            orderManagementResponseDTO.setCallinglistName(orderManagement8.getCallinglistName());
            orderManagementResponseDTO.setGroupId(orderManagement8.getGroupId());
            orderManagementResponseDTO.setGroupName(orderManagement8.getGroupName());
            orderManagementResponseDTO.setAffiliateId(orderManagement8.getAffiliateId());
            orderManagementResponseDTO.setReason(orderManagement8.getReason());
            orderManagementResponseDTO.setClickId(orderManagement8.getClickId());
            orderManagementResponseDTO.setLeadName(orderManagement8.getLeadName());
            orderManagementResponseDTO.setLeadPostbackStatus(orderManagement8.getLeadPostbackStatus());
            orderManagementResponseDTO.setReasonCallback(orderManagement8.getReasonCallback());
            orderManagementResponseDTO.setAgentNote(orderManagement8.getAgentNote());
            orderManagementResponseDTO.setActualCall(orderManagement8.getActualCall());
            orderManagementResponseDTO.setSubid5(orderManagement8.getSubid5());
            orderManagementResponseDTO.setSubId1(orderManagement8.getSubId1());
            if (orderManagementRequestDTO.isDirector())
                orderManagementResponseDTO.setLeadPhone(orderManagement8.getLeadPhone());
            else
                orderManagementResponseDTO.setLeadPhone(null);

            if (orderManagementRequestDTO.isTeamLeader())
                orderManagementResponseDTO.setAddress(null);
            else
                orderManagementResponseDTO.setAddress(orderManagement8.getAddress());
            orderManagementResponseDTOList.add(orderManagementResponseDTO);
        }

        return orderManagementResponseDTOList;
    }

    public static List<OrderManagementResponseDTO> buildMappedToDTO(
            List<GetOrderManagement10Resp> orderManagementResp, OrderManagementRequestDTO orderManagementRequestDTO) {
        List<OrderManagementResponseDTO> orderManagementResponseDTOList = new ArrayList<>();
        for (GetOrderManagement10Resp orderManagement : orderManagementResp) {
            OrderManagementResponseDTO orderManagementResponseDTO = new OrderManagementResponseDTO();
            orderManagementResponseDTO.setOrgId(orderManagement.getOrgId());
            orderManagementResponseDTO.setSoId(orderManagement.getSoId());
            orderManagementResponseDTO.setDoId(orderManagement.getDoId());
            orderManagementResponseDTO.setDoCode(orderManagement.getDoCode());
            orderManagementResponseDTO.setLeadId(orderManagement.getLeadId());
            orderManagementResponseDTO.setProductName(orderManagement.getProductName());
            orderManagementResponseDTO.setProductCrosssell(orderManagement.getProductCrosssell());
            orderManagementResponseDTO.setAmount(orderManagement.getAmount());
            orderManagementResponseDTO.setSource(orderManagement.getSource());
            orderManagementResponseDTO.setSourceName(orderManagement.getSourceName());
            orderManagementResponseDTO.setSourceCode(orderManagement.getSourceCode());
            orderManagementResponseDTO.setAgId(orderManagement.getAgId());
            orderManagementResponseDTO.setAgName(orderManagement.getAgName());
            orderManagementResponseDTO.setComment(orderManagement.getComment());
            orderManagementResponseDTO.setFfmId(orderManagement.getFfmId());
            orderManagementResponseDTO.setFfmName(orderManagement.getFfmName());
            orderManagementResponseDTO.setWarehouseId(orderManagement.getWarehouseId());
            orderManagementResponseDTO.setWarehouseName(orderManagement.getWarehouseName());
            orderManagementResponseDTO.setLastmileId(orderManagement.getLastmileId());
            orderManagementResponseDTO.setLastmileName(orderManagement.getLastmileName());
            orderManagementResponseDTO.setPaymentMethod(orderManagement.getPaymentMethod());
            orderManagementResponseDTO.setLeadStatusId(orderManagement.getLeadStatusId());
            orderManagementResponseDTO.setLeadStatus(orderManagement.getLeadStatus());
            orderManagementResponseDTO.setSoStatusId(orderManagement.getSoStatusId());
            orderManagementResponseDTO.setOrderStatus(orderManagement.getOrderStatus());
            orderManagementResponseDTO.setDoStatusId(orderManagement.getDoStatusId());
            orderManagementResponseDTO.setDeliveryStatus(orderManagement.getDeliveryStatus());
            orderManagementResponseDTO.setLeadType(orderManagement.getLeadType());
            orderManagementResponseDTO.setLeadTypeName(orderManagement.getLeadTypeName());
            orderManagementResponseDTO.setCreatedate(orderManagement.getCreatedate());
            orderManagementResponseDTO.setModifyDate(orderManagement.getModifyDate());
            orderManagementResponseDTO.setTotalCall(orderManagement.getTotalCall());
            orderManagementResponseDTO.setCpId(orderManagement.getCpId());
            orderManagementResponseDTO.setCpName(orderManagement.getCpName());
            orderManagementResponseDTO.setCallinglistId(orderManagement.getCallinglistId());
            orderManagementResponseDTO.setCallinglistName(orderManagement.getCallinglistName());
            orderManagementResponseDTO.setGroupId(orderManagement.getGroupId());
            orderManagementResponseDTO.setGroupName(orderManagement.getGroupName());
            orderManagementResponseDTO.setAffiliateId(orderManagement.getAffiliateId());
            orderManagementResponseDTO.setReason(orderManagement.getReason());
            orderManagementResponseDTO.setClickId(orderManagement.getClickId());
            orderManagementResponseDTO.setLeadName(orderManagement.getLeadName());
            orderManagementResponseDTO.setLeadPostbackStatus(orderManagement.getLeadPostbackStatus());
            orderManagementResponseDTO.setReasonCallback(orderManagement.getReasonCallback());
            orderManagementResponseDTO.setAgentNote(orderManagement.getAgentNote());
            orderManagementResponseDTO.setActualCall(orderManagement.getActualCall());
            orderManagementResponseDTO.setSubid5(orderManagement.getSubid5());
            orderManagementResponseDTO.setSubId1(orderManagement.getSubId1());
            orderManagementResponseDTO.setCustAge(orderManagement.getCustAge());
            orderManagementResponseDTO.setCustJob(orderManagement.getCustJob());
            orderManagementResponseDTO.setCustOtherSymptom(orderManagement.getCustOtherSymptom());
            orderManagementResponseDTO.setCustArea(orderManagement.getCustArea());
            orderManagementResponseDTO.setCustGender(orderManagement.getCustGender());
            orderManagementResponseDTO.setCustDob(orderManagement.getCustDob());
            orderManagementResponseDTO.setProvinceName(orderManagement.getProvinceName());
            orderManagementResponseDTO.setDistrictName(orderManagement.getDistrictName());
            orderManagementResponseDTO.setSubDistrictName(orderManagement.getSubDistrictName());

            if (orderManagementRequestDTO.isDirector()) {
                orderManagementResponseDTO.setLeadPhone(orderManagement.getLeadPhone());
            } else {
                orderManagementResponseDTO.setLeadPhone(null);
            }

            if (orderManagementRequestDTO.isTeamLeader()) {
                orderManagementResponseDTO.setAddress(null);
            } else {
                orderManagementResponseDTO.setAddress(orderManagement.getAddress());
            }

            orderManagementResponseDTOList.add(orderManagementResponseDTO);
        }

        return orderManagementResponseDTOList;
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

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClickId() {
        return clickId;
    }

    public void setClickId(String clickId) {
        this.clickId = clickId;
    }

    public String getLeadPostbackStatus() {
        return leadPostbackStatus;
    }

    public void setLeadPostbackStatus(String leadPostbackStatus) {
        this.leadPostbackStatus = leadPostbackStatus;
    }

    public String getReasonCallback() {
        return reasonCallback;
    }

    public void setReasonCallback(String reasonCallback) {
        this.reasonCallback = reasonCallback;
    }

    public String getAgentNote() {
        return agentNote;
    }

    public void setAgentNote(String agentNote) {
        this.agentNote = agentNote;
    }

    public Integer getActualCall() {
        return actualCall;
    }

    public void setActualCall(Integer actualCall) {
        this.actualCall = actualCall;
    }

    public String getSubid5() {
        return subid5;
    }

    public void setSubid5(String subid5) {
        this.subid5 = subid5;
    }

    public String getSubId1() {
        return subId1;
    }

    public void setSubId1(String subId1) {
        this.subId1 = subId1;
    }

    public String getCustAge() {
        return custAge;
    }

    public void setCustAge(String custAge) {
        this.custAge = custAge;
    }

    public String getCustGender() {
        return custGender;
    }

    public void setCustGender(String custGender) {
        this.custGender = custGender;
    }

    public LocalDate getCustDob() {
        return custDob;
    }

    public void setCustDob(LocalDate custDob) {
        this.custDob = custDob;
    }

    public String getCustJob() {
        return custJob;
    }

    public void setCustJob(String custJob) {
        this.custJob = custJob;
    }

    public String getCustOtherSymptom() {
        return custOtherSymptom;
    }

    public void setCustOtherSymptom(String custOtherSymptom) {
        this.custOtherSymptom = custOtherSymptom;
    }

    public String getCustArea() {
        return custArea;
    }

    public void setCustArea(String custArea) {
        this.custArea = custArea;
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

}
