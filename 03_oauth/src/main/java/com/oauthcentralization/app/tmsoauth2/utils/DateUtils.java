package com.oauthcentralization.app.tmsoauth2.utils;

import com.oauthcentralization.app.tmsoauth2.variables.PatternEpochVariable;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtils {

    private static final DateFormat dateFormat = new SimpleDateFormat(PatternEpochVariable.BIBLIOGRAPHY_EPOCH_PATTERN);

    public static String exchangeDateToGMT(Date dateReq) {
        SimpleDateFormat pattern = new SimpleDateFormat("EEE, d MMM yyyy");
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        pattern.setTimeZone(timeZone);
        return pattern.format(dateReq);
    }

    public static long snagDurationTypeElapsed(Date dateReq, String typeElapsed) {
        Date now = new Date();
        long endurance = now.getTime() - dateReq.getTime();

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

        switch (StringUtils.trimAllWhitespace(typeElapsed).toLowerCase()) {
            case "days":
                return elapsedDays;
            case "hours":
                return elapsedHours;
            case "minutes":
                return elapsedMinutes;
            case "seconds":
                return elapsedSeconds;
            default:
                return -1;
        }
    }

    public static Date subtractDays(Date dateReq, int days) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateReq);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
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

    public static long snapDuration(Date startTime, Date endTime, String elapse) {
        if (!ObjectUtils.allNotNull(startTime, endTime, elapse)) {
            return -1;
        }

        if (areMatches(startTime, endTime) == -1) {
            return -1;
        }

        long endurance = endTime.getTime() - startTime.getTime();
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

        switch (elapse.toLowerCase()) {
            case "days":
                return elapsedDays;
            case "hours":
                return elapsedHours;
            case "minutes":
                return elapsedMinutes;
            case "seconds":
                return elapsedSeconds;
            default:
                return -1;
        }
    }

    public static String feedStageAsString(Date epoch) {
        return dateFormat.format(epoch);
    }

    public static Date addDay(Date date, int day) {
        if (!ObjectUtils.allNotNull(date)) {
            date = new Date();
        }

        if (!ObjectUtils.allNotNull(day)) {
            return date;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static String snagPatternStage(String patternsStage, Date epochAsDate) {
        return new SimpleDateFormat(patternsStage).format(epochAsDate);
    }
}
