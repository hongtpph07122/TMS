package com.tms.api.entity;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "od_sale_order")
public class OdSaleOrder implements Serializable {

    @Id
    @SequenceGenerator(name="saleorder_generator", sequenceName = "seq_so_id", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "saleorder_generator")
    @Column(name = "so_id")
    private Integer soId;
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "cp_id")
    private Integer cpId;
    @Column(name = "ag_id")
    private Integer agId;
    @Column(name = "lead_id")
    private Integer leadId;
    @Column(name = "lead_name")
    private String leadName;
    @Column(name = "lead_phone")
    private String leadPhone;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "payment_method")
    private Integer paymentMethod;
    @Column(name = "status")
    private Integer status;
    @Column(name = "createby")
    private Integer createby;
    @Column(name = "createdate")
    private Date createdate;
    @Column(name = "modifyby")
    private Integer modifyby;
    @Column(name = "modifydate")
    private Date modifydate;
    @Column(name = "amount_deposit")
    private Double amountDeposit;
    @Column(name = "amount_postpaid")
    private Double amountPostpaid;
    @Column(name = "list_price")
    private Double listPrice;
    @Column(name = "discount_level")
    private Integer discountLevel;
    @Column(name = "discount_type_1")
    private Integer discountType1;
    @Column(name = "unit_1")
    private Integer unit1;
    @Column(name = "discount_cash_1")
    private Double discountCash1;
    @Column(name = "discount_percent_1")
    private Double discountPercent1;
    @Column(name = "discount_type_2")
    private Integer discountType2;
    @Column(name = "unit_2")
    private Integer unit2;
    @Column(name = "discount_cash_2")
    private Double discountCash2;
    @Column(name = "discount_percent_2")
    private Double discountPercent2;
    @Column(name = "discount_type_3")
    private Integer discountType3;
    @Column(name = "unit_3")
    private Integer unit3;
    @Column(name = "discount_cash_3")
    private Double discountCash3;
    @Column(name = "discount_percent_3")
    private Double discountPercent3;
    @Column(name = "discount_type_4")
    private Integer discountType4;
    @Column(name = "unit_4")
    private Integer unit4;
    @Column(name = "discount_cash_4")
    private Double discountCash4;
    @Column(name = "discount_percent_4")
    private Double discountPercent4;
    @Column(name = "is_validated", nullable = false)
    private Boolean isValidated;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "validate_by")
    private Integer validateBy;
    @Column(name = "reason")
    private String reason;
    @Column(name = "qa_note")
    private String qaNote;
    @Column(name = "appointment_date")
    private Date appointmentDate;

    public Boolean getValidated() {
        return isValidated;
    }

    public void setValidated(Boolean validated) {
        isValidated = validated;
    }

    public Double getAmountDeposit() {
        return amountDeposit;
    }

    public void setAmountDeposit(Double amountDeposit) {
        this.amountDeposit = amountDeposit;
    }

    public Double getAmountPostpaid() {
        return amountPostpaid;
    }

    public void setAmountPostpaid(Double amountPostpaid) {
        this.amountPostpaid = amountPostpaid;
    }

    public Integer getSoId() {
        return soId;
    }

    public void setSoId(Integer soId) {
        this.soId = soId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getAgId() {
        return agId;
    }

    public void setAgId(Integer agId) {
        this.agId = agId;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public Date getModifydate() {
        return modifydate;
    }

    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    public Double getListPrice() {
        return listPrice;
    }

    public void setListPrice(Double listPrice) {
        this.listPrice = listPrice;
    }

    public Integer getDiscountLevel() {
        return discountLevel;
    }

    public void setDiscountLevel(Integer discountLevel) {
        this.discountLevel = discountLevel;
    }

    public Integer getDiscountType1() {
        return discountType1;
    }

    public void setDiscountType1(Integer discountType1) {
        this.discountType1 = discountType1;
    }

    public Integer getUnit1() {
        return unit1;
    }

    public void setUnit1(Integer unit1) {
        this.unit1 = unit1;
    }

    public Double getDiscountCash1() {
        return discountCash1;
    }

    public void setDiscountCash1(Double discountCash1) {
        this.discountCash1 = discountCash1;
    }

    public Double getDiscountPercent1() {
        return discountPercent1;
    }

    public void setDiscountPercent1(Double discountPercent1) {
        this.discountPercent1 = discountPercent1;
    }

    public Integer getDiscountType2() {
        return discountType2;
    }

    public void setDiscountType2(Integer discountType2) {
        this.discountType2 = discountType2;
    }

    public Integer getUnit2() {
        return unit2;
    }

    public void setUnit2(Integer unit2) {
        this.unit2 = unit2;
    }

    public Double getDiscountCash2() {
        return discountCash2;
    }

    public void setDiscountCash2(Double discountCash2) {
        this.discountCash2 = discountCash2;
    }

    public Double getDiscountPercent2() {
        return discountPercent2;
    }

    public void setDiscountPercent2(Double discountPercent2) {
        this.discountPercent2 = discountPercent2;
    }

    public Integer getDiscountType3() {
        return discountType3;
    }

    public void setDiscountType3(Integer discountType3) {
        this.discountType3 = discountType3;
    }

    public Integer getUnit3() {
        return unit3;
    }

    public void setUnit3(Integer unit3) {
        this.unit3 = unit3;
    }

    public Double getDiscountCash3() {
        return discountCash3;
    }

    public void setDiscountCash3(Double discountCash3) {
        this.discountCash3 = discountCash3;
    }

    public Double getDiscountPercent3() {
        return discountPercent3;
    }

    public void setDiscountPercent3(Double discountPercent3) {
        this.discountPercent3 = discountPercent3;
    }

    public Integer getDiscountType4() {
        return discountType4;
    }

    public void setDiscountType4(Integer discountType4) {
        this.discountType4 = discountType4;
    }

    public Integer getUnit4() {
        return unit4;
    }

    public void setUnit4(Integer unit4) {
        this.unit4 = unit4;
    }

    public Double getDiscountCash4() {
        return discountCash4;
    }

    public void setDiscountCash4(Double discountCash4) {
        this.discountCash4 = discountCash4;
    }

    public Double getDiscountPercent4() {
        return discountPercent4;
    }

    public void setDiscountPercent4(Double discountPercent4) {
        this.discountPercent4 = discountPercent4;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getValidateBy() {
        return validateBy;
    }

    public void setValidateBy(Integer validateBy) {
        this.validateBy = validateBy;
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

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
