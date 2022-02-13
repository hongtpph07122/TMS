package com.tms.api.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.api.helper.EnumType;
import com.tms.api.helper.FlashHelper;
import com.tms.api.response.TMSResponse;
import com.tms.api.utils.ObjectUtils;
import com.tms.dto.DBResponse;
import com.tms.dto.GetDoNew;
import com.tms.dto.GetDoNewResp;
import com.tms.ff.dto.response.flash.FlashNotifyResponse;
import com.tms.service.impl.DeliveryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Component
public class FlashJob {
    @Autowired
    private DeliveryOrderService deliveryOrderService;

    @Value("${config.flash-notification}")
    public Boolean isFlashNotification;

    @Scheduled(cron = "0 0 14 * * ?")
    public TMSResponse sendFlashNotification() throws IOException, ParseException {
        System.out.println("FlashJob.sendFlashNotification START*******************START");
        if (!isFlashNotification)
            return null;

        TMSResponse response = new TMSResponse();
        FlashNotifyResponse noti;
        String SESSION_ID = UUID.randomUUID().toString();
        GetDoNew doNew = new GetDoNew();
        DBResponse<List<GetDoNewResp>> dos;
        int tot, totNew, totPacked;

        doNew.setFfmId(EnumType.FFM_ID.MYCLOUD.getValue());
        doNew.setCarrierId(EnumType.CARRIER_ID.FLASH_TH.getValue());
        doNew.setStatus(EnumType.DO_STATUS_ID.NEW.getValue());
        dos = deliveryOrderService.getDoNew(SESSION_ID, doNew);
        totNew = ObjectUtils.isNull(dos) ? 0 : dos.getRowCount();
        System.out.println("Total parcels NEW: " + totNew);

        doNew.setStatus(EnumType.DO_STATUS_ID.READY_TO_PICK.getValue());
        dos = deliveryOrderService.getDoNew(SESSION_ID, doNew);
        totPacked = ObjectUtils.isNull(dos) ? 0 : dos.getRowCount();
        System.out.println("Total parcels PACKED: " + totPacked);

        tot = totNew + totPacked;
        if (tot > 0) {
            noti = FlashHelper.sendNotification(tot);
            return response.buildResponse(noti, tot, new ObjectMapper().writeValueAsString(noti), 200);
        }

        return null;
    }

}
