package com.tms.api.task;

import com.tms.api.helper.Const;
import com.tms.api.helper.DateHelper;
import com.tms.api.helper.EnumType;
import com.tms.dto.DBResponse;
import com.tms.dto.GetUserParamsV5;
import com.tms.entity.User;
import com.tms.entity.log.UpdUserV2;
import com.tms.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class UserJob {
    private static final Logger logger = LoggerFactory.getLogger(UserJob.class);
    private final UserService userService;
    @Value("${config.run-expired-password:false}")
    public boolean runExpiredPassword;
    @Value("${config.day-expired-password:30}")
    public int dayExpiredPasswords;

    @Autowired
    public UserJob(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void autoCheckAgentPasswordIsExpired() {
        if (!runExpiredPassword)
            return;

        logger.info("---------- AUTO CHECK AGENT PASSWORD IS EXPIRED ----------");
        String ssid = UUID.randomUUID().toString();
        LocalDate dayEnd = LocalDate.now().minusDays(dayExpiredPasswords).plusDays(1);

        GetUserParamsV5 params = new GetUserParamsV5();
        params.setUserType(EnumType.USER_TYPE.AGENT.getValue());
        params.setUserLock(EnumType.USER_LOCK.NONE.getValue());
        params.setPasswordUpdateTime("|" + DateHelper.toTMSDateFormat(dayEnd));
        params.setIsExpired(Boolean.FALSE);
        DBResponse<List<User>> response = userService.getUserV5(ssid, params);
        if (response == null || response.getResult().isEmpty()) {
            logger.info("---------- NO AGENT NEED TO UPDATE EXPIRED! ----------");
            return;
        }

        int numberAgentExpired = 0;
        for (User user : response.getResult()) {
            UpdUserV2 updUser = new UpdUserV2();
            updUser.setUserId(user.getUserId());
            updUser.setIsExpired(Boolean.TRUE);
            updUser.setModifyby(Const.SYSTEM_ID);
            DBResponse<?> updResponse = userService.upUserV2(ssid, updUser);
            if (updResponse.getErrorCode() == 0)
                numberAgentExpired++;
        }
        logger.info(String.format("---------- CHANGE %d AGENT TO EXPIRED ----------", numberAgentExpired));
    }
}
