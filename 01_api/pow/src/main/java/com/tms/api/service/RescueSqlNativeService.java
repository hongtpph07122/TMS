package com.tms.api.service;

public interface RescueSqlNativeService {

    boolean existByIdWithPreDelivery(Integer id, boolean isPreDelivery);
}
