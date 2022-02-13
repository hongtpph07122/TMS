package com.tms.api.dto;

import com.tms.dto.GetDoNewResp;
import com.tms.entity.CLFresh;
import com.tms.entity.DeliveryOrder;

/**
 * Created by dinhanhthai on 10/07/2019.
 */
public class ShippingDto extends DeliveryOrder {
    private CLFresh customer;
    private String carrier;
    private String warehouse;
    private String errorCode;
    private String errorMessage;
    private String packageName;
    private String ffmCode;
    private String lastmileReturnCode;

    public String getLastmileReturnCode() {
        return lastmileReturnCode;
    }

    public void setLastmileReturnCode(String lastmileReturnCode) {
        this.lastmileReturnCode = lastmileReturnCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public CLFresh getCustomer() {
        return customer;
    }

    public void setCustomer(CLFresh customer) {
        this.customer = customer;
    }

    public String getFfmCode() {
        return ffmCode;
    }

    public void setFfmCode(String ffmCode) {
        this.ffmCode = ffmCode;
    }

    public ShippingDto() {

    }

    public ShippingDto(GetDoNewResp doNewResp) {
        this.setDoId(doNewResp.getDoId());
        this.setSoId(doNewResp.getSoId());
        this.setPartnerId(doNewResp.getCarrierId());
        this.setTrackingCode(doNewResp.getTrackingCode());
        this.setCustomerId(doNewResp.getCustomerId());
        this.setStatus(doNewResp.getStatus());
        this.setCreatedate(doNewResp.getCreatedate());
        this.setCreateby(doNewResp.getCreateby());
        this.setModifyby(doNewResp.getUpdateby());
        this.setModifydate(doNewResp.getUpdatedate());
        this.setApprovedTime(doNewResp.getApprovedTime());
        this.setExpectedDeliveryTime(doNewResp.getExpectedDeliveryTime());
        this.setExpectedTimeArrival(doNewResp.getExpectedArrivalTime());
        this.setCarrier(doNewResp.getCarrierName());
        this.setWarehouse(doNewResp.getWarehouseName());
        this.setErrorCode(doNewResp.getErrorCode());
        this.setErrorMessage(doNewResp.getErrorMessage());
        this.setPackageName(doNewResp.getPackageName());
        this.setFfmCode(doNewResp.getFfmCode());
        this.setLastmileReturnCode(doNewResp.getLastmileReturnCode());
    }
}
