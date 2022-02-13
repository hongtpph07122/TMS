package com.tms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;

import javax.persistence.*;
import javax.persistence.JoinColumns;

import com.tms.api.entity.RcActionMapping;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.FetchType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


@Entity
@DynamicUpdate
@Transactional
@Table(name = "od_do_new")
public class OdDONew implements Serializable {
    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "do_id")
    private Integer do_id;
    @Column(name = "org_id")
    private Integer org_id;
    @Column(name = "do_code")
    private String do_code;
    @Column(name = "ffm_code")
    private String ffm_code;
    @Column(name = "tracking_code")
    private String tracking_code;
    @Column(name = "so_id")
    private Integer so_id;
    @Column(name = "ffm_id")
    private Integer ffm_id;
    @Column(name = "warehouse_id")
    private Integer warehouse_id;
    @Column(name = "carrier_id")
    private Integer carrier_id;
    @Column(name = "customer_id")
    private Integer customer_id;
    @Column(name = "customer_name")
    private String customer_name;
    @Column(name = "customer_phone")
    private String customer_phone;
    @Column(name = "customer_address")
    private String customer_address;
    @Column(name = "customer_wards")
    private String customer_wards;
    @Column(name = "customer_district")
    private String customer_district;
    @Column(name = "customer_province")
    private String customer_province;
    @Column(name = "package_id")
    private String package_id;
    @Column(name = "package_name")
    private String package_name;
    @Column(name = "package_description")
    private String package_description;
    @Column(name = "amountcod")
    private Double amountcod;
    @Column(name = "status")
    private Integer status;
    @Column(name = "status_ffm")
    private String status_ffm;
    @Column(name = "status_lastmile")
    private String status_lastmile;
    @Column(name = "error_code")
    private String error_code;
    @Column(name = "error_message")
    private String error_message;
    @Column(name = "attribute1")
    private String attribute1;
    @Column(name = "attribute2")
    private String attribute2;
    @Column(name = "attribute3")
    private String attribute3;
    @Column(name = "attribute4")
    private String attribute4;
    @Column(name = "attribute5")
    private String attribute5;
    @Column(name = "createby")
    private Integer createby;
    @Column(name = "createdate")
    private Timestamp createdate;
    @Column(name = "updateby")
    private Integer updateby;
    @Column(name = "approved_time")
    private Timestamp approved_time;
    @Column(name = "expected_arrival_time")
    private Timestamp expected_arrival_time;
    @Column(name = "ffm_return_code")
    private String ffm_return_code;
    @Column(name = "ffm_reason")
    private String ffm_reason;
    @Column(name = "ffm_reason_detail")
    private String ffm_reason_detail;
    @Column(name = "lastmile_return_code")
    private String lastmile_return_code;
    @Column(name = "lastmile_reason")
    private String lastmile_reason;
    @Column(name = "lastmile_reason_detail")
    private String lastmile_reason_detail;
    @Column(name = "rescue_id")
    private Integer rescue_id;
    @Column(name = "is_active")
    private Integer is_active;
    @Column(name = "is_postback")
    private Integer is_postback;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(updatable = false, insertable = false, name = "lastmile_reason", referencedColumnName = "status_name"),
            @JoinColumn(updatable = false, insertable = false, name = "lastmile_reason_detail", referencedColumnName = "sub_status_name"),
            @JoinColumn(updatable = false, insertable = false, name = "org_id", referencedColumnName = "org_id")
    })

    private RcActionMapping rcActionMapping;
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
    	@JoinColumn(updatable = false, insertable = false, name = "customer_id", referencedColumnName = "lead_id")
	})
    private ClFreshEntity clFresh;

    @Column(name = "appointment_date")
    private Date appointmentDate;
    
    public RcActionMapping getRcActionMapping() {
        return rcActionMapping;
    }

    public void setRcActionMapping(RcActionMapping rcActionMapping) {
        this.rcActionMapping = rcActionMapping;
    }

    public Integer getDo_id() {
        return do_id;
    }

    public void setDo_id(Integer do_id) {
        this.do_id = do_id;
    }

    public Integer getOrg_id() {
        return org_id;
    }

    public void setOrg_id(Integer org_id) {
        this.org_id = org_id;
    }

    public String getDo_code() {
        return do_code;
    }

    public void setDo_code(String do_code) {
        this.do_code = do_code;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
    }

    public String getFfm_code() {
        return ffm_code;
    }

    public void setFfm_code(String ffm_code) {
        this.ffm_code = ffm_code;
    }

    public String getTracking_code() {
        return tracking_code;
    }

    public void setTracking_code(String tracking_code) {
        this.tracking_code = tracking_code;
    }

    public Integer getSo_id() {
        return so_id;
    }

    public void setSo_id(Integer so_id) {
        this.so_id = so_id;
    }

    public Integer getFfm_id() {
        return ffm_id;
    }

    public void setFfm_id(Integer ffm_id) {
        this.ffm_id = ffm_id;
    }

    public Integer getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Integer warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public Integer getCarrier_id() {
        return carrier_id;
    }

    public void setCarrier_id(Integer carrier_id) {
        this.carrier_id = carrier_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getCustomer_wards() {
        return customer_wards;
    }

    public void setCustomer_wards(String customer_wards) {
        this.customer_wards = customer_wards;
    }

    public String getCustomer_district() {
        return customer_district;
    }

    public void setCustomer_district(String customer_district) {
        this.customer_district = customer_district;
    }

    public String getCustomer_province() {
        return customer_province;
    }

    public void setCustomer_province(String customer_province) {
        this.customer_province = customer_province;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_description() {
        return package_description;
    }

    public void setPackage_description(String package_description) {
        this.package_description = package_description;
    }

    public Double getAmountcod() {
        return amountcod;
    }

    public void setAmountcod(Double amountcod) {
        this.amountcod = amountcod;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatus_ffm() {
        return status_ffm;
    }

    public void setStatus_ffm(String status_ffm) {
        this.status_ffm = status_ffm;
    }

    public String getStatus_lastmile() {
        return status_lastmile;
    }

    public void setStatus_lastmile(String status_lastmile) {
        this.status_lastmile = status_lastmile;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public Timestamp getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Timestamp createdate) {
        this.createdate = createdate;
    }

    public Integer getUpdateby() {
        return updateby;
    }

    public void setUpdateby(Integer updateby) {
        this.updateby = updateby;
    }

    public Timestamp getApproved_time() {
        return approved_time;
    }

    public void setApproved_time(Timestamp approved_time) {
        this.approved_time = approved_time;
    }

    public Timestamp getExpected_arrival_time() {
        return expected_arrival_time;
    }

    public void setExpected_arrival_time(Timestamp expected_arrival_time) {
        this.expected_arrival_time = expected_arrival_time;
    }

    public String getFfm_return_code() {
        return ffm_return_code;
    }

    public void setFfm_return_code(String ffm_return_code) {
        this.ffm_return_code = ffm_return_code;
    }

    public String getFfm_reason() {
        return ffm_reason;
    }

    public void setFfm_reason(String ffm_reason) {
        this.ffm_reason = ffm_reason;
    }

    public String getFfm_reason_detail() {
        return ffm_reason_detail;
    }

    public void setFfm_reason_detail(String ffm_reason_detail) {
        this.ffm_reason_detail = ffm_reason_detail;
    }

    public String getLastmile_return_code() {
        return lastmile_return_code;
    }

    public void setLastmile_return_code(String lastmile_return_code) {
        this.lastmile_return_code = lastmile_return_code;
    }

    public String getLastmile_reason() {
        return lastmile_reason;
    }

    public void setLastmile_reason(String lastmile_reason) {
        this.lastmile_reason = lastmile_reason;
    }

    public String getLastmile_reason_detail() {
        return lastmile_reason_detail;
    }

    public void setLastmile_reason_detail(String lastmile_reason_detail) {
        this.lastmile_reason_detail = lastmile_reason_detail;
    }

    public Integer getRescue_id() {
        return rescue_id;
    }

    public void setRescue_id(Integer rescue_id) {
        this.rescue_id = rescue_id;
    }

	public Integer getIs_postback() {
		return is_postback;
	}

	public void setIs_postback(Integer is_postback) {
		this.is_postback = is_postback;
	}

	public ClFreshEntity getClFresh() {
		return clFresh;
	}

	public void setClFresh(ClFreshEntity clFresh) {
		this.clFresh = clFresh;
	}

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
