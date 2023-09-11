//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.zhengjie.utils.date;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public DateUtils() {
    }

    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now(ZoneOffset.of("+8"));
    }

    public static LocalDate nowLocalDate() {
        return LocalDate.now(ZoneOffset.of("+8"));
    }

    public static LocalTime nowLocalTime() {
        return LocalTime.now(ZoneOffset.of("+8"));
    }

    public static LocalDateTime nowMAX() {
        return nowLocalDate().atTime(LocalTime.MAX);
    }

    public static LocalDateTime nowMIN() {
        return nowLocalDate().atTime(LocalTime.MIN);
    }

    public static String toStr(LocalDate localDate) {
        return localDate.format(DateFormatterUtils.dateFormatter());
    }

    public static String toStr(LocalDateTime localDateTime) {
        return localDateTime.format(DateFormatterUtils.dateTimeFormatter());
    }

    public static String toSStr(LocalDateTime localDateTime) {
        return localDateTime.format(DateFormatterUtils.dateTimeSFormatter());
    }

    public static String toStr(LocalTime localTime) {
        return localTime.format(DateFormatterUtils.timeFormatter());
    }

    public static String toSStr(LocalTime localTime) {
        return localTime.format(DateFormatterUtils.timesFormatter());
    }

    public static String toDateCron(LocalDateTime localTime) {
        return localTime.format(DateFormatterUtils.dateCronFormatter());
    }

    public static String toTimeCron(LocalTime localTime) {
        return localTime.format(DateFormatterUtils.timeCronFormatter());
    }

    public static String nowDateTimeCron() {
        return nowLocalDateTime().format(DateFormatterUtils.dateTimeCronFormatter());
    }

    public static String nowDateCron() {
        return nowLocalDateTime().format(DateFormatterUtils.dateCronFormatter());
    }

    public static String nowTimeCron() {
        return nowLocalTime().format(DateFormatterUtils.timeCronFormatter());
    }

    public static String toDateTimeCron(LocalDateTime localTime) {
        return localTime.format(DateFormatterUtils.dateTimeCronFormatter());
    }

    public static String nowLocalDateTimeStr() {
        return toStr(nowLocalDateTime());
    }

    public static String nowLocalDateStr() {
        return toStr(nowLocalDate());
    }

    public static String nowLocalTimeStr() {
        return toStr(nowLocalTime());
    }

    public static String nowLocalDateTimeSStr() {
        return toSStr(nowLocalDateTime());
    }

    public static String nowLocalTimeSStr() {
        return toSStr(nowLocalTime());
    }

    public static LocalDate[] thisWeekAllDay() {
        LocalDate firstDay = firstDayOfWeek(nowLocalDate(), weeks());
        LocalDate[] localDates = new LocalDate[7];

        for(int i = 0; i < 7; ++i) {
            localDates[i] = firstDay.plusDays((long)i);
        }

        return localDates;
    }

    public static LocalDate[] thisYearForMonthAllFirstDay() {
        LocalDate firstDay = firstDayOfYear();
        LocalDate[] localDates = new LocalDate[12];

        for(int i = 0; i < 12; ++i) {
            localDates[i] = firstDay.plusMonths((long)i);
        }

        return localDates;
    }

    public static LocalDate firstDayOfMonth() {
        return nowLocalDate().with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate lastDayOfMonth() {
        return nowLocalDate().with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate firstDayOfYear() {
        return nowLocalDate().with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDate lastDayOfYear() {
        return nowLocalDate().with(TemporalAdjusters.lastDayOfYear());
    }

    public static LocalDate firstDayOfWeek() {
        return nowLocalDate().with(TemporalAdjusters.dayOfWeekInMonth(weeks(), DayOfWeek.MONDAY)).minusDays(7L);
    }

    public static LocalDate lastDayOfWeek() {
        return nowLocalDate().with(TemporalAdjusters.dayOfWeekInMonth(weeks(), DayOfWeek.SUNDAY));
    }

    public static LocalDate firstDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate lastDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate firstDayOfYear(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDate lastDayOfYear(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfYear());
    }

    public static LocalDate firstDayOfWeek(LocalDate localDate, int weeks) {
        return localDate.with(TemporalAdjusters.dayOfWeekInMonth(weeks, DayOfWeek.MONDAY)).minusDays(7L);
    }

    public static LocalDate lastDayOfWeek(LocalDate localDate, int weeks) {
        return localDate.with(TemporalAdjusters.dayOfWeekInMonth(weeks, DayOfWeek.SUNDAY));
    }

    public static int weeks() {
        Calendar c = Calendar.getInstance();
        return c.get(4);
    }

    public static long toSecond(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    public static long toMilliSecond(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long toMilliSecond(LocalDate localDate) {
        return toMilliSecond(localDate.atTime(LocalTime.MIN));
    }

    public static long toMilliSecond(LocalTime localTime) {
        return localTime.getLong(ChronoField.MILLI_OF_DAY);
    }

    public static long nowDateTimeMilliSecond() {
        return nowLocalDateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static long nowDateMilliSecond() {
        return toMilliSecond(nowLocalDate().atTime(LocalTime.MIN));
    }

    public static long nowTimeMilliSecond() {
        return nowLocalTime().getLong(ChronoField.MILLI_OF_DAY);
    }

    public static LocalDateTime toLocalDateTime(long timestamp) {
        return (new Date(timestamp)).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }

    public static LocalDate toLocalDate(long timestamp) {
        return (new Date(timestamp)).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDate();
    }

    public static LocalTime toLocalTime(long timestamp) {
        return (new Date(timestamp)).toInstant().atZone(ZoneOffset.of("+8")).toLocalTime().minusHours(8L);
    }

    public static LocalDateTime toLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateFormatterUtils.dateTimeFormatter());
    }

    public static LocalDateTime toLocalDateTimeS(String dateTime) {
        return LocalDateTime.parse(dateTime, DateFormatterUtils.dateTimeSFormatter());
    }

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, DateFormatterUtils.dateFormatter());
    }

    public static LocalTime toLocalTime(String time) {
        return LocalTime.parse(time, DateFormatterUtils.timeFormatter());
    }

    public static LocalTime toLocalTimeS(String time) {
        return LocalTime.parse(time, DateFormatterUtils.timesFormatter());
    }

    public static LocalDateTime toMax(LocalDate localDate) {
        return localDate.atTime(max());
    }

    public static LocalDateTime toMin(LocalDate localDate) {
        return localDate.atTime(min());
    }

    public static LocalTime min() {
        return LocalTime.MIN;
    }

    public static LocalTime max() {
        return LocalTime.MAX;
    }
}
