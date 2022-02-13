package com.tms.api.dto.delivery;

import java.util.List;

import com.tms.api.helper.Helper;
import com.tms.entity.CLFresh;

/**
 * Created by dinhanhthai on 27/07/2019.
 */
public class DeliveryDto {
    private String SESSION_ID;
    private Integer orgId;
    private Integer doId;
    private String doCode;
    //lastmile
    private String warehouseShortname;
    private Integer lmProvinceId;
    private Integer lmDistrictId;
    //RECEIVE
    private Integer provinceId;
    private String provinceCode;
    private Integer districtId;
    private String districtCode;
    private Integer wardsId;
    private String wardsCode;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String address;
    private String phone;
    private String phone2;
    private String receiveName;
    private String email;
    private String note;

    private Integer lastmileId;
    //SENDER
    private Integer sProvinceId;
    private String sProvinceCode;
    private Integer sDistrictId;
    private String sDistrictCode;
    private Integer sWardsId;
    private String sProvinceName;
    private String sDistrictName;
    private String sWardName;
    private String sWardCode;
    private String sAddress;
    private String sPhone;
    private String sName;

    private Integer reicvWhProvinceId;
    private Integer reicvWhDistrictId;

    private Integer whCustomerPrvId;
    private Integer whCustomerDistrictId;

    private List<ProductDto> products;
    private String orderCode;
    private Integer cod_money;
    private Integer paymentMethod;

    private Integer warehouseId;

    private String packageName;
    private Integer amount;

    //VIETTEL param, store in DB
    private String customerId;
    //bp_warehouse.wh_code_inpartner
    private String groupAddressId;

    private Integer partnerId;
    //DHL config
    private String pickupId;
    private String soldToAccountId;
    private String pickupName;

    private String sZipCode;
    private String rZipCode;
    private String lastmileService;

    private String ffmCode;

    private String ghnWardCode;
    private String ghnDistrictCode;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getsDistrictCode() {
        return sDistrictCode;
    }

    public void setsDistrictCode(String sDistrictCode) {
        this.sDistrictCode = sDistrictCode;
    }

    public String getGhnWardCode() {
        return ghnWardCode;
    }

    public void setGhnWardCode(String ghnWardCode) {
        this.ghnWardCode = ghnWardCode;
    }

    public String getGhnDistrictCode() {
        return ghnDistrictCode;
    }

