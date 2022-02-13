package com.tms.api.service.impl;

import com.tms.api.response.AppointmentDateResponse;
import com.tms.api.service.AppointmentDateService;
import com.tms.api.utils.DateUtils;
import com.tms.api.utils.ObjectUtils;
import com.tms.api.utils.StringUtility;
import com.tms.api.utils.ValidationUtils;
import com.tms.api.variable.PatternEpochVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service(value = "appointmentDateService")
@Transactional
public class AppointmentDateServiceImpl implements AppointmentDateService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentDateServiceImpl.class);

    @Value("${config.appointment-date.enable}")
    private boolean active;

    @Value("${config.appointment-date.folio-limit}")
    private int folioLimit;

    @Value("${config.appointment-date.mod-limit}")
    private String modLimit;

    @Override
    public AppointmentDateResponse init() {
        AppointmentDateResponse appointmentDateResponse = new AppointmentDateResponse();
        appointmentDateResponse.setActive(ObjectUtils.allNotNull(active) && active);
        appointmentDateResponse.setFolioLimit(ObjectUtils.allNotNull(folioLimit) && folioLimit < 3 ? folioLimit : 1);
        appointmentDateResponse.setModLimit(!StringUtility.isEmpty(modLimit) ? modLimit : "week");
        return appointmentDateResponse;
    }

    @Override
    public boolean isValid(String appointmentDate) {
        if (StringUtility.isEmpty(appointmentDate) || !ValidationUtils.isVerifiedAsDate(appointmentDate)) {
            return false;
        }
        AppointmentDateResponse appointmentDateResponse = init();
        Date now = new Date();
        String nowStr = DateUtils.snagPatternStage(PatternEpochVariable.SHORT_BIBLIOGRAPHY_EPOCH_PATTERN, now);
        Date reqTime = DateUtils.parse(StringUtility.trimSingleWhitespace(appointmentDate), PatternEpochVariable.SHORT_BIBLIOGRAPHY_EPOCH_PATTERN);
        Date nowTime = DateUtils.parse(nowStr, PatternEpochVariable.SHORT_BIBLIOGRAPHY_EPOCH_PATTERN);
        int resultOfMatches = DateUtils.areMatches(nowTime, reqTime);

        if (resultOfMatches == -1) {
            return false;
        }

        String elapse = (StringUtility.trimSingleWhitespace(appointmentDateResponse.getModLimit()).toLowerCase().equalsIgnoreCase("week") ||
                StringUtility.trimSingleWhitespace(appointmentDateResponse.getModLimit()).toLowerCase().equalsIgnoreCase("weeks")) ?
                "days" : StringUtility.trimSingleWhitespace(appointmentDateResponse.getModLimit()).toLowerCase();
        int unit = (StringUtility.trimSingleWhitespace(appointmentDateResponse.getModLimit()).toLowerCase().equalsIgnoreCase("week") ||
                StringUtility.trimSingleWhitespace(appointmentDateResponse.getModLimit()).toLowerCase().equalsIgnoreCase("weeks")) ?
                7 : 2;

        long duration = DateUtils.snapTimeLeft(nowTime, reqTime, elapse);

        int limit = elapse.equalsIgnoreCase("days") ? (unit * appointmentDateResponse.getFolioLimit()) : unit;

        return duration >= 0 && duration <= limit;
    }

}
