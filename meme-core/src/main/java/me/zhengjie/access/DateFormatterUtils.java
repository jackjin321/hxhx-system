package me.zhengjie.access;

import java.time.format.DateTimeFormatter;

public class DateFormatterUtils {
    public DateFormatterUtils() {
    }

    public static DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static DateTimeFormatter dateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public static DateTimeFormatter timeFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    public static DateTimeFormatter timeCronFormatter() {
        return DateTimeFormatter.ofPattern("ss mm HH * * ? *");
    }

    public static DateTimeFormatter dateTimeCronFormatter() {
        return DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy");
    }

    public static DateTimeFormatter dateCronFormatter() {
        return DateTimeFormatter.ofPattern("ss mm HH dd MM ? *");
    }

    public static DateTimeFormatter timesFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm:ss SSS");
    }

    public static DateTimeFormatter dateTimeSFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");
    }
}
