package com.tms.api.helper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by dinhanhthai on 30/09/2019.
 */
public class DateHelper {

    private static final String DATE_FORMAT_UI = "dd/MM/yyyy";
    private static final String DATE_TIME = "yyyyMMdd HH:mm:ss";

    private static final String DB_DATE = "yyyyMMdd";
    private static final String DB_DATE_TIME = "yyyyMMddHHmmss";
    private static final String GLOBAL_DATE = "yyyy-MM-dd";
    private static final String GLOBAL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static String toTMSFullDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DB_DATE_TIME);
        return simpleDateFormat.format(date.getTime());
    }

    public static Long toLongTMSFullDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DB_DATE_TIME);
        return Long.parseLong(simpleDateFormat.format(date.getTime()), 10);
    }

    public static String toTMSFullDateFormat(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DB_DATE_TIME);
        return localDateTime.format(formatter);
    }

    public static String toTMSDateFormat(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DB_DATE);
        return localDate.format(formatter);
    }

    public static String toTMSDateFormat() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DB_DATE);
        return simpleDateFormat.format(now.getTime());
    }

    public static String toTMSDateFormat(Date date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DB_DATE);
        return simpleDateFormat.format(date.getTime());
    }

    public static String toTMSDateTimeFormat() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME);
        return simpleDateFormat.format(now.getTime());
    }

    public static String toTMSDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME);
        return localDateTime.format(formatter);
    }

    public static String toTMSDateFormatUI(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_UI);
        return date.format(formatter);
    }

    public static String toTMSGlobalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GLOBAL_DATE_TIME);
        return localDateTime.format(formatter);
    }

    public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime convertFromTMSFullDateFormat(String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DB_DATE_TIME);
        return LocalDateTime.parse(dateFormat, formatter);
    }

    public static LocalDate convertFromTMSDateFormat(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DB_DATE);
        return LocalDate.parse(date, formatter);
    }

    public static LocalDate convertFromTMSFullDateFormatToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DB_DATE_TIME);
        return LocalDate.parse(date, formatter);
    }

    public static LocalDate convertToLocalDateViaMillisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Timestamp dateToTimestamp(String date) throws ParseException {
        if (!date.equals("")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_UI);
            Date parsedDate = dateFormat.parse(date);
            return new Timestamp(parsedDate.getTime());
        }
        return null;
    }

    public static Timestamp ghnDateToTimestamp(String date) throws ParseException {
        if (!date.equals("")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date parsedDate = dateFormat.parse(date);
            return new Timestamp(parsedDate.getTime());
        }
        return null;
    }

    public static Timestamp get30DaysAgo() {
        Date today30 = getDateBefore(30);
        return new Timestamp(today30.getTime());
    }

    public static Date getDateBefore(int numberOfDay) {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -numberOfDay);
        return cal.getTime();
    }

    public static Date addMinutesToDate(int minutes, Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = beforeTime.getTime();
        return new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
    }

    public static long calculateDifferenceTime(com.tms.entity.CLBasket clBasket, Date date) {
        return (date.getTime() - clBasket.getCreatedate().getTime()) / 1000;
    }

    public static Timestamp nowToTimestamp() {
        Date now = new Date();
        return new Timestamp(now.getTime());
    }

	public static Date getDateBeforeMonth(Integer numberOfMonth) {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.MONTH, -numberOfMonth);
        return cal.getTime();
	}
}
