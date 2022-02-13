package com.tms.api.utils;

import com.tms.api.variable.PatternEpochVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private static final DateFormat dateFormat = new SimpleDateFormat(PatternEpochVariable.BIBLIOGRAPHY_EPOCH_PATTERN);

    public static String exchangeSecondToTimeHHMMSSFormat(int seconds) {
        Date date = new Date(seconds * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return simpleDateFormat.format(date);
    }

    public static Date parse(String epochAsDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(epochAsDate);
        } catch (ParseException error) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(epochAsDate);
            } catch (ParseException errorMessage) {
                logger.error(errorMessage.getMessage(), errorMessage);
                return null;
            }
        }
    }

    public static String feedStageAsString(Date epoch) {
        if (ObjectUtils.isNull(epoch)) {
            return "";
        }
        return dateFormat.format(epoch);
    }


    public static String snagPatternStage(String patternsStage, Date epochAsDate) {
        if (!ObjectUtils.allNotNull(patternsStage, epochAsDate)) {
            return "";
        }
        return new SimpleDateFormat(patternsStage).format(epochAsDate);
    }

    public static long feedAsNumberSecondOfHHMMSS(String hhMmSS) {
        DateFormat dateFormat = new SimpleDateFormat(PatternEpochVariable.SHORT_TIME_BIBLIOGRAPHY_EPOCH_PATTERN);
        long seconds = 0;

        if (StringUtils.isEmpty(hhMmSS)) {
            return seconds;
        }

        try {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse(hhMmSS);
            seconds = date.getTime() / 1000L;
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return seconds;
    }

    public static String feedAsSecondInHHMMSS(long seconds) {
        long part_1 = seconds % 60;
        long part_2 = seconds / 60;
        long part_3 = part_2 % 60;
        part_2 = part_2 / 60;
        return StringUtils.trimAllWhitespace(part_2 + ":" + part_3 + ":" + part_1);
    }

    public static Date addHours(Date date, int hours) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date feedStageAsDate(LocalDate epoch) {
        if (!ObjectUtils.allNotNull(epoch)) {
            return null;
        }
        return Date.from(epoch.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate subtractDate(LocalDate epoch, long days) {
        if (!ObjectUtils.allNotNull(epoch, days)) {
            return null;
        }

        return epoch.minusDays(days);
    }

    public static Date feedStageAsDate(LocalDateTime epoch) {
        if (!ObjectUtils.allNotNull(epoch)) {
            return null;
        }
        return Date.from(epoch.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * +) > : 1 -> req > now
     * +) < : -1 -> req < now
     * +) = : 0 -> req = now
     */
    public static int areMatches(Date now, Date request) {
        Calendar nowCalendar = Calendar.getInstance();
        Calendar requestCalendar = Calendar.getInstance();
        nowCalendar.setTime(request);
        requestCalendar.setTime(now);
        if (nowCalendar.after(requestCalendar)) {
            return 1;
        } else if (nowCalendar.before(requestCalendar)) {
            return -1;
        } else {
            return 0;
        }
    }

    public static long snapTimeLeft(Date nowTime, Date reqTime, String elapse) {
        if (!ObjectUtils.allNotNull(nowTime, reqTime, elapse)) {
            return -1;
        }

        if (areMatches(nowTime, reqTime) == -1) {
            return -1;
        }

        long endurance = reqTime.getTime() - nowTime.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = endurance / daysInMilli;
        endurance = endurance % daysInMilli;

        long elapsedHours = endurance / hoursInMilli;
        endurance = endurance % hoursInMilli;

        long elapsedMinutes = endurance / minutesInMilli;
        endurance = endurance % minutesInMilli;

        long elapsedSeconds = endurance / secondsInMilli;

        switch (StringUtility.trimSingleWhitespace(elapse).toLowerCase()) {
            case "days":
            case "day":
                return elapsedDays;
            case "hours":
            case "hour":
                return elapsedHours;
            case "minutes":
            case "minute":
                return elapsedMinutes;
            case "seconds":
            case "second":
                return elapsedSeconds;
            default:
                return -1;
        }
    }

    public static Date parse(String epochAsDate, String patternsStage) {
        try {
            if (StringUtility.isEmpty(epochAsDate)) {
                return null;
            }
            if (StringUtility.isEmpty(patternsStage)) {
                return new SimpleDateFormat(PatternEpochVariable.BIBLIOGRAPHY_EPOCH_PATTERN).parse(epochAsDate);
            }
            return new SimpleDateFormat(patternsStage).parse(epochAsDate);
        } catch (ParseException error) {
            try {
                return new SimpleDateFormat(PatternEpochVariable.SHORT_BIBLIOGRAPHY_EPOCH_PATTERN).parse(epochAsDate);
            } catch (ParseException errorMessage) {
                logger.error(errorMessage.getMessage());
                return null;
            }
        }
    }

    public static Date convertTime(String dateTime) {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(dateTime);
        } catch (ParseException e) {
            return null;
        }
    }
}