    public void setGhnDistrictCode(String ghnDistrictCode) {
        this.ghnDistrictCode = ghnDistrictCode;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getLastmileService() {
        return lastmileService;
    }

    public void setLastmileService(String lastmileService) {
        this.lastmileService = lastmileService;
    }

    public String getsWardCode() {
        return sWardCode;
    }

    public void setsWardCode(String sWardCode) {
        this.sWardCode = sWardCode;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getWardsCode() {
        return wardsCode;
    }

    public void setWardsCode(String wardsCode) {
        this.wardsCode = wardsCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getsProvinceCode() {
        return sProvinceCode;
    }

    public void setsProvinceCode(String sProvinceCode) {
        this.sProvinceCode = sProvinceCode;
    }

    public String getsZipCode() {
        return sZipCode;
    }

    public void setsZipCode(String sZipCode) {
        this.sZipCode = sZipCode;
    }

    public String getrZipCode() {
        return rZipCode;
    }

    public void setrZipCode(String rZipCode) {
        this.rZipCode = rZipCode;
    }

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }

    public String getPickupId() {
        return pickupId;
    }

    public void setPickupId(String pickupId) {
        this.pickupId = pickupId;
    }

    public String getSoldToAccountId() {
        return soldToAccountId;
    }

    public void setSoldToAccountId(String soldToAccountId) {
        this.soldToAccountId = soldToAccountId;
    }

    public String getPickupName() {
        return pickupName;
    }

    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public boolean isLocalProvince(){
        if(sProvinceId != null && provinceId != null){
            return (sProvinceId.equals(provinceId));
        }
        return false;
    }

    public Integer getReicvWhDistrictId() {
        return reicvWhDistrictId;
    }

    public void setReicvWhDistrictId(Integer reicvWhDistrictId) {
        this.reicvWhDistrictId = reicvWhDistrictId;
    }

    public Integer getReicvWhProvinceId() {
        return reicvWhProvinceId;
    }

    public void setReicvWhProvinceId(Integer reicvWhProvinceId) {
        this.reicvWhProvinceId = reicvWhProvinceId;
    }

    public Integer getWhCustomerPrvId() {
        return whCustomerPrvId;
    }

    public void setWhCustomerPrvId(Integer whCustomerPrvId) {
        this.whCustomerPrvId = whCustomerPrvId;
    }

    public Integer getWhCustomerDistrictId() {
        return whCustomerDistrictId;
    }

    public void setWhCustomerDistrictId(Integer whCustomerDistrictId) {
        this.whCustomerDistrictId = whCustomerDistrictId;
    }

    public String getDoCode() {
        return doCode;
    }

    public void setDoCode(String doCode) {
        this.doCode = doCode;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getGroupAddressId() {
        return groupAddressId;
    }

    public void setGroupAddressId(String groupAddressId) {
        this.groupAddressId = groupAddressId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public DeliveryDto(CLFresh fresh){
        this.address = fresh.getAddress();
        this.receiveName = fresh.getName();
        this.provinceId = Helper.IntergeTryParse(fresh.getProvince());
        this.districtId = Helper.IntergeTryParse(fresh.getDistrict());
        this.wardsId = Helper.IntergeTryParse(fresh.getSubdistrict());
        this.phone = fresh.getPhone();
        this.phone2 = fresh.getOtherPhone1();
        this.note = fresh.getComment();
    }

    public Integer getLastmileId() {
        return lastmileId;
    }

    public void setLastmileId(Integer lastmileId) {
        this.lastmileId = lastmileId;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public Integer getsProvinceId() {
        return sProvinceId;
    }

    public void setsProvinceId(Integer sProvinceId) {
        this.sProvinceId = sProvinceId;
    }

    public Integer getsDistrictId() {
        return sDistrictId;
    }

    public void setsDistrictId(Integer sDistrictId) {
        this.sDistrictId = sDistrictId;
    }

    public Integer getsWardsId() {
        return sWardsId;
    }

    public void setsWardsId(Integer sWardsId) {
        this.sWardsId = sWardsId;
    }

    public String getsProvinceName() {
        return sProvinceName;
    }

    public void setsProvinceName(String sProvinceName) {
        this.sProvinceName = sProvinceName;
    }

    public String getsDistrictName() {
        return sDistrictName;
    }

    public void setsDistrictName(String sDistrictName) {
        this.sDistrictName = sDistrictName;
    }

    public String getsWardName() {
        return sWardName;
    }

    public void setsWardName(String sWardName) {
        this.sWardName = sWardName;
    }

    public Integer getDoId() {
        return doId;
    }

    public void setDoId(Integer doId) {
        this.doId = doId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getWardsId() {
        return wardsId;
    }

    public void setWardsId(Integer wardsId) {
        this.wardsId = wardsId;
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

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getCod_money() {
        return cod_money;
    }

    public void setCod_money(Integer cod_money) {
        this.cod_money = cod_money;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getFfmCode() {
		return ffmCode;
	}

	public void setFfmCode(String ffmCode) {
		this.ffmCode = ffmCode;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

    public String getWarehouseShortname() {
        return warehouseShortname;
    }

    public void setWarehouseShortname(String warehouseShortname) {
        this.warehouseShortname = warehouseShortname;
    }

    public Integer getLmProvinceId() {
        return lmProvinceId;
    }

    public void setLmProvinceId(Integer lmProvinceId) {
        this.lmProvinceId = lmProvinceId;
    }

    public Integer getLmDistrictId() {
        return lmDistrictId;
    }

    public void setLmDistrictId(Integer lmDistrictId) {
        this.lmDistrictId = lmDistrictId;
    }
}
