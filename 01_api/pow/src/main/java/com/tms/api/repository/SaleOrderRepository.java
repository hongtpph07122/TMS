package com.tms.api.repository;

import com.tms.api.entity.OdSaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface SaleOrderRepository extends JpaRepository<OdSaleOrder,Integer> {

    @Query(value = "from OdSaleOrder a where a.status = 357 and a.creationDate <= ?1 ")
    List<OdSaleOrder> getListSaleOrderDelay(Timestamp endTime);

    @Query(value = "from OdSaleOrder a where a.soId = ?1 ")
    OdSaleOrder getSaleOrder(Integer soId);

    @Query(value = "from OdSaleOrder saleOrder  where saleOrder.leadId = ?1 ")
    OdSaleOrder findByLeadId(Integer leadId);
}
