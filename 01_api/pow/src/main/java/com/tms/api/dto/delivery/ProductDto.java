package com.tms.api.dto.delivery;

/**
 * Created by dinhanhthai on 27/07/2019.
 */
public class ProductDto {
    private Integer proId;
    private String proName;
    private String proCode;
    private String proPartnerCode;
    private Integer qty;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private Double price;

    public ProductDto(){
        this.weight = 100D;
        this.length = 10D;
        this.width = 10D;
        this.height = 10D;
        this.qty = 1;
    }

    public String getProPartnerCode() {
        return proPartnerCode;
    }

    public void setProPartnerCode(String proPartnerCode) {
        this.proPartnerCode = proPartnerCode;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
