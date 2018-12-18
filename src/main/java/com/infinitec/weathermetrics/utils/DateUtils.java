package com.infinitec.weathermetrics.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    public static Date startOfNthDay(int n) {
        return getNextNthDayWithTime(n, 6, 0, 0);
    }

    public static Date endOfNthDay(int n) {
        return getNextNthDayWithTime(n, 18, 0, 0);
    }

    public static Date midnightOfNthDay(int n) {
        return getNextNthDayWithTime(n, 0, 0, 0);
    }

    public static Date getNextNthDayWithTime(int n, int hh, int mm, int ss) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        calendar.set(Calendar.SECOND, ss);

        calendar.add(Calendar.DATE, n);

        return calendar.getTime();
    }
}
