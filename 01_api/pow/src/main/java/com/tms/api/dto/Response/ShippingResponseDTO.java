package com.tms.api.dto.Response;

import com.tms.api.dto.ShippingDto;

import java.util.List;

public class ShippingResponseDTO {

    private List<ShippingDto> listShipping;
    private Integer counter;

    public ShippingResponseDTO(){}

    public ShippingResponseDTO(List<ShippingDto> listShipping, Integer counter) {
        this.listShipping = listShipping;
        this.counter = counter;
    }

    public List<ShippingDto> getListShipping() {
        return listShipping;
    }

    public void setListShipping(List<ShippingDto> listShipping) {
        this.listShipping = listShipping;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}
