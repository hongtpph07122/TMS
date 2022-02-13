package com.oauthcentralization.app.tmsoauth2.jobs;

import com.oauthcentralization.app.tmsoauth2.service.InvokeMechanismUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Component
@ConditionalOnProperty(name = "tms.invoke-mechanism-system-change-password.enable")
public class InvokeMechanismUserSystemJobs {

    private static final Logger logger = LoggerFactory.getLogger(InvokeMechanismUserSystemJobs.class);

    private final InvokeMechanismUserService invokeMechanismService;

    @Value("${tms.invoke-mechanism-system-change-password.enable}")
    private boolean isActiveInvokeMechanism;

    @Value(("${tms.invoke-mechanism-system-change-password.no-days-expired}"))
    private int noOfDays;

    @Autowired
    public InvokeMechanismUserSystemJobs(InvokeMechanismUserService invokeMechanismService) {
        this.invokeMechanismService = invokeMechanismService;
    }

    @Scheduled(cron = "${tms.invoke-mechanism-system-change-password.cron.expression_1}")
    public void scheduleInvokeMechanismSystemJobs() {
        if (!ObjectUtils.isEmpty(isActiveInvokeMechanism)) {
            if (isActiveInvokeMechanism) {
                boolean isSuccess = invokeMechanismService.changeUPasswordMechanismAutomate(noOfDays);
                if (isSuccess) {
                    logger.info("{}", "System sent new password to all user has account is expired, on ".concat(new Date().toString()));
                }
            }
        }

    }
}
