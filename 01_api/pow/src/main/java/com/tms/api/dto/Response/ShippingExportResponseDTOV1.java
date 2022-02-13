package com.tms.api.dto.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.dto.GetShippingExportResp;
import com.tms.dto.GetShippingExportRespV1;

public class ShippingExportResponseDTOV1 {

    private Integer orgId;
    private String saleOrder;
    private String deliveryCode;
    private String customerName;
    @JsonIgnore
    private String customerPhone;
    private String source;
    private String userName;
    private String address;
    private String postalCode;
    private String neighborhoodId;
    private String neighborhood;
    private String wardsId;
    private String wards;
    private String district;
    private String province;
    private String carrier;
    private String comment;
    private String status;
    private String soStatus;
    private String statusTransport;
    private Date createdate;
    private Date updatedate;
    private Integer totalCall;
    private String paymentMethod;
    private Double amount;
    private String product1;
    private Double qty1;
    private Double price1;
    private String product2;
    private Double qty2;
    private Double price2;
    private String product3;
    private Double qty3;
    private Double price3;
    private String product4;
    private Double qty4;
    private Double price4;
    private Integer leadId;
    private String ffmCode;
    private String trackingCode;
    private String ffmName;
    private String warehouse;
    private String agentNote;
    private String affiliateId;
    private String lastmileReturnCode;
    private Date pickedUpDate;
    private Integer attemp;
    private String paymentStatus;
    private Date paidCodDate;
    private Date firstDeliveryTime;
    private Date secondDeliveryTime;
    private Date thirdDeliveryTime;
    private Date returnTime;
    private Date closeTime;
    private Date pickedTime;
    private Date packedTime;
    private Date handoveredTime;
    private String cpName;
    private String category;

    public static List<ShippingExportResponseDTOV1> buildMappedToDTO(
            List<GetShippingExportRespV1> getShippingExportRespList,
            OrderManagementRequestDTO orderManagementRequestDTO) {
        List<ShippingExportResponseDTOV1> shippingExportResponseDTOS = new ArrayList<>();
        for(GetShippingExportRespV1 exportResp: getShippingExportRespList){
            ShippingExportResponseDTOV1 shippingExportResponseDTO = new ShippingExportResponseDTOV1();
            shippingExportResponseDTO.setOrgId(exportResp.getOrgId());
            shippingExportResponseDTO.setSaleOrder(exportResp.getSaleOrder());
            shippingExportResponseDTO.setDeliveryCode(exportResp.getDeliveryCode());
            shippingExportResponseDTO.setSource(exportResp.getSource());
            shippingExportResponseDTO.setUserName(exportResp.getUserName());
            shippingExportResponseDTO.setPostalCode(exportResp.getPostalCode());
            shippingExportResponseDTO.setNeighborhood(exportResp.getNeighborhood());
            shippingExportResponseDTO.setWards(exportResp.getWards());
            shippingExportResponseDTO.setDistrict(exportResp.getDistrict());
            shippingExportResponseDTO.setProvince(exportResp.getProvince());
            shippingExportResponseDTO.setCarrier(exportResp.getCarrier());
            shippingExportResponseDTO.setComment(exportResp.getComment());
            shippingExportResponseDTO.setStatus(exportResp.getStatus());
            shippingExportResponseDTO.setSoStatus(exportResp.getSoStatus());
            shippingExportResponseDTO.setStatusTransport(exportResp.getStatusTransport());
            shippingExportResponseDTO.setCreatedate(exportResp.getCreatedate());
            shippingExportResponseDTO.setUpdatedate(exportResp.getUpdatedate());
            shippingExportResponseDTO.setTotalCall(exportResp.getTotalCall());
            shippingExportResponseDTO.setPaymentMethod(exportResp.getPaymentMethod());
            shippingExportResponseDTO.setAmount(exportResp.getAmount());
            shippingExportResponseDTO.setProduct1(exportResp.getProduct1());
            shippingExportResponseDTO.setQty1(exportResp.getQty1());
            shippingExportResponseDTO.setPrice1(exportResp.getPrice1());
            shippingExportResponseDTO.setProduct2(exportResp.getProduct2());
            shippingExportResponseDTO.setQty2(exportResp.getQty2());
            shippingExportResponseDTO.setPrice2(exportResp.getPrice2());
            shippingExportResponseDTO.setProduct3(exportResp.getProduct3());
            shippingExportResponseDTO.setQty3(exportResp.getQty3());
            shippingExportResponseDTO.setPrice3(exportResp.getPrice3());
            shippingExportResponseDTO.setProduct4(exportResp.getProduct4());
            shippingExportResponseDTO.setQty4(exportResp.getQty4());
            shippingExportResponseDTO.setPrice4(exportResp.getPrice4());
            shippingExportResponseDTO.setLeadId(exportResp.getLeadId());
            shippingExportResponseDTO.setFfmCode(exportResp.getFfmCode());
            shippingExportResponseDTO.setFfmName(exportResp.getFfmName());
            shippingExportResponseDTO.setWarehouse(exportResp.getWarehouse());
            shippingExportResponseDTO.setCustomerName(exportResp.getCustomerName());
            shippingExportResponseDTO.setAgentNote(exportResp.getAgentNote());
            shippingExportResponseDTO.setAffiliateId(exportResp.getAffiliateId());
            shippingExportResponseDTO.setLastmileReturnCode(exportResp.getLastmileReturnCode());
            shippingExportResponseDTO.setPickedUpDate(exportResp.getPickedUpDate());
            shippingExportResponseDTO.setAttemp(exportResp.getAttemp());
            shippingExportResponseDTO.setPaymentStatus(exportResp.getPaidCod());
            shippingExportResponseDTO.setPaidCodDate(exportResp.getPaidCodDate());
            shippingExportResponseDTO.setFirstDeliveryTime(exportResp.getFirstDeliveryTime());
            shippingExportResponseDTO.setSecondDeliveryTime(exportResp.getSecondDeliveryTime());
            shippingExportResponseDTO.setThirdDeliveryTime(exportResp.getThirdDeliveryTime());
            shippingExportResponseDTO.setReturnTime(exportResp.getReturnTime());
            shippingExportResponseDTO.setCloseTime(exportResp.getCloseTime());
            shippingExportResponseDTO.setPickedTime(exportResp.getPickedTime());
            shippingExportResponseDTO.setPackedTime(exportResp.getPackedTime());
            shippingExportResponseDTO.setHandoveredTime(exportResp.getHandoveredTime());
            shippingExportResponseDTO.setCpName(exportResp.getCpName());
            shippingExportResponseDTO.setCategory(exportResp.getCategory());
            shippingExportResponseDTO.setNeighborhoodId(exportResp.getNeighborhoodId());
            shippingExportResponseDTO.setWardsId(exportResp.getWardsId());
            if(orderManagementRequestDTO.isDirector())
                shippingExportResponseDTO.setCustomerPhone(exportResp.getCustomerPhone());
            else
                shippingExportResponseDTO.setCustomerPhone(null);

            if(orderManagementRequestDTO.isTeamLeader()){
                shippingExportResponseDTO.setAddress(null);
                shippingExportResponseDTO.setTrackingCode(null);
            } else{
                shippingExportResponseDTO.setAddress(exportResp.getAddress());
                shippingExportResponseDTO.setTrackingCode(exportResp.getTrackingCode());
            }

            shippingExportResponseDTOS.add(shippingExportResponseDTO);
        }
        return shippingExportResponseDTOS;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLastmileReturnCode() {
        return lastmileReturnCode;
    }

    public void setLastmileReturnCode(String lastmileReturnCode) {
        this.lastmileReturnCode = lastmileReturnCode;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(String saleOrder) {
        this.saleOrder = saleOrder;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getWards() {
        return wards;
    }

    public void setWards(String wards) {
        this.wards = wards;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSoStatus() {
        return soStatus;
    }

    public void setSoStatus(String soStatus) {
        this.soStatus = soStatus;
    }

    public String getStatusTransport() {
        return statusTransport;
    }

    public void setStatusTransport(String statusTransport) {
        this.statusTransport = statusTransport;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Integer getTotalCall() {
        return totalCall;
    }

    public void setTotalCall(Integer totalCall) {
        this.totalCall = totalCall;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getProduct1() {
        return product1;
    }

    public void setProduct1(String product1) {
        this.product1 = product1;
    }

    public Double getQty1() {
        return qty1;
    }

    public void setQty1(Double qty1) {
        this.qty1 = qty1;
    }

    public Double getPrice1() {
        return price1;
    }

    public void setPrice1(Double price1) {
        this.price1 = price1;
    }

    public String getProduct2() {
        return product2;
    }

    public void setProduct2(String product2) {
        this.product2 = product2;
    }

    public Double getQty2() {
        return qty2;
    }

    public void setQty2(Double qty2) {
        this.qty2 = qty2;
    }

    public Double getPrice2() {
        return price2;
    }

    public void setPrice2(Double price2) {
        this.price2 = price2;
    }

    public String getProduct3() {
        return product3;
    }

    public void setProduct3(String product3) {
        this.product3 = product3;
    }

    public Double getQty3() {
        return qty3;
    }

    public void setQty3(Double qty3) {
        this.qty3 = qty3;
    }

    public Double getPrice3() {
        return price3;
    }

    public void setPrice3(Double price3) {
        this.price3 = price3;
    }

    public String getProduct4() {
        return product4;
    }

    public void setProduct4(String product4) {
        this.product4 = product4;
    }

    public Double getQty4() {
        return qty4;
    }

    public void setQty4(Double qty4) {
        this.qty4 = qty4;
    }

    public Double getPrice4() {
        return price4;
    }

    public void setPrice4(Double price4) {
        this.price4 = price4;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getFfmCode() {
        return ffmCode;
    }

    public void setFfmCode(String ffmCode) {
        this.ffmCode = ffmCode;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getFfmName() {
        return ffmName;
    }

    public void setFfmName(String ffmName) {
        this.ffmName = ffmName;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
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

    public Date getPickedUpDate() {
        return pickedUpDate;
    }

    public void setPickedUpDate(Date pickedUpDate) {
        this.pickedUpDate = pickedUpDate;
    }

    public Integer getAttemp() {
        return attemp;
    }

    public void setAttemp(Integer attemp) {
        this.attemp = attemp;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getPaidCodDate() {
        return paidCodDate;
    }

    public void setPaidCodDate(Date paidCodDate) {
        this.paidCodDate = paidCodDate;
    }

    public Date getSecondDeliveryTime() {
        return secondDeliveryTime;
    }

    public void setSecondDeliveryTime(Date secondDeliveryTime) {
        this.secondDeliveryTime = secondDeliveryTime;
    }

    public Date getThirdDeliveryTime() {
        return thirdDeliveryTime;
    }

    public void setThirdDeliveryTime(Date thirdDeliveryTime) {
        this.thirdDeliveryTime = thirdDeliveryTime;
    }

    public Date getFirstDeliveryTime() {
        return firstDeliveryTime;
    }

    public void setFirstDeliveryTime(Date firstDeliveryTime) {
        this.firstDeliveryTime = firstDeliveryTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getPickedTime() {
        return pickedTime;
    }

    public void setPickedTime(Date pickedTime) {
        this.pickedTime = pickedTime;
    }

    public Date getPackedTime() {
        return packedTime;
    }

    public void setPackedTime(Date packedTime) {
        this.packedTime = packedTime;
    }

    public Date getHandoveredTime() {
        return handoveredTime;
    }

    public void setHandoveredTime(Date handoveredTime) {
        this.handoveredTime = handoveredTime;
    }

    public String getNeighborhoodId() {
        return neighborhoodId;
    }

    public void setNeighborhoodId(String neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    public String getWardsId() {
        return wardsId;
    }

    public void setWardsId(String wardsId) {
        this.wardsId = wardsId;
    }
}
